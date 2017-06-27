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

}
