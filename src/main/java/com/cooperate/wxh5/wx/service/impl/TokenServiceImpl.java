package com.cooperate.wxh5.wx.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.exception.CommonRuntimeException;
import com.cooperate.wxh5.wx.mapper.TokenMapper;
import com.cooperate.wxh5.wx.service.TokenService;

@Service("tokenService")
public class TokenServiceImpl implements TokenService{

	@Autowired
	private TokenMapper tokenMapper;
	
	@Override
	@Transactional
	public Integer insert(Token token) throws CommonRuntimeException{
		
		Integer cnt = tokenMapper.insert(token);
		if(cnt == 0) {
			throw new ServiceException("data is not saved or modified");
		}
		return cnt;
	}

	@SuppressWarnings("unused")
	@Override
	public Token getToken() throws CommonRuntimeException {
		Token token = null;
		if(null!=token){
			 token =tokenMapper.getToken();
		}
		return token;
	}

}
