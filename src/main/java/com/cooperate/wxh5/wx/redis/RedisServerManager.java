package com.cooperate.wxh5.wx.redis;

import java.util.Date;
import java.util.List;

import com.cooperate.wxh5.wx.redis.support.IServer;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class RedisServerManager implements IServer {

	protected Pool<Jedis> jedisPool;
	
	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	
	@Override
	public Date getServerTime() {
		Jedis jedis = (Jedis) jedisPool.getResource();
		List<String> time = jedis.time();
		String unixTimeStr = time.get(0);
		String microsecondStr = time.get(1);
		
		long unixTime = Long.parseLong(unixTimeStr) * 1000 
					  + Long.parseLong(microsecondStr) / 100;
		
		Date nowTime = new Date(unixTime);
		
		return nowTime;
	}

}
