package com.lambda.core.base;

import java.io.Serializable;

public class CommonResponse<T> implements Serializable{

	private static final long serialVersionUID = 3861441348071979191L;
	private String status;
	private String msg;
	private T body;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "CommonResponse [status=" + status + ", msg=" + msg + ", body=" + body + "]";
	}
}