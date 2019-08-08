package com.lambda.core.model;

import com.lambda.core.base.BaseModel;

/**
 * 用户表
 * @author WKX
 */
public class Member extends BaseModel{

	private static final long serialVersionUID = 0L;

	private String userName;

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}
}