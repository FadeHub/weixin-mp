package com.cooperate.wxh5.wx.entity.tags;

import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName: TemplateMsg 
* @Description: TODO
* @author zhrt
* @date 2017年6月26日 上午10:16:45
* @Version 1.0
 */
public class TemplateMsg {
 
	private String touser;
	
	private String template_id;
	
	private String url;
	
	private String topcolor;
	
	private Map<String, Object> data = new HashMap<>();

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
}
