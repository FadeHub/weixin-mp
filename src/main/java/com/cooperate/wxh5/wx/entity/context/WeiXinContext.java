package com.cooperate.wxh5.wx.entity.context;

/**
 * 
 * @author zhrt
 *
 */
public class WeiXinContext {
	
	private static String accessToken;
	
	public static void setAccessToken(String accessToken) {
		WeiXinContext.accessToken = accessToken;
	}
	
	public static String getAccessToken(){
		return WeiXinContext.accessToken;
	}
}

