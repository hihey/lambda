package com.lambda.server.mapper;

import com.github.pagehelper.Page;
import com.lambda.core.model.Member;
import com.lambda.server.form.MemberForm;

/**
 * 用户表
 * @author WKX
 */
public interface MemberMapper{

	public Member getById(Long id);

	public Page<Member> getByPage(MemberForm form);

	public void saveModel(Member member);

	public void updateModel(Member member);

	public void updateModelSelective(Member member);

	public void deleteById(Long id);
}