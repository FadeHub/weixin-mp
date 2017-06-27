package com.cooperate.wxh5.wx.redis.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import com.cooperate.wxh5.wx.redis.RedisException;

import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Pool;

public class CommonShardedJedisPoolFactory implements DisposableBean, FactoryBean<Pool<JedisCommands>> {

    private static final String HASH_HEAD = "instance:";

    private Pool<JedisCommands> jedisPool;

    private String servers;

    private JedisPoolConfig jedisPoolConfig;

    private Integer timeout = 2000;

    private boolean shareded = false;

    @Override
    public void destroy() throws Exception {
        jedisPool.destroy();
    }

    @Override
    public Pool<JedisCommands> getObject() throws Exception {
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
    private Pool<JedisCommands> create() {
        Pool shardedJedisPool = null;
        if (StringUtils.isNotBlank(this.servers)) {
            String[] redisList = StringUtils.split(this.servers, ";");
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(redisList.length);
            if (redisList.length > 1 && shareded) {
                for (int i = 0; i < redisList.length; i++) {
                    String host = StringUtils.trim(StringUtils.split(redisList[i], ":")[0]);
                    Integer port = Integer.valueOf(StringUtils.trim(StringUtils.split(redisList[i], ":")[1]));
                    String name = HASH_HEAD + i;
                    JedisShardInfo jedisInfo = new JedisShardInfo(host, port, timeout, name);
                    shards.add(jedisInfo);
                }
                shardedJedisPool = new ShardedJedisPool(this.jedisPoolConfig, shards);
            } else if (1 == redisList.length && !shareded) {
                shardedJedisPool = new JedisPool(this.jedisPoolConfig, StringUtils.trim(StringUtils.split(redisList[0], ":")[0]), Integer.valueOf(StringUtils.trim(StringUtils.split(redisList[0], ":")[1])), timeout);
            }else{
                throw new RedisException(-1, "CONFIG ERROR : "+ToStringBuilder.reflectionToString(this));
            }
        }
        return shardedJedisPool;
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

    public void setShareded(boolean shareded) {
        this.shareded = shareded;
    }

    public void setJedisPool(Pool<JedisCommands> jedisPool) {
        this.jedisPool = jedisPool;
    }
    
}
