package com.cooperate.wxh5.wx.service;

import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.exception.CommonRuntimeException;

public interface TokenService {

	public Integer insert(Token token) throws CommonRuntimeException;
	
	public Token getToken() throws CommonRuntimeException;
}
