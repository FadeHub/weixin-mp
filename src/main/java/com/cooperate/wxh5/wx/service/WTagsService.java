package com.cooperate.wxh5.wx.service;

import java.util.List;
import java.util.Map;

import com.cooperate.wxh5.wx.entity.tags.WTags;

/**
 * 
* @ClassName: WGroupService 
* @Description: TODO
* @author zhrt
* @date 2017年6月26日 下午2:07:30
* @Version 1.0
 */
public interface WTagsService {

	public void add(WTags wGroup);
	
	public List<WTags> getAllTags();
	
	public void update(WTags wTags);
	
	public void delete(WTags wTags);
	
	public List<WTags> getUserTags();
	
	public void getUserTagsToPost(Map<String, String> maps);
	
}
