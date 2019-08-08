package com.lambda.server.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ResourceManager {
	private static String JDBC_DRIVER;

	private static String JDBC_URL;

	private static String JDBC_USER;

	private static String JDBC_PASSWORD;

	private static String configFile = "application.properties";

	private static Properties props;

	private static Driver driver = null;
	
	/**
	 * 依据dbFile得到数据库连接
	 * @param dbFile
	 * @throws SQLException
	 */
	public static Connection getConnection(String dbFile) throws SQLException{
		loadProperties(dbFile);
		return getConnection();
	}

	private static synchronized Connection getConnection() throws SQLException {
		loadProperties();
		if (driver == null) {
			try {
				Class<?> jdbcDriverClass = Class.forName(JDBC_DRIVER);
				driver = (Driver) jdbcDriverClass.newInstance();
				DriverManager.registerDriver(driver);
			} catch (Exception e) {
				System.out.println("Failed to initialise JDBC driver");
				e.printStackTrace();
			}
		}
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	public static void close(Connection conn) {
		try {
			if (conn != null)conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void close(Statement stmt) {
		try {
			if (stmt != null)stmt.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null)rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public static void closeQuietly(Connection conn,Statement st,ResultSet rs){
		close(rs);
		close(st);
		close(conn);
	}
	
	/**
	 * 依据dbFile得到属性，同时之后的数据库连接将会随之改变
	 * @param dbFile
	 */
	public static void loadProperties(String dbFile) {
		if(!configFile.equals(dbFile)){				
			configFile = dbFile;
			props = null;
			
		}
		loadProperties();
	}
	private static void loadProperties() {
		if (props == null) {
			props = new Properties();
			try {
				InputStream in = ResourceManager.class.getClassLoader().getResourceAsStream(configFile);
				props.load(in);
			} catch (FileNotFoundException e) {
				System.err.println("配置文件" + configFile + "不存在!");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("读取配置文件时发生错误!");
				e.printStackTrace();
			}
			JDBC_DRIVER = props.getProperty("spring.datasource.driver-class-name");
			JDBC_URL = props.getProperty("spring.datasource.url");
			JDBC_USER = props.getProperty("spring.datasource.username");
			JDBC_PASSWORD = props.getProperty("spring.datasource.password");
		}
	}
	
	/**
	 * 得到属性信息
	 * @param property
	 * @return
	 */
	public static String getProperty(String property){
		if(props == null) loadProperties();
		return props.getProperty(property);
	}
}
