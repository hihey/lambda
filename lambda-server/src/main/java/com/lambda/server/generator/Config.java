package com.lambda.server.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.WordUtils;
import org.springframework.util.StringUtils;

import com.lambda.server.generator.CodeGenerator.enmClassType;

public class Config {

	// 对外属性，需要设置
	private String dbFile; // 数据库配置文件
	private String projOutputPath = "target/classes"; // 输出路径
	private String projSrcPath; // 相对输出路径的根路径的源文件路径，如 src\main\java
	private String projSrcPath2; // 相对输出路径的根路径的源文件路径，如 src\main\resources
	private String basePackage; // 基础Package地址
	private String baseObjName; // 基础对象名称，将作为所有类文件名字的一部分
	private String shortObjName; // 对象名缩写，用于方法名
	private String tableName; // 表名
	private String keyFieldName; // 主键字段名
	private String desc; // 描述
	private String viewSql; // 视图SQL，即查询结果需要的结构的SQL
	private boolean areaFilter; // 是否要增加area过滤条件
	private String orderBy = ""; // 排序
	private boolean overwriteFile = true;		//是否覆盖文件，默认覆盖 
	private String nameField = "";	//名称字段，用于判断重复
	private String author = "";
	
	private boolean userWhere = false;

	
	public boolean isUserWhere() {
		return userWhere;
	}

