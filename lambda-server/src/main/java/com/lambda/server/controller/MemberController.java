package com.lambda.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lambda.core.base.BaseController;
import com.lambda.core.base.CommonResponse;
import com.lambda.core.model.Member;
import com.lambda.server.service.MemberService;

/**
 * 用户表Controller类
 * @author WKX
 */
@RestController
@RequestMapping(value = "/member")
public class MemberController extends BaseController{

	@Autowired
	private MemberService memberService;

	/**
	 * 根据主键获取对象
	 * @param id 主键
	 */
	@PostMapping(value="/get")
	public CommonResponse<Member> getById(Long id){
		try {
			
			return SUCCESS("操作成功！",memberService.getById(id));
		} catch (Exception e) {
			e.printStackTrace();
			return this.FAILED(e);
		}
	}
}