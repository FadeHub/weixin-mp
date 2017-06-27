package com.cooperate.wxh5.wx.redis.support;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import com.cooperate.wxh5.wx.redis.RedisException;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 访问redis集群模式对象构建
 * 
 * @author sunburst
 */
public class CommonClusterJedisPoolFactory implements DisposableBean, FactoryBean<JedisCluster> {

    private JedisCluster jedisCluster;

    private String servers;

    private boolean clustered = false;

    @Override
    public void destroy() throws Exception {
        if (null != jedisCluster) {
            jedisCluster.close();
        }
    }

    @Override
    public JedisCluster getObject() throws Exception {
        if (null == jedisCluster) {
            this.jedisCluster = this.create();
        }
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return ShardedJedisPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private JedisCluster create() {
        JedisCluster jedisCluster =  null;
        if (StringUtils.isNotBlank(this.servers)) {
            String[] redisList = StringUtils.split(this.servers, ";");
            if (redisList.length > 0 && clustered) {
                Set<HostAndPort> nodes = new HashSet<HostAndPort>();

                for (int i = 0, len=redisList.length; i < len; i++) {
                    String host = StringUtils.trim(StringUtils.split(redisList[i], ":")[0]);
                    Integer port = Integer.valueOf(StringUtils.trim(StringUtils.split(redisList[i], ":")[1]));
                    HostAndPort hostAndPort = new HostAndPort(host, port);
                    nodes.add(hostAndPort);
                }
                
                jedisCluster = new JedisCluster(nodes);
                        
            } else {
                throw new RedisException(-1, "CONFIG ERROR : "+ToStringBuilder.reflectionToString(this));
            }
        }
        return jedisCluster;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setClustered(boolean clustered) {
        this.clustered = clustered;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

}
