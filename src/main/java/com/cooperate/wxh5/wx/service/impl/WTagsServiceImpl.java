package com.cooperate.wxh5.wx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cooperate.wxh5.wx.entity.context.WeiXinContext;
import com.cooperate.wxh5.wx.entity.tags.WTags;
import com.cooperate.wxh5.wx.service.WTagsService;
import com.cooperate.wxh5.wx.util.StringJsonToListUtil;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

@Service
public class WTagsServiceImpl implements WTagsService{

	@Autowired
	private WeixinHttpClient weixinHttpClient;
	
	@Value("#{weixin[add_tags_url]}")
	private String addTagsUrl;
	
	@Value("#{weixin[get_tags_url]}")
	private String  getTagsUrl;
	
	@Value("#{weixin[update_tags_url]}")
	private String updateTagsUrl;
	
	@Value("#{weixin[delete_tags_url]}")
	private String deleteTagsUrl;
	
	@Value("#{weixin[user_tags_url]}")
	private String userTagsUrl;
	
	@Value("#{weixin[user_tags_post_url]}")
	private String userTagsPostUrl;
	
	@Override
	public void add(WTags wGroup) {
		String url = addTagsUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		Map<String,Object> map = new HashMap<>();
		map.put("tag", wGroup);
		JSONObject fromObject = JSONObject.fromObject(map);
		JSONObject ret = weixinHttpClient.post(url,fromObject.toString());
		System.out.println(ret);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WTags> getAllTags() {
		Map<String,String> maps = new HashMap<>();
		maps.put("access_token", WeiXinContext.getAccessToken());
		JSONObject jsonObject = weixinHttpClient.get(getTagsUrl, maps);
		if(jsonObject!=null){
			String string = jsonObject.getString("tags");
			/*JSONArray jsonArray = JSONArray.fromObject(string);
			Collection collection = JSONArray.toCollection(jsonArray, WTags.class);
	        List<WTags> userList = new ArrayList<WTags>();
			Iterator it = collection.iterator();
	        while (it.hasNext()) {
	        	WTags user = (WTags) it.next();
	            userList.add(user);
	        }
	        return userList;*/
			List<WTags> stringJsonToListUtil = (List<WTags>) StringJsonToListUtil.stringJsonToList(string, WTags.class);
//			List<WTags> parseArray = JSON.parseArray(string, WTags.class);
//			System.out.println(parseArray);
			System.out.println(stringJsonToListUtil);
			return stringJsonToListUtil;
		}
		return null;
	}

	@Override
	public void update(WTags wTags) {
		String url = updateTagsUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		Map<String,Object> map = new HashMap<>();
		map.put("tag", wTags);
		JSONObject fromObject = JSONObject.fromObject(map);
		JSONObject ret = weixinHttpClient.post(url,fromObject.toString());
		System.out.println(ret);
	}

	@Override
	public void delete(WTags wTags) {
		String url = deleteTagsUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		Map<String, Object> maps = new HashMap<>();
		maps.put("tag", wTags);
		JSONObject fromObject = JSONObject.fromObject(maps);
		JSONObject ret = weixinHttpClient.post(url,fromObject.toString());
		System.out.println(ret);
	}

	@Override
	public List<WTags> getUserTags() {
		Map<String,String> maps = new HashMap<>();
		maps.put("access_token", WeiXinContext.getAccessToken());
		JSONObject jsonObject = weixinHttpClient.get(getTagsUrl, maps);
		if(jsonObject!=null){
			String string = jsonObject.getString("tags");
			List<WTags> parseArray = JSON.parseArray(string, WTags.class);
			return parseArray;
		}
		return null;
	}

	@Override
	public void getUserTagsToPost(Map<String, String> maps) {
		String url = userTagsPostUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
		Map<String,Object> map = new HashMap<>();
		JSONObject fromObject = JSONObject.fromObject(map);
		JSONObject ret = weixinHttpClient.post(url,fromObject.toString());
		if(ret!=null){
			String count = ret.getString("count");
			String data = ret.getString("data");
			String nextOpenId = ret.getString("next_openid");
		}
		System.out.println(ret);
	}

}
