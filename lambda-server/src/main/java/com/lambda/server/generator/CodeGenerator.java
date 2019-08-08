package com.lambda.server.generator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.springframework.util.StringUtils;

/**
 * 代码自动生成器
 */
public class CodeGenerator {

	Config config = null;

	// 开关，用来略过生成某些文件，或者进行配置文件修改，可以设置进行修改
	private boolean entityClsOn = false;
	private boolean formClsOn = false;
	private boolean mapperClsOn = false;
	private boolean mapperFileOn = false;
	private boolean serviceClsOn = false;
	private boolean implServiceClsOn = false;
	private boolean ctrlClsOn = false;
	private boolean configChgOn = false;

	// 文件夹名字，一般不可修改
	public static enum enmClassType {
		entity, form, mapper, mapperXML, service, serviceImpl, ctroller
	};

	// 内部属性，中间生成
	private List<Field> fields; // 字段Map, key:字段名 | value:类型
	private List<Field> viewFields; // 字段Map, key:字段名 | value:类型
	private Field keyField;

	private String editFieldPart = "";

	/**
	 * 构造函数
	 */
	public CodeGenerator(Config config) {
		super();
		this.config = config;
		fields = new ArrayList<Field>();
		viewFields = new ArrayList<Field>();
		keyField = null;

	}

	/**
	 * 开始生成代码，主要调用方法，必须事先设置好各项属性
	 * @date: 2017-0-12
	 */
	public void run() {
		try {
			System.out.println("Starting...");
			String sql = "SELECT * FROM " + config.getTableName() + " where 1=0";
			fields = getStructure(sql);
			if (StringUtils.isEmpty(config.getViewSql())) {
				viewFields = fields;
				config.setViewSql("SELECT * FROM " + config.getTableName());
			} else {
				String sql1 = config.getViewSql();
				if (sql1.indexOf(" where ") < 0)
					sql1 += " where 1=0 ";
				viewFields = getStructure(sql1);
			}

			if (isEntityClsOn()) {
				System.out.println("1-Writing entity class....");
				this.fillEntityClass();
			}
			
			if (isFormClsOn()) {
				System.out.println("2-Writing form class....");
				this.fillFormClass();
			}

			if (isMapperClsOn()) {
				System.out.println("3-Writing mapper class....");
				this.fillMapperClass();
			}
			
			if (isMapperFileOn()) {
				System.out.println("4-Writing mapper xml file....");
				this.fillMapperFile();
			}
			
			if (isServiceClsOn()) {
				System.out.println("5-Writing service class....");
				this.fillServiceClass();
			}

			if (isImplServiceClsOn()) {
				System.out.println("6-Writing service implemental class....");
				this.fillServiceImplClass();
			}

			if (isCtrlClsOn()) {
				System.out.println("7-Writing controller class....");
				this.fillControllerClass();
			}

			if (isConfigChgOn()) {
				System.out.println("8-Change MyBatis Config File....");
				this.registerFiles();
			}

			System.out.println("===Successfully generated files. ===");
			System.out
					.println("Please refresh your directory to view these files.");
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("Failed!!!!!!!!");
		}
	}

