/*
 * Automatically generated
 */

package com.cooperate.wxh5.wx.mapper;

import com.cooperate.wxh5.wx.annotation.MybatisMapper;
import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.exception.CommonRuntimeException;

@MybatisMapper
public interface TokenMapper{
	
	public Integer insert(Token entity) throws CommonRuntimeException;
	
	public Token getToken() throws CommonRuntimeException;

}
