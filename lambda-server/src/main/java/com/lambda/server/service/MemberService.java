package com.lambda.server.service;

import com.github.pagehelper.Page;
import com.lambda.core.model.Member;
import com.lambda.server.form.MemberForm;

/**
 * 用户表Service
 * @author WKX
 */
public interface MemberService{
	
	/**
	 * 测试分布式锁（不同ID可以同时执行，相同ID则阻塞执行）
	 * @param id 用户主键（作为锁标识）
	 * @throws Exception
	 */
	public void test(Long id) throws Exception;

	public Member getById(Long id) throws Exception;

	public Page<Member> getByPage(Integer pageIndex,Integer pageSize,MemberForm form);

	public void saveModel(Member member) throws Exception;

	public void updateModel(Member member) throws Exception;

	public void deleteById(Long id) throws Exception;
}