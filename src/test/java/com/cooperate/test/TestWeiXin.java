package com.cooperate.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.cooperate.wxh5.wx.entity.accesstoken.Token;
import com.cooperate.wxh5.wx.entity.context.WeiXinContext;
import com.cooperate.wxh5.wx.entity.menu.WeiXinMenu;
import com.cooperate.wxh5.wx.entity.tags.ModelMagData;
import com.cooperate.wxh5.wx.entity.tags.TemplateMsg;
import com.cooperate.wxh5.wx.entity.tags.WTags;
import com.cooperate.wxh5.wx.entity.user.WeiXinUserInfoBatch;
import com.cooperate.wxh5.wx.kit.MessageKit;
import com.cooperate.wxh5.wx.service.TokenService;
import com.cooperate.wxh5.wx.service.WTagsService;
import com.cooperate.wxh5.wx.service.WeiXinTokenService;
import com.cooperate.wxh5.wx.service.WeiXinUserInfoService;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

@ContextConfiguration(locations = {"classpath*:/spring/spring.xml","classpath*:/spring/applicationContext-dao.xml","classpath*:/spring/applicationContext-datasource.xml"})
//@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = false)
public class TestWeiXin extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private WeiXinTokenService weiXinTokenService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private WeiXinUserInfoService weiXinUserInfoService;
	
	@Value("#{weixin[user_info_query_url]}")
	private String userInfoUrl;
	
	@Value("#{weixin[menu_url]}")
	private String menuUrl;
	
	@Autowired
	private MessageKit messageKit;
	
	@Autowired
	private WeixinHttpClient weixinHttpClient;
	
	@Autowired
	private WTagsService wTagsService;
	
	@Test
	public void testAccessToken(){
	}
	
	@Test
	public void testToken(){
		Token token = new Token();
		token.setAccessToken("1");
		token.setId(UUID.randomUUID().toString().replace("-", ""));
		token.setExpiresIn("7200");
		token.setCreateTime(new Date());
		tokenService.insert(token);
	}
	
	@Test
	public void testUserInfo(){
		JSONObject userInfo = weiXinUserInfoService.getUserInfo("oshQGv90CxqWraefyuz9RWitgYG8");
		System.out.println(userInfo.toString());
	}
	
	@Test
	public void testMenu() {
		List<WeiXinMenu> wx = new ArrayList<>();
		WeiXinMenu menu3 = new WeiXinMenu();
		menu3.setName("了解连众");
		menu3.setType("view");
		menu3.setUrl("https://sit.lianzhonghuzhu.com/wxh5/bothDifference.html");
		wx.add(menu3);
		WeiXinMenu menu4 = new WeiXinMenu();
		menu4.setName("加入互助");
		menu4.setType("view");
		menu4.setUrl("https://sit.lianzhonghuzhu.com/wxh5/");
		wx.add(menu4);
		WeiXinMenu menu = new WeiXinMenu();
		menu.setId(1);
		menu.setName("我的互助");
		List<WeiXinMenu> list = new ArrayList<>();
		WeiXinMenu menu1 = new WeiXinMenu();
		menu1.setName("邀请好友");
		menu1.setType("view");
		menu1.setUrl("https://sit.lianzhonghuzhu.com/wxh5/inviteFriends.html");
		menu1.setKey("31");
		WeiXinMenu menu2 = new WeiXinMenu();
		menu2.setName("我要登录");
		menu2.setType("view");
		menu2.setKey("32");
        menu2.setUrl("https://sit.lianzhonghuzhu.com/wxh5/web_login.html");
		list.add(menu1);
		list.add(menu2);
		menu.setSub_button(list);
		wx.add(menu);
		Map<String, List<WeiXinMenu>> map = new HashMap<>();
		map.put("button", wx);
		JSONObject fromObject = JSONObject.fromObject(map);
		JSONObject post = weixinHttpClient.post(menuUrl.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken()), fromObject.toString());
		System.out.println(post);
	}
	
	@Test
	public void testTemplate(){
		TemplateMsg tm = new TemplateMsg();
		tm.setTemplate_id("r-F6uTKxeUAt7f52DPVcaJrDfFvx4c7Xpz7945cRM_4");
		tm.setTouser("oshQGv90CxqWraefyuz9RWitgYG8");
		tm.setUrl("http://www.baidu.com");
		tm.setTopcolor("#FF3030");
		Map<String,Object> map = new HashMap<>();
		map.put("num",new ModelMagData("103", "#8B4C39"));
		tm.setData(map);
		String postTemplateMsg = messageKit.postTemplateMsg(tm);
		System.out.println(postTemplateMsg);
	}
	
	@Test
	public void testTags() {
		WTags wTags = new WTags();
		wTags.setName("南阳");
		wTagsService.add(wTags);
	}
	
	@Test
	public void testGetTags() {
		List<WTags> allTags = wTagsService.getAllTags();
		System.out.println(allTags);
	}
	
	@Test
	public void testUpdateTags() {
		WTags wTags = new WTags();
		wTags.setName("广州");
		wTags.setId(100);
		wTagsService.update(wTags);
	}
	
	@Test
	public void testDeleteTags() {
		WTags wTags = new WTags();
		wTags.setId(100);
		wTagsService.delete(wTags);
	}
	
	@Test
	public void testUserTags() {
		wTagsService.getUserTags();
	}
	
	@Test
	public void testUserPostTags() {
		Map<String,String> maps = new HashMap<>();
		maps.put("tagid", "1");
		wTagsService.getUserTagsToPost(maps);
	}
	
	@Test
	public void testUserInfoBatch() {
		List<WeiXinUserInfoBatch> list = new ArrayList<>();
		WeiXinUserInfoBatch wx = new WeiXinUserInfoBatch();
		wx.setLang("zh_CN");
		wx.setOpenid("oshQGv3_lhoaeTVdgs4NKRqySMjQ");
		list.add(wx);
		WeiXinUserInfoBatch wBatch = new WeiXinUserInfoBatch();
		wBatch.setLang("zh_CN");
		wBatch.setOpenid("oshQGv6E_SLhEdVR9ju8eJiw962U");
		list.add(wBatch);
		 weiXinUserInfoService.getBatchUserInfo(list);
	}
	
	@Test
	public void testUserInfoList() {
		weiXinUserInfoService.getUserInfoList("");
	}
}