	/**
	 * 把创建的类和文件注册到daoDefault.xml和sqlmapConfigDefault.xml
	 *
	 */
	private void registerFiles() {
		try {
			// dao file
			String temp = "";
			String configFile = config.getBaseSrcFilePath().replace("java","resources") + "\\mybatis-config.xml";
			String content = FileManager.readFile(configFile);

			temp = "<mapper resource=\"" + config.getFullClsName(enmClassType.mapper).replace(".", "/") + ".xml\"/>";
			if (content.indexOf(temp) > 0) {
				// 已经存在
				return;
			}
			int pos = content.indexOf("</mappers>");

			content = content.substring(0, pos) + "\t" + temp + "\n\t" + content.substring(pos);
			FileManager.writeFile(content, configFile, true);

			System.out.println("Successfully set xml file to mybatis-config.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// fillMapperFile
	private void fillMapperFile() {
		StringBuffer content = new StringBuffer();

		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"")
				.append("\n\t")
				.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");

		content.append("\n<mapper namespace=\"")
				.append(config.getFullClsName(enmClassType.mapper))
				.append("\">");
		// 生成一个resultMap,使用hmViewFields
		content.append("\n\n<resultMap id=\"resultMap\" type=\"")
				.append(config.getFullClsName(enmClassType.entity))
				.append("\">");

		for (Field f : viewFields) {
			content.append("\n\t<result column=\"").append(f.getColName())
					.append("\" property=\"").append(f.getFieldName())
					.append("\" jdbcType=\"").append(f.getJdbcType())
					.append("\"/>");
		}
		content.append("\n</resultMap>");

		//getById
		content.append("\n");
		content.append("\n<select id=\"getById\" resultMap=\"resultMap\" parameterType=\"java.lang.String\">");
		content.append("\n\t")
				.append(config.getViewSql()).append(" WHERE ")
				.append(keyField.getColName()).append(" = #{")
				.append(keyField.getFieldName()).append("} ");
		if (config.isAreaFilter()) {
			content.append("\n\t\t and ").append(config.getAreaFilter(true));
		}
		content.append("\n</select>");

		//getByPage
		content.append("\n");
		content.append("\n<select id=\"getByPage\" resultMap=\"resultMap\">");
		content.append("\n\t").append(config.getViewSql()).append(" WHERE 1=1 ");

		if (config.isAreaFilter()) {
			content.append(" AND ").append(config.getAreaFilter(true, null));
		}
		content.append(getSQLWhere(null));
		String orderBy = config.getOrderBy(true);
		if(org.apache.commons.lang.StringUtils.isNotBlank(orderBy))content.append("\n\t\t").append(orderBy);
		content.append("\n</select>");

		////////////////查询结束,操作/////////////////////

		String temp = "";
		String temp1 = "";
		String temp2 = "";
		String temp3 = "";//动态更新
		int count = 0;
		for (Field f : fields) {//新增的字段
			count++;
			
			if (StringUtils.isEmpty(temp)) {
				temp = "\t\t" + f.getColName();
				temp1 = "\t\t#{" + f.getFieldName() + ",jdbcType=" + f.getJdbcType() + "}";
			} else {
				temp += ",\n\t\t" + f.getColName();
				temp1 += ",\n\t\t" + f.getBatisValue();
			}

			//更新的字段
			if (f.excludeUpdate())
				continue; // 跳过不可更新的一些字段
			if (keyField.equal(f))
				continue; // 跳过主键
			if (StringUtils.isEmpty(temp2)) {
				temp2 = "\t\t" + f.getColName() + " = " + f.getBatisValue();
			} else {
				temp2 += ",\n\t\t" + f.getColName() + " = " + f.getBatisValue();
			}
			
			temp3 += "\n\t\t<if test=\"" + f.getFieldName() + " != null\">";
			if(count<fields.size()){
				temp3 += "\n\t\t\t" + f.getColName() + " = " + f.getBatisValue() + ",";
			}else{
				temp3 += "\n\t\t\t" + f.getColName() + " = " + f.getBatisValue();
			}
			temp3 += "\n\t\t</if>";
		}
		
		//新增
		content.append("\n");
		content.append("\n<insert id=\"saveModel" + WordUtils.capitalize(config.getShortObjName()) + "\" useGeneratedKeys=\"true\" keyProperty=\"id\" parameterType=\"")
				.append(config.getFullClsName(enmClassType.entity))
				.append("\">");
		content.append("\n\tINSERT INTO ").append(config.getTableName()).append("(\n");
		content.append(temp).append("\n\t)VALUES(\n");
		content.append(temp1).append(")");
		content.append("\n</insert>");

		//修改
		content.append("\n");
		content.append("\n<update id=\"updateModel" + WordUtils.capitalize(config.getShortObjName()) + "\" keyProperty=\"id\" parameterType=\"")
				.append(config.getFullClsName(enmClassType.entity))
				.append("\">");
		content.append("\n\tUPDATE ").append(config.getTableName())
				.append(" SET\n");
		content.append(temp2);
		content.append(" \n\tWHERE ").append(keyField.getColName()).append(" = #{").append(keyField.getFieldName()).append("}");
		if (config.isAreaFilter()) {
			content.append("\n\t\t AND ").append(config.getAreaFilter(false));
		}
		content.append("\n</update>");
		
		//修改（只更新有值的）
		content.append("\n");
		content.append("\n<update id=\"updateModelSelective" + WordUtils.capitalize(config.getShortObjName()) + "\" keyProperty=\"id\" parameterType=\"")
				.append(config.getFullClsName(enmClassType.entity))
				.append("\">");
		content.append("\n\tUPDATE ").append(config.getTableName());
		content.append("\n\t\t<set>");
		content.append(temp3);
		content.append("\n\t\t</set>");
		content.append(" \n\tWHERE ").append(keyField.getColName()).append(" = #{").append(keyField.getFieldName()).append("}");
		if (config.isAreaFilter()) {
			content.append("\n\t\t AND ").append(config.getAreaFilter(false));
		}
		content.append("\n</update>");

		//删除
		content.append("\n");
		content.append("\n<delete id=\"deleteById\" parameterType=\"" + config.getFullClsName(enmClassType.entity) + "\">");
		content.append("\n\tDELETE FROM ").append(config.getTableName())
				.append(" WHERE id=#{id}");
		content.append("\n</delete>");

		//end
		content.append("\n\n</mapper>");
		try {
			FileManager.writeFile(content.toString(), config.getFullFileName(enmClassType.mapperXML), config.isOverwriteFile());
			System.out.println("Successfully wrote config file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: getSQLWhere
	 */
	private String getSQLWhere(String container) {
		StringBuffer content = new StringBuffer();
		String p = "";
		if (!StringUtils.isEmpty(container)) p = container + ".";
		String prefix = "";//别名
		for (Field f : fields) {

			if (config.isAreaFilter() && f.isAreaField()) continue;
			String type = f.getJdbcType().toUpperCase();
			if (type.equals("TIMESTAMP")) continue; // TIMESTAMP暂时不处理

			// else if(type.equals("INTEGER") || type.equals("SMALLINT")) {
			// content.append("\n\t\t<if test=\"").
			// append(f.getFieldName()).append(" &gt; 0\">");
			// content.append("\n\t\t\tand ").append(prefix +
			// f.getColName()).append(" = ").
			// append(f.getBatisValue());
			// content.append("\n\t\t</if>");
			// }
			else if (type.equals("VARCHAR")) {
				content.append("\n\t\t<if test=\"").append(p)
						.append(f.getFieldName()).append(" !=null and ")
						.append(p).append(f.getFieldName()).append(" !=''\">");
				content.append("\n\t\t\tAND INSTR(")
						.append(prefix + f.getColName()).append(",")
						.append(f.getBatisValue(container));
				content.append(")>0 \n\t\t</if>");
			}else {
				content.append("\n\t\t<if test=\"").append(p)
						.append(f.getFieldName()).append(" !=null\">");
				content.append("\n\t\t\tAND ").append(prefix + f.getColName())
						.append(" = ").append(f.getBatisValue(container));
				content.append("\n\t\t</if>");
			}
		}
		// /content.replace(start, end, str)
		return content.toString();
	}

	/**
	 * 填充Entity类
	 * @Title: fillEntityClass
	 */
	private void fillEntityClass() {
		StringBuffer content = new StringBuffer("package ");
		StringBuffer c1 = new StringBuffer("");
		content.append(getPkgByClsFull(config.getFullClsName(enmClassType.entity)))
				.append(";\n");
		content.append("\nimport java.util.Date;");
		content.append("\nimport com.impawn.server.model.BaseModel;");

		content.append("\n\n");
		content.append(config.getAutoComment(""));
		content.append("public class ").append(config.getEntityClsName());
		content.append(" extends BaseModel{");

		content.append("\n\n\tprivate static final long serialVersionUID = 0L;\n");// .append(Long.).append(";");

		for (Field f : viewFields) {
			if(!(f.getFieldName()).equals(config.getKeyFieldName())){
				
				content.append("\n\tprivate ").append(f.getType()).append(" ")
				.append(f.getFieldName()).append(";");
				
				// getter
				c1.append("\n\n\tpublic ").append(f.getType()).append(" ")
				.append(f.getter()).append("{");
				c1.append("\n\t\treturn ").append(f.getFieldName())
				.append(";\n\t}");
				// setter
				c1.append("\n\n\tpublic void ")
				.append(f.setter(f.getType() + " " + f.getFieldName()))
				.append("{");
				c1.append("\n\t\tthis.").append(f.getFieldName()).append(" = ")
				.append(f.getFieldName()).append(";\n\t}");
			}
		}

		content.append(c1);
		content.append("\n}");
		try {
			FileManager.writeFile(content.toString(),
					config.getFullFileName(enmClassType.entity),
					config.isOverwriteFile());
			System.out.println("Successfully create entity class file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 填充Form类
	 * @Title: fillFormClass
	 */
	private void fillFormClass() {
		StringBuffer content = new StringBuffer("package ");
		String ent = config.getEntityClsName();
		StringBuffer c1 = new StringBuffer("");
		content.append(getPkgByClsFull(config.getFullClsName(enmClassType.form)))
				.append(";\n");
		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.entity)).append(";\n\n");

		content.append(config.getAutoComment("（查询表单）"));
		content.append("public class ").append(config.getFormClsName());
		content.append(" extends ").append(ent+"{");

		content.append("\n\n\tprivate static final long serialVersionUID = 0L;\n");// .append(Long.).append(";");
		content.append("\n\t").append("private " + ent + " " + low(ent)+";");

		content.append("\n\n\t").append("public ").append(ent).append(" get").append(ent).append("(){")
				.append("\n\t\t").append("return ").append(low(ent)+";")
				.append("\n\t}");
		
		content.append("\n\n\t").append("public ").append("void").append(" set").append(ent).append("("+ ent + " " + low(ent) +"){")
			.append("\n\t\t").append("this.").append(low(ent)+" = "+ low(ent) +";")
			.append("\n\t}");
		
		content.append(c1);
		content.append("\n}");
		try {
			FileManager.writeFile(content.toString(),
					config.getFullFileName(enmClassType.form),
					config.isOverwriteFile());
			System.out.println("Successfully create form class file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Service类
	 * @Title: fillServiceClass
	 */
	private void fillMapperClass() {
		StringBuffer content = new StringBuffer("package ");
		content.append(getPkgByClsFull(config.getFullClsName(enmClassType.mapper))).append(";\n");

		content.append("\nimport com.github.pagehelper.Page;");
		
		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.entity)).append(";\n");
		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.form)).append(";\n\n");

		content.append(config.getAutoComment(""));
		content.append("public interface ").append(config.getMapperClsName());
		content.append("{");
		
		//methods
		content.append("\n\n\tpublic ").append(config.getEntityClsName()).append(" getById(String id);");

		content.append("\n\n\tpublic Page<").append(config.getEntityClsName())
				.append(">").append(" getByPage(" + config.getEntityClsName() + "Form form);");

		content.append("\n\n\tpublic void saveModel" + WordUtils.capitalize(config.getShortObjName()) + "(")
				.append(config.getEntityClsName()).append(" " + low(config.getEntityClsName()) + ");");

		content.append("\n\n\tpublic void updateModel" + WordUtils.capitalize(config.getShortObjName()) + "(")
		.append(config.getEntityClsName()).append(" " + low(config.getEntityClsName()) + ");");
		
		content.append("\n\n\tpublic void updateModelSelective" + WordUtils.capitalize(config.getShortObjName()) + "(")
		.append(config.getEntityClsName()).append(" " + low(config.getEntityClsName()) + ");");

		content.append("\n\n\tpublic void deleteById(String id);");

		content.append("\n}");
		try {
			FileManager.writeFile(content.toString(),
					config.getFullFileName(enmClassType.mapper),
					config.isOverwriteFile());
			System.out.println("Successfully wrote mapper class file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 首字母小写
	 * @author WKX
	 * @param name 文字
	 */
	public static String low(String name) {
    	name = name.substring(0, 1).toLowerCase() + name.substring(1);
    	return  name;
    }

	/**
	 * 生成Service类
	 * @Title: fillServiceClass
	 */
	private void fillServiceClass() {
		StringBuffer content = new StringBuffer("package ");
		content.append(getPkgByClsFull(config.getFullClsName(enmClassType.service))).append(";\n");

		content.append("\nimport com.github.pagehelper.Page;");
		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.entity)).append(";");
		content.append("\nimport ").append(config.getFullClsName(enmClassType.form)).append(";\n");

		content.append("\n"+config.getAutoComment("Service"));
		content.append("public interface ").append(config.getServiceClsName());
		content.append("{");
		
		//methods
		content.append("\n\n\tpublic ")
				.append(config.getEntityClsName())
				.append(" getById(String id);");

		content.append("\n\n\tpublic Page<").append(config.getEntityClsName()).append("> getByPage(Integer pageIndex,Integer pageSize," + config.getEntityClsName() + "Form form);");

		content.append("\n\n\tpublic ").append("void")
				.append(" saveModel(").append(config.getEntityClsName())
				.append(" " + low(config.getEntityClsName()) + ") throws Exception;");

		content.append("\n\n\tpublic ").append("void")
				.append(" updateModel(").append(config.getEntityClsName())
				.append(" " + low(config.getEntityClsName()) + ") throws Exception;");

		content.append("\n\n\tpublic void deleteById(String id) throws Exception;");

		content.append("\n}");
		try {
			FileManager.writeFile(content.toString(), config.getFullFileName(enmClassType.service), config.isOverwriteFile());
			System.out.println("Successfully wrote service class file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成ServiceImpl类
	 * @Title: fillServiceImplClass
	 */
	private void fillServiceImplClass() {
		StringBuffer content = new StringBuffer("package ");
		content.append(getPkgByClsFull(config.getFullClsName(enmClassType.serviceImpl))).append(";\n");

		content.append("\nimport java.util.Date;");
		content.append("\nimport org.apache.commons.lang3.StringUtils;");
		content.append("\nimport javax.annotation.Resource;");
		content.append("\nimport org.springframework.stereotype.Service;");
		content.append("\nimport org.springframework.transaction.annotation.Transactional;");
		
		content.append("\nimport com.github.pagehelper.Page;");
		content.append("\nimport com.github.pagehelper.PageHelper;");
		content.append("\nimport com.impawn.server.utils.MyException;");

		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.entity)).append(";");
		content.append("\nimport ").append(config.getFullClsName(enmClassType.form)).append(";");
		content.append("\nimport ").append(config.getFullClsName(enmClassType.mapper)).append(";");
		content.append("\nimport ").append(config.getFullClsName(enmClassType.service)).append(";\n");

		content.append("\n"+config.getAutoComment("Service逻辑实现类"));
		content.append("@Service");
		content.append("\npublic class ")
				.append(config.getImplServiceClsName()).append(" implements ")
				.append(config.getServiceClsName());
		content.append("{");

		String mapperObj = WordUtils.uncapitalize(config.getMapperClsName());
		content.append("\n\n\t@Resource");
		content.append("\n\tprivate ").append(config.getMapperClsName())
				.append(" ").append(mapperObj).append(";");

		//methods
		content.append("\n\n\t@Override");
		content.append("\n\tpublic ")
				.append(config.getEntityClsName())
				.append(" getById(String id){");
		content.append("\n\t\treturn ").append(mapperObj).append(".getById(id);");
		content.append("\n\t}");

		content.append("\n\n\t@Override");
		content.append("\n\tpublic Page<").append(config.getEntityClsName()).append("> getByPage(Integer pageIndex,Integer pageSize," + config.getEntityClsName() + "Form form){");
		content.append("\n\t\tpageIndex = pageIndex == null ? 1 : pageIndex;");
		content.append("\n\t\tpageSize = pageSize == null ? 10 : pageSize;");
		content.append("\n\t\tPageHelper.startPage(pageIndex, pageSize);");
		content.append("\n\t\treturn " + mapperObj).append(".getByPage(form);");
		content.append("\n\t}");
		
		String name = low(config.getEntityClsName());
		content.append("\n\n\t@Override");
		content.append("\n\t@Transactional");
		content.append("\n\tpublic ").append("void")
				.append(" saveModel(").append(config.getEntityClsName() + " " + name)
				.append(") throws Exception{");
		content.append("\n\t\tif(").append(name + "==null) throw new MyException();");
		content.append("\n\t\t").append(name).append(".setCreateTime(new Date());");
		content.append("\n\t\t").append(mapperObj).append(".saveModel(" + name + ");");
		content.append("\n\t}");

		content.append("\n\n\t@Override");
		content.append("\n\t@Transactional");
		content.append("\n\tpublic ").append("void")
				.append(" updateModel(").append(config.getEntityClsName() + " " + name)
				.append(") throws MyException{");
		content.append("\n\t\tif(").append(name + "==null || ").append("StringUtils.isBlank("+name + ".getId())) throw new MyException();");
		
		content.append("\n\t\t").append(mapperObj).append(".updateModel(" + name + ");");
		content.append("\n\t}");
		
		content.append("\n\n\t@Override");
		content.append("\n\t@Transactional");
		content.append("\n\tpublic void deleteById(String id) throws MyException{");
		content.append("\n\t\t").append(mapperObj)
				.append(".deleteById(id);");
		content.append("\n\t}");

		content.append("\n}");
		try {
			FileManager.writeFile(content.toString(),
					config.getFullFileName(enmClassType.serviceImpl),
					config.isOverwriteFile());
			System.out.println("Successfully wrote service class file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Control类
	 * @author: xiaolong
	 * @Title: fillControllerClass
	 */
	private void fillControllerClass() {
		StringBuffer content = new StringBuffer("package ");
		content.append(getPkgByClsFull(config.getFullClsName(enmClassType.ctroller))).append(";\n");

		content.append("\nimport org.springframework.beans.factory.annotation.Autowired;");
		content.append("\nimport org.springframework.web.bind.annotation.RequestMapping;");
		content.append("\nimport org.springframework.web.bind.annotation.PostMapping;");
		content.append("\nimport org.springframework.web.bind.annotation.RestController;");
		
		content.append("\nimport com.github.pagehelper.Page;");
		content.append("\nimport com.github.pagehelper.PageInfo;");
		content.append("\nimport com.impawn.server.utils.BaseController;");
		content.append("\nimport com.impawn.server.utils.MyException;");

		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.entity)).append(";\n");
		content.append("\n\nimport ").append(config.getFullClsName(enmClassType.form)).append(";\n");
		content.append("\nimport ").append(config.getFullClsName(enmClassType.service)).append(";\n\n");

		content.append(config.getAutoComment("Controller类"));
		content.append("@RestController");
		content.append("\n@RequestMapping(value = \"/" + low(config.getEntityClsName()) + "\")");
		content.append("\npublic class ").append(config.getCtrlClsName())
				.append(" extends BaseController");
		content.append("{");

		String serviceObj = WordUtils.uncapitalize(config.getServiceClsName());
		content.append("\n\n\t@Autowired");
		content.append("\n\tprivate ").append(config.getServiceClsName())
				.append(" ").append(serviceObj).append(";");

		String name = low(config.getEntityClsName());
		
		//save
		content.append("\n");
		content.append("\n\t@PostMapping(value=\"/save\")");
		content.append("\n\tpublic ").append("String")
				.append(" save(").append(config.getEntityClsName()).append(" " + name)
				.append(") throws Exception{");

		
		content.append("\n\t\t").append("try {");
		
		content.append("\n\t\t\tif(" + name + " != null && ").append(name+".").append(keyField.getter())
				.append(" != null").append("){");
		
		content.append("\n\t\t\t\t" + serviceObj + ".updateModel(" + name + ");");
		content.append("\n\t\t\t}else if(" + name + "!=null){");
		content.append("\n\t\t\t\t" + serviceObj + ".saveModel(" + name + ");");
		content.append("\n\t\t\t}");
		
		content.append("\n\t\t").append("} catch (Exception e) {");
		content.append("\n\t\t\t").append("e.printStackTrace();");
		content.append("\n\t\t\treturn this.FAILED(e);");
		content.append("\n\t\t}");
		
		content.append("\n\t\treturn this.SUCCESS(\"操作成功\"," + name + ");");
		content.append("\n\t}");

		//getById
		content.append("\n");
		content.append("\n\t@PostMapping(value=\"/get\")");
		content.append("\n\tpublic ").append("String")
				.append(" getById(String id){");

		content.append("\n\t\treturn this.SUCCESS(\"操作成功\",").append(serviceObj)
				.append(".getById(id));");
		content.append("\n\t}");

		//getByPage
		content.append("\n");
		content.append("\n\t@PostMapping(value=\"/page\")");
		content.append("\n\tpublic String")
				.append(" page(Integer pageIndex,Integer pageSize,").append(config.getEntityClsName()+"Form")
				.append(" form){");
		content.append("\n\t\tPage<" + config.getEntityClsName() + "> p = new Page<"+ config.getEntityClsName() +">();");
		content.append("\n\t\t").append("try {");
		content.append("\n\t\t\t").append("p = ").append(serviceObj).append(".getByPage(pageIndex,pageSize,form);");
		content.append("\n\t\t").append("} catch (Exception e) {");
		content.append("\n\t\t\t").append("e.printStackTrace();");
		content.append("\n\t\t\treturn this.FAILED(e);");
		content.append("\n\t\t}");
		content.append("\n\t\treturn this.SUCCESS(\"操作成功\",").append("new PageInfo<>(p));");
		
		content.append("\n\t}");

		//deleteById
		content.append("\n");
		content.append("\n\t@PostMapping(value=\"/del\")");
		content.append("\n\tpublic String deleteById(String id){");
		
		
		
		
		
		content.append("\n\t\t").append("try {");
		
		content.append("\n\t\t\tif(id == null)").append("throw new MyException();");
		
		content.append("\n\t\t\t").append(serviceObj).append(".deleteById(id);");
		
		
		
		content.append("\n\t\t").append("} catch (Exception e) {");
		content.append("\n\t\t\t").append("e.printStackTrace();");
		content.append("\n\t\t\treturn this.FAILED(e);");
		content.append("\n\t\t}");
		content.append("\n\t\treturn this.SUCCESS(\"操作成功\");");
		
		content.append("\n\t}");
		
		
		
		
		
		
		

		content.append("\n}");
		try {
			FileManager.writeFile(content.toString(),
					config.getFullFileName(enmClassType.ctroller),
					config.isOverwriteFile());
			System.out.println("Successfully wrote service class file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用sessionUser 
	 * 
	 * @author: xiaolong
	 * @date: 2016年5月18日
	 * @Title: getAreaPara
	 * @param paraName
	 * @return
	 */
	public String getAreaPara(String paraName) {
		if (!config.isAreaFilter()) return "";
		String s = "\n\t\t";
		String r = s;
		r += "if(" + paraName + ".getUserId()==null) " + paraName + ".setUserId(user.getUserId());";

		return r;
	}

	/**
	 * 获取表的结构
	 * @Title: getStructure
	 * @throws SQLException
	 */
	protected List<Field> getStructure(String sql) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 直接取得数据库连接
			System.out.println("starting to get fields...");
			conn = ResourceManager.getConnection(config.getDbFile());
			st = conn.createStatement();

			rs = st.executeQuery(sql);
			ResultSetMetaData rsMt = rs.getMetaData();
			List<Field> result = new ArrayList<Field>();

			for (int i = 1; i <= rsMt.getColumnCount(); i++) {
				Field f = new Field();
				f.setColName(rsMt.getColumnLabel(i));
				f.setType(getTypeBySqlName(rsMt.getColumnClassName(i)));
				f.setFieldName(getPropByColName(f.getColName()));
				f.setJdbcType(getJdbcTypeBySqlName(rsMt.getColumnClassName(i)));
				result.add(f);
				if (keyField == null
						&& f.getColName().equalsIgnoreCase(
								config.getKeyFieldName())) {
					keyField = f;
				}
				if (StringUtils.isEmpty(editFieldPart)
						&& f.getColName().indexOf("edit_by") >= 0) {
					if (f.getColName().indexOf("last") >= 0)
						editFieldPart = "LastEdit";
					else
						editFieldPart = "Edit";
				}
			}
			System.out.println("Successed to get fields.");
			return result;
		} catch (SQLException e) {
			System.out.println("Failed to get fields.");
			e.printStackTrace();
			throw e;
		} finally {
			ResourceManager.closeQuietly(conn, st, rs);
		}
	}

	/**
	 * 根据SQL 的配型得到Java 的类型
	 * @Title: getClassBySqlName
	 * @param sqlName
	 */
	private String getTypeBySqlName(String sqlName) {
		String result = sqlName;
		result = result.replace("java.lang.", "");
		result = result.replace("Boolean", "Byte");
		result = result.replace("java.sql.Timestamp", "Date");
		return result;
	}

	private String getJdbcTypeBySqlName(String sqlName) {
		sqlName = sqlName.toUpperCase();
		String result = sqlName.substring(sqlName.lastIndexOf(".") + 1);

		if (sqlName.indexOf("STRING") > 0) {
			result = "VARCHAR";
		} else if (sqlName.indexOf("INTEGER") > 0) {
			result = "INTEGER";
		} else if (sqlName.indexOf("LONG") > 0) {
			result = "BIGINT";
		} else if (sqlName.indexOf("BOOLEAN") > 0) {
			result = "BIT";
		} else if (sqlName.indexOf("SHORT") > 0) {
			result = "SMALLINT";
		} else if (sqlName.indexOf("BIGDECIMAL") > 0) {
			result = "DECIMAL";
		}
		return result;
	}

	/**
	 * 根据列名得到entity的属性名
	 * @Title: getPropByColName
	 * @param colName
	 */
	private String getPropByColName(String colName) {
		// 分解下划线
		String[] cs = colName.split("_");
		String rs = "";
		for (String str : cs) {
			if (StringUtils.isEmpty(rs))
				rs += WordUtils.uncapitalize(str);
			else
				rs += WordUtils.capitalize(str);
		}
		return rs;
	}

	/**
	 * 根据全名得到package
	 * @Title: getPkgByClsFull
	 * @param clsFullName
	 */
	private String getPkgByClsFull(String clsFullName) {
		int i = clsFullName.lastIndexOf(".");
		return clsFullName.substring(0, i);
	}

	// ////////////////////////// 属性get/set //////////////////////////////

	public boolean isConfigChgOn() {
		return configChgOn;
	}

	/**
	 * 是否需要修改配置文件
	 * @Title: setConfigChgOn
	 * @param configChgOn
	 */
	public void setConfigChgOn(boolean configChgOn) {
		this.configChgOn = configChgOn;
	}

	public boolean isEntityClsOn() {
		return entityClsOn;
	}
	
	/**
	 * 是否需要生成实体类，false表示跳过
	 * @Title: setFormClsOn
	 * @param formClsOn
	 */
	public void setFormClsOn(boolean formClsOn) {
		this.formClsOn = formClsOn;
	}
	
	public boolean isFormClsOn() {
		return formClsOn;
	}

	/**
	 * 是否需要生成实体类，false表示跳过
	 * @Title: setEntityClsOn
	 * @param entityClsOn
	 */
	public void setEntityClsOn(boolean entityClsOn) {
		this.entityClsOn = entityClsOn;
	}

	public boolean isMapperClsOn() {
		return mapperClsOn;
	}

	/**
	 * 是否需要生成mapper类，false表示跳过
	 * @Title: setMapperClsOn
	 * @param mapperClsOn
	 */
	public void setMapperClsOn(boolean mapperClsOn) {
		this.mapperClsOn = mapperClsOn;
	}

	public boolean isMapperFileOn() {
		return mapperFileOn;
	}

	/**
	 * 是否需要生成mapper.xml文件，false表示跳过
	 * @Title: setMapperFileOn
	 * @param mapperFileOn
	 */
	public void setMapperFileOn(boolean mapperFileOn) {
		this.mapperFileOn = mapperFileOn;
	}

	public boolean isServiceClsOn() {
		return serviceClsOn;
	}

	/**
	 * 是否需要生成service类，false表示跳过
	 * @Title: setServiceClsOn
	 * @param serviceClsOn
	 */
	public void setServiceClsOn(boolean serviceClsOn) {
		this.serviceClsOn = serviceClsOn;
	}

	public boolean isImplServiceClsOn() {
		return implServiceClsOn;
	}

	/**
	 * 是否需要生成service实现类，false表示跳过
	 * @Title: setImplServiceClsOn
	 * @param implServiceClsOn
	 */
	public void setImplServiceClsOn(boolean implServiceClsOn) {
		this.implServiceClsOn = implServiceClsOn;
	}

	public boolean isCtrlClsOn() {
		return ctrlClsOn;
	}

	/**
	 * 是否需要生成controller类，false表示跳过
	 * @Title: setCtrlClsOn
	 * @param ctrlClsOn
	 */
	public void setCtrlClsOn(boolean ctrlClsOn) {
		this.ctrlClsOn = ctrlClsOn;
	}
}