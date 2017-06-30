package com.cooperate.wxh5.wx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooperate.wxh5.wx.entity.context.WeiXinContext;
import com.cooperate.wxh5.wx.entity.user.WeiXinUserInfoBatch;
import com.cooperate.wxh5.wx.entity.user.WeiXinUserList;
import com.cooperate.wxh5.wx.service.WeiXinTokenService;
import com.cooperate.wxh5.wx.service.WeiXinUserInfoService;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

@Service
public class WeiXinUserInfoServiceImpl implements WeiXinUserInfoService{

	@Value("#{weixin[user_info_query_url]}")
	private String userInfoUrl;
	
	@Value("#{weixin[user_info_batch_url]}")
	private String userInfoBatchUrl;
	
	@Value("#{weixin[user_info_list_url]}")
	private String userInfoListUrl;
	
	@Value("#{weixin[user_black_list_url]}")
	private String blackUserListUrl;
	
	@Value("#{weixin[get_current_autoreply_info]}")
	private String getCurrentAutoreplyInfoUrl;
	
	@Value("#{weixin[userinfo_url]}")
	private String userinfoUrl;
	
	@Autowired
	private WeixinHttpClient weixinHttpClient;
	
	@Override
	public JSONObject getUserInfo(String openId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("access_token", WeiXinContext.getAccessToken());
		map.put("openid", openId);
		map.put("lang", "zh_CN");
		JSONObject jsonObject = weixinHttpClient.get(userInfoUrl,map);
		if(null!=jsonObject){
			return jsonObject;
		}
		return null;
	}

	@Override
	public List<Object> getBatchUserInfo(List<WeiXinUserInfoBatch> list) {
		String url = userInfoBatchUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		Map<String, List<WeiXinUserInfoBatch>>  map = new HashMap<>();
		map.put("user_list", list);
		JSONObject fromObject = JSONObject.fromObject(map);
		JSONObject ret = weixinHttpClient.post(url,fromObject.toString());
		if(ret!=null) {
			System.out.println(ret);
		}
		return null;
	}

	@Override
	public List<Object> getUserInfoList(String nextOpenId) {
		Map<String,String> maps = new HashMap<>();
		maps.put("ACCESS_TOKEN",WeiXinContext.getAccessToken());
		maps.put("NEXT_OPENID", nextOpenId);
		JSONObject ret = weixinHttpClient.get(userInfoListUrl, maps);
		if(ret!=null){
			System.out.println(ret);
		}
		
		return null;
	}

	@Override
	public List<Object> getBlackList(String benginOpenId) {
		String url = blackUserListUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		Map<String, String> maps = new HashMap<>();
		maps.put("begin_openid", benginOpenId);
		JSONObject fromObject = JSONObject.fromObject(maps);
		JSONObject ret = weixinHttpClient.post(url,fromObject.toString());
		if(ret!=null) {
			System.out.println(ret.toString());
		}
		return null;
	}

	@Override
	public List<Object> getCurrentAutoreplyInfo() {
		Map<String, String> maps = new HashMap<>();
		maps.put("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		JSONObject ret = weixinHttpClient.get(getCurrentAutoreplyInfoUrl, maps);
		if(ret!=null){
			System.out.println(ret);
		}
		return null;
	}

	@Override
	public JSONObject getUserInfo(String openid,String accessToken) {
		//?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN 
		Map<String, String> maps = new HashMap<>();
		maps.put("access_token", accessToken);
		maps.put("openid", openid);
		maps.put("lang", "zh_CN");
		JSONObject ret = weixinHttpClient.get(userinfoUrl, maps);
		if(ret!=null){
			return ret;
		}
		return null;
	}

}