	public void setUserWhere(boolean userWhere) {
		this.userWhere = userWhere;
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	private String entityFolder = "model";
	private String formFolder = "form";
	private String mapperFolder = "mapper";
	private String mapperXmlFolder = "xml";
	private String serviceFolder = "service";
	private String ctrlFolder = "controller";
	private String implServiceSubFolder = "impl"; // 是service folder的子文件夹
	

	public String getOrderBy() {
		return orderBy == null ? "" : orderBy.trim();
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAreaFilter() {
		return areaFilter;
	}

	public void setAreaFilter(boolean areaFilter) {
		this.areaFilter = areaFilter;
	}

	private String baseFilePath; // 内部使用

	private String entityClsName; // 实体类名称
	private String formClsName; // 实体类(表单)名称
	private String mapperClsName; // mapper类名称
	private String mapperXmlName; // mapperXML名称
	private String serviceClsName; // service类名称
	private String implServiceClsName; // service实现类名称
	private String ctrlClsName; // controller类名称

	private String basePackageFolder;

	public String getBasePackageFolder() {
		return basePackageFolder;
	}

	/**
	 * 
	 * 
	 * @param dbFile
	 *            数据库配置文件
	 * @param projOutputPath
	 *            项目输出路径，以/结尾，如 target/classes/
	 * @param projSrcPath
	 *            源代码路径，以以/结尾，如 src/main/java/
	 * @param basePackage
	 *            基础Package地址
	 * @param baseObjName
	 *            基础对象名称，将作为所有类文件名字的一部分
	 * @param shortObjName
	 *            对象名缩写，用于方法名
	 * @param tableName
	 *            表名
	 * @param keyFieldName
	 *            主键字段名
	 * @param viewSql
	 *            视图SQL，即查询结果需要的结构的SQL
	 * @param desc
	 *            描述
	 */
	public Config(String dbFile, String projOutputPath, String author,String projSrcPath,String projSrcPath2,
			String basePackage, String baseObjName, String shortObjName,
			String tableName, String keyFieldName, String viewSql, String desc) {
		super();
		setDbFile(dbFile);
		setBasePackage(basePackage);
		setBaseObjName(baseObjName);
		setShortObjName(shortObjName);
		setTableName(tableName);
		setKeyFieldName(keyFieldName);

		setProjOutputPath(projOutputPath);
		setProjSrcPath(projSrcPath);
		setProjSrcPath2(projSrcPath2);
		setViewSql(viewSql);
		setDesc(desc);
		
		this.author = author;

	}

	public String getBaseObjName() {
		return baseObjName;
	}

	/**
	 * 基础对象名称，将作为所有类名字的一部分<br>
	 * 比如此属性设置为HplGoodsCLass，则Mapper文件名字将为HplGoodsCLassMapper.java<br>
	 * 设置这个值的时候，同时会变更生成的类名，因此如果需要不同的类名，在设置此值之后需要单独设置
	 */
	public void setBaseObjName(String baseObjName) {
		this.baseObjName = baseObjName;
		this.entityClsName = WordUtils.capitalize(baseObjName);
		this.formClsName = this.entityClsName + "Form";
		this.mapperClsName = this.entityClsName + "Mapper";
		this.mapperXmlName = low(this.entityClsName) + "-mapper";
		this.serviceClsName = this.entityClsName + "Service";
		this.implServiceClsName = this.serviceClsName + "Impl";
		this.ctrlClsName = this.entityClsName + "Controller";
	}

	public String getShortObjName() {
		return shortObjName;
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
	 * 对象名缩写，用于方法名，如GoodsClass，生成的类对应的方法名为addGoodsClass等
	 */
	public void setShortObjName(String shortObjName) {
		this.shortObjName = shortObjName;
	}

	public String getKeyFieldName() {
		return keyFieldName;
	}

	/**
	 * 设置主键字段名字
	 */
	public void setKeyFieldName(String keyFieldName) {
		this.keyFieldName = keyFieldName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDbFile() {
		return dbFile;
	}

	/**
	 * 设置数据库连接配置文件, 如jdbc.properties文件
	 */
	public void setDbFile(String dbFile) {
		this.dbFile = dbFile;
	}

	/**
	 * 
	 * @return 返回基础package地址
	 */
	public String getBasePackage() {
		return basePackage;
	}

	/**
	 * 设置基础package地址， 
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
		this.basePackageFolder = basePackage.replace(".", "/");
	}

	/**
	 * 项目输出地址，默认是target/classes
	 */
	public void setProjOutputPath(String projOutputPath) {
		this.projOutputPath = projOutputPath;

		String s = CodeGenerator.class.getResource("/").getPath();
		this.baseFilePath = s.substring(0, s.length() - projOutputPath.length());

	}

	public String getBaseSrcFilePath() {
		return baseFilePath + getProjSrcPath();
	}
	
	public String getBaseSrcFilePath2() {
		return baseFilePath + getProjSrcPath2();
	}

	public String getCapShort() {
		return WordUtils.capitalize(getShortObjName());
	}

	public String getProjSrcPath() {
		return projSrcPath;
	}

	public void setProjSrcPath(String projSrcPath) {
		this.projSrcPath = projSrcPath;
	}
	
	public String getProjSrcPath2() {
		return projSrcPath2;
	}

	public void setProjSrcPath2(String projSrcPath2) {
		this.projSrcPath2 = projSrcPath2;
	}

	public String getProjOutputPath() {
		return projOutputPath;
	}

	public String getEntityClsName() {
		return entityClsName;
	}
	
	public String getFormClsName() {
		return formClsName;
	}

	public String getMapperClsName() {
		return mapperClsName;
	}
	
	public String getMapperXmlName() {
		return mapperXmlName;
	}

	public String getServiceClsName() {
		return serviceClsName;
	}

	public String getImplServiceClsName() {
		return implServiceClsName;
	}

	public String getCtrlClsName() {
		return ctrlClsName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getViewSql() {
		return viewSql;
	}

	public void setViewSql(String viewSql) {
		this.viewSql = viewSql;
	}

	/**
	 * @param withA 是否加前缀 "A."
	 * @return
	 */
	public String getAreaFilter(boolean withA) {
		return getAreaFilter(withA, null);
	}

	/**
	 * 
	 * @param withA 是否加前缀 "A."
	 * @return
	 */
	public String getAreaFilter(boolean withA,String c) {
		return "";
//		if(c == null) c="";
//		if (withA)
//			return " A.user_id = #{"+c+"userId}  ";
//		else
//			return " area_id = #{"+c+"userId} ";
	}

	/**
	 * Orderby
	 * @return
	 */
	public String getOrderBy(boolean withA) {
		if (!withA)
			return getOrderBy();
		String o = "";
		if (!StringUtils.isEmpty(getOrderBy())) {
			o = " ORDER BY ";
			String[] os = getOrderBy().split(",");
			for (String s : os) {
				o += s + ",";
			}
			if (o.lastIndexOf(",") > 0) {
				o = o.substring(0, o.length() - 1);
			}
		}
		return o;
	}

	private String _now = null;
	public String now() {
		if (_now == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			Date date = new Date(); // now date on application server
			sdf.setTimeZone(TimeZone.getDefault());
			_now = sdf.format(date); // format
		}
		return _now;
	}

	public String getAutoComment(String clsDesc) {
		String s = "";
		s += "/**\n";
		s += " * " + getDesc() + clsDesc + "\n";
		s += " * @author "+author+"\n";
		s += " */\n";
		return s;
	}

	public String getFullFileName(CodeGenerator.enmClassType type) {
		String fileName = "", folder = "", fileExt = "java";
		switch (type) {
		case entity:
			fileName = getEntityClsName();
			folder = entityFolder;
			break;
		case form:
			fileName = getFormClsName();
			folder = formFolder;
			break;
		case ctroller:
			fileName = getCtrlClsName();
			folder = ctrlFolder;
			break;
		case mapper:
			fileName = getMapperClsName();
			folder = mapperFolder;
			break;
		case mapperXML:
			fileName = getMapperXmlName();
			folder = mapperFolder;
			fileExt = "xml";
			break;
		case service:
			fileName = getServiceClsName();
			folder = serviceFolder;
			break;
		case serviceImpl:
			fileName = getImplServiceClsName();
			folder = serviceFolder + "/" + implServiceSubFolder;
			break;
		}
		if("xml".equals(fileExt)){
			return getFullFileName2(fileName, folder, fileExt);
		}else{
			return getFullFileName(fileName, folder, fileExt);
		}
	}

	/**
	 * 获得完整的类的名字，包括Package
	 * @Title: getFullClsName
	 * @param type
	 */
	public String getFullClsName(enmClassType type) {
		String s = getBasePackage() + ".";
		switch (type) {
		case entity:
			s += entityFolder + "." + getEntityClsName();
			break;
		case form:
			s += formFolder + "." + getFormClsName();
			break;
		case ctroller:
			s += ctrlFolder + "." + getCtrlClsName();
			break;
		case mapperXML:
			s += mapperXmlFolder + "." + getMapperXmlName();
			break;
		case mapper:
			s += mapperFolder + "." + getMapperClsName();
			break;
		case service:
			s += serviceFolder + "." + getServiceClsName();
			break;
		case serviceImpl:
			s += serviceFolder + "." + implServiceSubFolder + "." + getImplServiceClsName();
			break;
		}
		return s;
	}

	/**
	 * 获取完整的绝对文件名（含路径）
	 * @Title: getFullFileName
	 * @param fileName
	 * @param folder
	 * @param fileExt
	 */
	public String getFullFileName(String fileName, String folder, String fileExt) {
		String s = getBaseSrcFilePath() + getBasePackageFolder();
		if (!StringUtils.isEmpty(folder))
			s += "/" + folder;
		s += "/" + fileName + "." + fileExt;
		return s;
	}
	
	public String getFullFileName2(String fileName, String folder, String fileExt) {
		String s = getBaseSrcFilePath2();
		if (!StringUtils.isEmpty(folder))
			s += "/" + folder;
		s += "/" + fileName + "." + fileExt;
		return s;
	}

	//Controller中的mapping地址
	public String getMappingName(){
		String lastPack = getBasePackage().substring(getBasePackage().lastIndexOf(".")+1);
		return lastPack + "/" + getBaseObjName();
	}

	public boolean isOverwriteFile() {
		return overwriteFile;
	}

	public void setOverwriteFile(boolean overwriteFile) {
		this.overwriteFile = overwriteFile;
	}
}