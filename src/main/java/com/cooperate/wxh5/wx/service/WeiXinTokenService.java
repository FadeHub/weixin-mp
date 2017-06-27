package com.cooperate.wxh5.wx.service;

import java.util.Map;

import com.cooperate.wxh5.wx.entity.accesstoken.Token;

public interface WeiXinTokenService {

	public String getAccessToken();
	
	public Token getToken();
}
