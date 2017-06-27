package com.cooperate.wxh5.wx.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cooperate.wxh5.wx.redis.support.ICache;
import com.cooperate.wxh5.wx.service.WeiXinTokenRedisService;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

@Service
public class WeiXinTokenRedisServiceImpl implements WeiXinTokenRedisService{

	private static final Logger log = LoggerFactory.getLogger(WeiXinTokenRedisServiceImpl.class);
		
	@Autowired
	private ICache<String> redisManager;
	
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
		String string = redisManager.get(tokenKey, String.class);
		if(StringUtils.isBlank(string)){
			string = "";
		}
		return string;
	}

	@Override
	//@Scheduled(cron = "*/5 * * * * ?")
	public void refreshToken() {
		Map<String,String> map = new HashMap<>();
		map.put("grant_type", "client_credential");
		map.put("appid", appid);
		map.put("appsecret", appsecret);
		try {
			JSONObject ret = weixinHttpClient.get(authorize, map);
			if (null != ret) {
				String token = ret.getString("access_token");
				log.info("refreshToken token {}", token);
				if (StringUtils.isNotBlank(token)) {
					redisManager.set(tokenKey,token);
				}
			}
		} catch(Exception e) {
			log.error("json conversion error or refreshToken error .", e);
		}
	}

}
