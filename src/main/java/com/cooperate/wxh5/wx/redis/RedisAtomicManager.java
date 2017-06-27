package com.cooperate.wxh5.wx.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.redis.support.IAtomic;

import redis.clients.jedis.JedisCommands;

/**
 * redis Atomic 操作管理器
 * 
 * @author sunburst
 *
 */
@Component("redisAtomicManager")
public class RedisAtomicManager extends RedisManager implements IAtomic<String> {

    @Override
    public void set(String key, long value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            jedis.set(key, String.valueOf(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public void set(String key, long value, long timeout, TimeUnit unit) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            jedis.setex(key, (int) unit.toSeconds(timeout), String.valueOf(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long incrementAndGet(String key, long delta) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return jedis.incrBy(key, delta);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long decrementAndGet(String key, long delta) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return jedis.incrBy(key, -delta);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long getLong(String key) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            if (isExist(key))
                return jedis.incrBy(key, 0);
            else
                return null;
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }
}
