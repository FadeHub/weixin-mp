package com.cooperate.wxh5.wx.service;

import java.util.List;

import com.cooperate.wxh5.wx.entity.user.WeiXinUserInfo;
import com.cooperate.wxh5.wx.entity.user.WeiXinUserInfoBatch;
import com.cooperate.wxh5.wx.entity.user.WeiXinUserList;

import net.sf.json.JSONObject;

/**
 * 获取微信用户基本信息
 * @author zhrt
 *
 */
public interface WeiXinUserInfoService {

	public JSONObject getUserInfo(String openId);
	
	public List<Object> getBatchUserInfo(List<WeiXinUserInfoBatch> list);
	
	public List<Object> getUserInfoList(String nextOpenId);
}
