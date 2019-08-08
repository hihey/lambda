package com.lambda.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lambda.core.base.BaseController;
import com.lambda.core.base.CommonResponse;
import com.lambda.server.rabbitmq.Sender;

/**
 * 主Controller类
 * @author WKX
 */
@RestController
@RequestMapping(value = "/index")
public class IndexController extends BaseController{
	
	@Autowired
    private Sender sender;

	/**
	 * 根据主键获取对象
	 * @param msg 消息内容
	 */
	@PostMapping(value="/rabbitmq")
	public CommonResponse<String> getById(String msg){
		try {
			sender.send(msg);
			return SUCCESS("发送成功！",msg);
		} catch (Exception e) {
			e.printStackTrace();
			return this.FAILED(e);
		}
	}
}