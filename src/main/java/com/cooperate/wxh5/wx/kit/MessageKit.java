package com.cooperate.wxh5.wx.kit;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.entity.context.WeiXinContext;
import com.cooperate.wxh5.wx.entity.tags.TemplateMsg;
import com.cooperate.wxh5.wx.quartz.RefreshAccessTokenTask;
import com.cooperate.wxh5.wx.util.MessageUtil;
import com.cooperate.wxh5.wx.util.WeixinHttpClient;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: MessageKit 
* @Description: TODO
* @author zhrt
* @date 2017年6月26日 上午9:49:27
* @Version 1.0
 */
@Component
public class MessageKit {

	private static Map<String,String> replyMsgs = new HashMap<>();
	
	@Value("#{weixin[send_template_id]}")
	private String sendTemplateId;
	
	@Autowired
	private WeixinHttpClient weixinHttpClient;
	
	static {
		replyMsgs.put("123", "你输入了123");
		replyMsgs.put("hello","world");
		replyMsgs.put("run","祝你一路平安");
	}
	
	public static Map<String, String> reqMsg2Map(HttpServletRequest req) throws Exception {
		Map<String, String> parseXml = MessageUtil.parseXml(req);
		return parseXml;
	}
	
	public  String postTemplateMsg(TemplateMsg tm) {
		try {
			String url = sendTemplateId.replace("ACCESS_TOKEN", WeiXinContext.getAccessToken());
			JSONObject fromObject = JSONObject.fromObject(tm);
			JSONObject ret = weixinHttpClient.post(url, fromObject.toString());
			return ret.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
