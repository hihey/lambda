package com.lambda.core.base;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;


/**
 * 数据请求返回值
 * @author WKX
 * @异常代码：{
 * 	E00:Exception
 * 	E01:ParseException
 * }
 */
public class BaseController {
	
	protected final String SUCCESS = "success";
	protected final String FAILED = "failed";
	protected final String SYSTEM = "system";
	
	protected final String SUCCESS_MSG = "操作成功！";
	protected final String FAILED_MSG = "操作失败！请联系客服";
	
	//***************SERVER端返回数据封装***************************************************************************************************
	
	/**
	 * 操作成功
	 * @author WKX
	 * @param msg 提示内容
	 */
	public <T> CommonResponse<T> SUCCESS(String msg){
		return this.SUCCESS(msg,null);
	}
	
	/**
	 * 操作成功
	 * @author WKX
	 * @param msg 提示内容
	 * @param t 数据
	 */
	public <T> CommonResponse<T> SUCCESS(String msg,T t){
		CommonResponse<T> res = new CommonResponse<T>();
		res.setStatus(SUCCESS);
		res.setMsg(StringUtils.isNotBlank(msg)?msg:SUCCESS_MSG);
		res.setBody(t);
		return res;
	}
	
	/**
	 * 操作失败
	 * @author WKX
	 * @param msg 提示内容
	 */
	public <T> CommonResponse<T> FAILED(String msg){
		return this.FAILED(msg, null);
	}
	
	/**
	 * 操作失败
	 * @author WKX
	 * @param msg 提示内容
	 * @param t 数据
	 */
	public <T> CommonResponse<T> FAILED(String msg,T t){
		CommonResponse<T> res = new CommonResponse<T>();
		res.setStatus(FAILED);
		res.setMsg(StringUtils.isNotBlank(msg)?msg:FAILED_MSG);
		res.setBody(t);
		return res;
	}
	
	/**
	 * 操作失败（根据异常处理）
	 * @author WKX
	 * @param e 异常类（自定义的异常则返回指定提示内容）
	 */
	public <T> CommonResponse<T> FAILED(Exception e){
		return FAILED(e,null);
	}
	
	/**
	 * 操作失败（倾向自定义错误信息，如果没有则使用传递的提示）
	 * @author WKX
	 * @param e 异常类（自定义的异常则返回指定提示内容）
	 */
	public <T> CommonResponse<T> FAILED(Exception e,String msg){
		if(e instanceof MyException && StringUtils.isNotBlank(e.getMessage())){
			msg = e.getMessage();
		}else if(e instanceof ParseException){
			msg = "数据异常，请联系客服！（E01）";
		}
		if(StringUtils.isBlank(msg)){
			msg = FAILED_MSG;
		}
		return this.FAILED(msg, null);
	}
}