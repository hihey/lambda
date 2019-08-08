package com.lambda.server.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lambda.core.base.MyException;
import com.lambda.core.model.Member;
import com.lambda.server.form.MemberForm;
import com.lambda.server.mapper.MemberMapper;
import com.lambda.server.redis.DistributedLocker;
import com.lambda.server.service.MemberService;

/**
 * 用户表Service逻辑实现类
 * @author WKX
 */
@Service
public class MemberServiceImpl implements MemberService{

	@Resource
	private MemberMapper memberMapper;
	
	@Autowired
    private DistributedLocker distributedLocker;
	
	@Override
	public void test(Long id) throws Exception{
		String key = "test" + id;//当前方法名加上操作标识（用户主键）
		try {
			distributedLocker.lock(key, TimeUnit.SECONDS, 5000);
			Thread.sleep(5000);
		}finally{
			distributedLocker.unlock(key);
		}
	}

	@Override
	public Member getById(Long id) throws Exception{
		if(id==null) throw new MyException();
		return memberMapper.getById(id);
	}

	@Override
	public Page<Member> getByPage(Integer pageIndex,Integer pageSize,MemberForm form){
		pageIndex = pageIndex == null ? 1 : pageIndex;
		pageSize = pageSize == null ? 10 : pageSize;
		PageHelper.startPage(pageIndex, pageSize);
		return memberMapper.getByPage(form);
	}

	@Override
	@Transactional
	public void saveModel(Member member) throws Exception{
		if(member==null) throw new MyException();
		memberMapper.saveModel(member);
	}

	@Override
	@Transactional
	public void updateModel(Member member) throws MyException{
		if(member==null || member.getId()==null) throw new MyException();
		memberMapper.updateModel(member);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws MyException{
		if(id==null) throw new MyException();
		memberMapper.deleteById(id);
	}
}