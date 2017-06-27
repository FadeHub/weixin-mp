package com.cooperate.wxh5.wx.redis.support;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Pool;

/**
 * Jedis 连接池工厂
 * 
 * @author sunburst
 *
 */
public class CommonJedisPoolFactory implements DisposableBean, FactoryBean<Pool<Jedis>> {

    private Pool<Jedis> jedisPool;

    private String servers;

    private JedisPoolConfig jedisPoolConfig;

    private Integer timeout = 2000;


    @Override
    public void destroy() throws Exception {
        jedisPool.destroy();
    }

    @Override
    public Pool<Jedis> getObject() throws Exception {
        if (null == jedisPool) {
            this.jedisPool = this.create();
        }
        return jedisPool;
    }

    @Override
    public Class<?> getObjectType() {
        return ShardedJedisPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Pool<Jedis> create() {
        Pool jedisPool = null;
        if (StringUtils.isNotBlank(this.servers)) {
            String[] redisList = StringUtils.split(this.servers, ";");
            String hostAndPort = redisList[0];
            String[] hostAndPortArray = StringUtils.split(hostAndPort, ":");
            
            String host = StringUtils.trim(hostAndPortArray[0]);
            int port = Integer.valueOf(hostAndPortArray[1]);
            
            jedisPool = new JedisPool(this.jedisPoolConfig, host, port, timeout);
        }
        return jedisPool;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}
