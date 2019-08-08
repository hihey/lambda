package com.lambda.server.form;


import com.lambda.core.model.Member;

/**
 * 用户表（查询表单）
 * @author WKX
 */
public class MemberForm extends Member{

	private static final long serialVersionUID = 0L;

	private Member member;

	public Member getMember(){
		return member;
	}

	public void setMember(Member member){
		this.member = member;
	}
}