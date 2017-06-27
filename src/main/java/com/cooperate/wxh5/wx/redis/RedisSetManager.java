package com.cooperate.wxh5.wx.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.redis.support.IRedisSet;
import com.google.common.primitives.Primitives;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;

/**
 * 简单的redis set操作管理器
 * 
 * @author sunburst
 *
 */
@Component("redisSetManager")
public class RedisSetManager extends RedisManager implements IRedisSet<String> {

    @Override
    public void add(String key, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            ((BinaryJedisCommands) jedis).sadd((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            this.getPool().returnResource(jedis);
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public boolean isMemberOf(String key, Object value) {
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).sismember((key.getBytes(charset)), this.redisSerializer.serialize(value));
        } catch (Exception e) {
            this.getReadPool().returnResource(jedis);
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
    }

    @Override
    public Set<Object> members(String key) {
        Set<Object> sets = null;
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            Set<byte[]> tmpSet = ((BinaryJedisCommands) jedis).smembers(key.getBytes(charset));
            if (null != tmpSet && !tmpSet.isEmpty()) {
                sets = new HashSet<Object>(tmpSet.size());
                for (byte[] s : tmpSet) {
                    sets.add(this.redisSerializer.deserialize(s));
                }
            }
        } catch (Exception e) {
            this.getReadPool().returnResource(jedis);
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
        return sets;
    }

    @Override
    public <T> T pop(String key, Class<T> classOfT) {
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            byte[] bytes = ((BinaryJedisCommands) jedis).get((key == null ? null : ((String) key).getBytes(charset)));
            if (null == bytes)
                return null;
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(bytes));
        } catch (Exception e) {
            this.getReadPool().returnResource(jedis);
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
    }
}
