package com.cooperate.wxh5.wx.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.entity.accesstoken.AccessToken;
import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.entity.context.WeiXinContext;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

@Component
public class RefreshAccessTokenTask {
	public static final String at = "k4qYgHUsWpwsk9uBX_QGUea70ATC-Swya5ssW43IXdJgi7-A2YCZX1FXIOeBknpGiOiy7FAM2d61Dc8PxT-jbh1qYH2lE4GruajNknvv_UIXBLeACAMQA";
	
	static{
		WeiXinContext.setAccessToken(at);
	}
	
	Logger log = LoggerFactory.getLogger(RefreshAccessTokenTask.class);
	
	@Autowired
	private WeixinHttpClient weixinHttpClient;
	
	@Value("#{weixin[authorize_url]}")
	private String authorize;
	
	@Value("#{weixin[weixin_appid]}")
	private String appid;
	
	@Value("#{weixin[weixin_appsecret]}")
	private String appsecret;
	
	public void refreshAccessTokenTask() {
		WeiXinContext.setAccessToken(at);
		/*Map<String,String> map = new HashMap<>();
		map.put("grant_type", "client_credential");
		map.put("appid", appid);
		map.put("secret", appsecret);
		JSONObject ret = weixinHttpClient.get(authorize, map);
		if(ret!=null){
			log.info("refreshToken accessToken ",ret.getString("access_token"));
			WeiXinContext.setAccessToken(ret.getString("access_token"));
		}else{
			refreshAccessTokenTask();
		}*/
	}
}
