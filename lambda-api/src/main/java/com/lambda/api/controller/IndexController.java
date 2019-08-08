package com.lambda.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lambda.core.base.BaseController;
import com.lambda.core.base.CommonResponse;


/**
 * 接口端（提供数据）
 * @author WKX
 */
@RestController
public class IndexController extends BaseController{
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
    /**
     * 服务降级（暂时提示无服务）
     * @author WKX
     */
    @RequestMapping("/fallback")
    public CommonResponse<?> fallback(){
        return this.FAILED("网络繁忙，请稍后再试！");
    }
    
    /**
     * 获取数据（测试：获取服务）
     * @author WKX
     */
    @GetMapping("/test")
    public CommonResponse<?> test(){
    	String services = "[haha]Services: " + discoveryClient.getServices();
    	System.out.println(services);
    	return this.SUCCESS("操作成功",services);
    }
}