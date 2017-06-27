package com.cooperate.wxh5.wx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.service.WeiXinTokenService;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

@Service("weiXinTokenService")
public class WeiXinTokenServiceImpl implements WeiXinTokenService{

	private static final Logger log = LoggerFactory.getLogger(WeiXinTokenServiceImpl.class);
	
	@Autowired
	private WeixinHttpClient weixinHttpClient;
	
	@Value("#{weixin[authorize_url]}")
	private String authorize;
	
	@Value("#{weixin[weixin_appid]}")
	private String appid;
	
	@Value("#{weixin[weixin_appsecret]}")
	private String appsecret;
	
	@Value("#{weixin[token_key]}")
	private String tokenKey;
	
	@Override
	public String getAccessToken() {
		String token = null;
		Map<String,String> map = new HashMap<>();
		map.put("grant_type", "client_credential");
		map.put("appid", appid);
		map.put("secret", appsecret);
		JSONObject ret = weixinHttpClient.get(authorize, map);
		if(null!=ret){
			token = ret.getString("access_token");
		}
		return token;
	}
	
	public static void main(String[] args) {
	}

	
	@Override
	public Token getToken() {
		Map<String,String> map = new HashMap<>();
		map.put("grant_type", "client_credential");
		map.put("appid", appid);
		map.put("secret", appsecret);
		JSONObject ret = weixinHttpClient.get(authorize, map);
		if(null!=ret){
			Token token = new Token();
			token.setAccessToken(ret.getString("access_token"));
			token.setCreateTime(new Date());
			token.setId(UUID.randomUUID().toString().replace("-", ""));
			token.setExpiresIn(String.valueOf(ret.getInt("expires_in")));
			return token;
		}
		return null;
	}

}
