package com.cooperate.wxh5.wx.redis;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.cooperate.wxh5.wx.redis.serializer.CommonRedisJavaSerializer;
import com.cooperate.wxh5.wx.redis.serializer.IRedisSerializer;
import com.cooperate.wxh5.wx.redis.support.ICommon;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;
import redis.clients.util.Pool;

public abstract class RedisManager implements ICommon<String> {

    protected final Charset charset = Charset.forName("UTF8");

    protected Pool<JedisCommands> jedisPool;

    protected Pool<JedisCommands> jedisPoolRead;

    protected IRedisSerializer<Object> redisSerializer = new CommonRedisJavaSerializer();

    public Pool<JedisCommands> getPool() {
        return this.jedisPool;
    }

    public Pool<JedisCommands> getReadPool() {
        if (null == this.jedisPoolRead)
            return this.jedisPool;
        else
            return this.jedisPoolRead;
    }

    @Override
    public void delete(String key) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            jedis.del(key);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        JedisCommands jedis = this.getPool().getResource();
        if (jedis instanceof Jedis) {
            try {
                if (CollectionUtils.isEmpty(keys)) {
                    return;
                }
                final byte[][] rawKeys = new byte[keys.size()][];
                int i = 0;
                for (String key : keys) {
                    rawKeys[i++] = key.getBytes(charset);
                }
                ((Jedis) jedis).del(rawKeys);
            } catch (Exception e) {
                throw new RedisException(-1, e.getMessage(), e);
            } finally {
                this.getPool().returnResource(jedis);
            }
        } else if (jedis instanceof ShardedJedis) {
            for (String key : keys) {
                delete(key);
            }
        }
    }

    @Override
    public boolean isExist(String key) {
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        JedisCommands jedis = this.getReadPool().getResource();
        if (jedis instanceof Jedis) {
            try {
                return ((Jedis) jedis).keys(pattern);
            } catch (Exception e) {
                throw new RedisException(-1, e.getMessage(), e);
            } finally {
                this.getReadPool().returnResource(jedis);
            }
        } else {
            throw new RedisException(-1, "集群模式不支持[KEYS]操作");
        }
    }

    protected byte[][] toByteArrays(Map<byte[], byte[]> source) {
        byte[][] result = new byte[source.size() * 2][];
        int index = 0;
        for (Map.Entry<byte[], byte[]> entry : source.entrySet()) {
            result[index++] = entry.getKey();
            result[index++] = entry.getValue();
        }
        return result;
    }

    public void setJedisPool(Pool<JedisCommands> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setJedisPoolRead(Pool<JedisCommands> jedisPoolRead) {
        this.jedisPoolRead = jedisPoolRead;
    }

    public void setRedisSerializer(IRedisSerializer<Object> redisSerializer) {
        this.redisSerializer = redisSerializer;
    }
}
