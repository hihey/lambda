package com.lambda.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.lambda.core.base.BaseController;
import com.lambda.core.base.CommonResponse;
import com.lambda.core.model.Member;


/**
 * 接口端（提供数据）
 * @author WKX
 */
@RestController
@RequestMapping(value = "/member")
public class MemberController extends BaseController{
	
	@Autowired
	RestTemplate restTemplate;
	
	@PostMapping("/get")
	public <T> CommonResponse<Member> get(Long id,HttpServletRequest req){
		try {
			MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
			params.set("id",id);//查看的用户主键
			
			@SuppressWarnings("unchecked")
			CommonResponse<Member> res = restTemplate.postForObject("http://lambda-server/member/get",params, CommonResponse.class);
			System.err.println(res.getBody());
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return FAILED(e);
		}
	}
}