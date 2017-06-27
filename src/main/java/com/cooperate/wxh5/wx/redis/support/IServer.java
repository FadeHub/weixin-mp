package com.cooperate.wxh5.wx.redis.support;

import java.util.Date;

/**
 * 服务器信息接口
 * 
 * @author sunburst
 *
 */
public interface IServer {
	
	/**
	 * 获取服务器时间
	 * 
	 * @return
	 */
	Date getServerTime();
	
}
