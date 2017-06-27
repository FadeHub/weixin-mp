package com.cooperate.wxh5.wx.service;

/**
 * 获取token
 * @author zhrt
 *
 */
public interface WeiXinTokenRedisService {

	public String getAccessToken();
	
	public void refreshToken();
}
