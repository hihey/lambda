package com.lambda.server.generator;

import org.apache.commons.lang.WordUtils;
import org.springframework.util.StringUtils;

public class Field {
	private String colName;
	private String fieldName;
	private String type;
	private String jdbcType;
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	
	/**
	 * 
	* @author: alan
	* @date: 2016年5月17日
	* @Title: getBatisValue  
	* @param container
	* @return
	 */
	public String getBatisValue(String container){
		String p = "";
		if(!StringUtils.isEmpty(container)) p = container + ".";
		return "#{" + p + getFieldName() + ",jdbcType=" + getJdbcType()+"}";
	}
	
	public String getBatisValue(){
		return getBatisValue(null);
	}
	public boolean isAreaField(){
		return getColName().equalsIgnoreCase("area_id") || getColName().equalsIgnoreCase("sys_id");
	}
	
	/**
	 * 不能更新的字段
	* @author: alan
	* @date: 2016年5月17日
	* @Title: excludeUpdate  
	* @return
	 */
	public boolean excludeUpdate(){
		if(getColName().indexOf("_guid")>0) return true;
		else if(getColName().equalsIgnoreCase("add_by") || getColName().equalsIgnoreCase("add_name")
				||getColName().equalsIgnoreCase("add_time") ) return true;
		else return isAreaField();
	}
	
	/**
	 * 判断是否同一个字段
	* @author: alan
	* @date: 2016年5月17日
	* @Title: equal  
	* @param f
	* @return
	 */
	public boolean equal(Field f){
		return getFieldName().equals(f.getFieldName());
	}
	
	//getter方法名
	public String getter(){
		return "get" + WordUtils.capitalize(getFieldName()) + "()";
	}
	
	//setter方法名
		public String setter(String s){
			return "set" + WordUtils.capitalize(getFieldName()) + "("+s+")";
		}
}
