package com.lambda.core.base;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public abstract class BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}