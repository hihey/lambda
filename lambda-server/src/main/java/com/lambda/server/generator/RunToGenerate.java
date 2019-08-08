package com.lambda.server.generator;

/**
 * 单独运行，来生成文件 All  
 */
public class RunToGenerate {

	public static void main(String[] args) {
		try {

			//不需要数据，只需要结构，可以Join其他的表
			String viewSql = null;
			Config config = new Config("application-dev.properties",//jdbc
					"target/classes/", 		//输出路径，以斜杠结束
					"WKX",//作者
					"src/main/java/", 		//源代码路径，以斜杠结束
					"src/main/resources/", 	//源代码路径，以斜杠结束（保存XML文件位置）
					"com.lambda.server", 	//代码文件的package
					"Member", 			//文件名称的基本部分
					"",				//方法名称的基本部分，
					"member", 		//表名
					"id", 			//表的主键，如果是联合主键，设置和表的业务内容最相关的主键，注意：区分大小写，请和表中一致
					viewSql, 				//视图SQL，即查询结果需要的结构的SQL
					"用户表" 				//描述
			);

			config.setOverwriteFile(false);//存在是否覆盖
			CodeGenerator generator = new CodeGenerator(config);

			//以下是是否生成相应文件的开关，如果设置false，将不生成对应文件
			//特别注意：如果开启，将会覆盖已经存在的文件。
			generator.setEntityClsOn(true);
			generator.setFormClsOn(true);

			generator.setMapperClsOn(true);
			generator.setMapperFileOn(true);

			generator.setServiceClsOn(true);
			generator.setImplServiceClsOn(true);
			
			generator.setCtrlClsOn(true);
			generator.run();

			System.out.println("准备好了吗，要记得刷新哦!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}