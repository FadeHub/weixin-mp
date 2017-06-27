package com.cooperate.wxh5.wx.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.redis.support.ICache;
import com.google.common.primitives.Primitives;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

/**
 * redis key/value 操作管理器
 * 
 * @author sunburst
 *
 */
@Component
public class RedisKVManager extends RedisManager implements ICache<String> {

    @Override
    public void set(String key, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            ((BinaryJedisCommands) jedis).set((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        if (StringUtils.isBlank(key))
            return;
        JedisCommands jedis = this.getPool().getResource();
        try {
            if (jedis instanceof Jedis) {
                ((Jedis) jedis).setex(key.getBytes(charset), (int)unit.toSeconds(timeout), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
            } else if (jedis instanceof ShardedJedis) {
                ((BinaryJedisCommands) jedis).setex((key == null ? null : key.getBytes(charset)), (int) unit.toSeconds(timeout), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
            }
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Boolean setIfAbsent(String key, Object value) {
        if (StringUtils.isBlank(key))
            return false;
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).setnx((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value)) > 0;
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public void multiSet(Map<? extends String, Object> m) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            final Map<byte[], byte[]> rawKeys = new LinkedHashMap<byte[], byte[]>(m.size());
            for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
                rawKeys.put(entry.getKey().getBytes(charset), this.redisSerializer.serialize(entry.getValue()));
            }
            if (jedis instanceof Jedis) {
                ((Jedis) jedis).mset(this.toByteArrays(rawKeys));
            } else if (jedis instanceof ShardedJedis) {
                throw new RedisException(-1, "集群模式不支持[multiSet]操作");
            }
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Boolean multiSetIfAbsent(Map<? extends String, Object> m) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            final Map<byte[], byte[]> rawKeys = new LinkedHashMap<byte[], byte[]>(m.size());
            for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
                rawKeys.put(entry.getKey().getBytes(charset), this.redisSerializer.serialize(entry.getValue()));
            }
            if (jedis instanceof Jedis) {
                return ((Jedis) jedis).msetnx(this.toByteArrays(rawKeys)) > 0;
            } else {
                throw new RedisException(-1, "不支持[multiSetIfAbsent]操作");
            }
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T get(Object key, Class<T> classOfT) {
        if (null == key)
            return null;
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            byte[] bytes = ((BinaryJedisCommands) jedis).get((key == null ? null : ((String) key).getBytes(charset)));
            if (null == bytes)
                return null;
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(bytes));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }

    }

    @Override
    public <T> T getAndSet(String key, Object value, Class<T> classOfT) {
        if (StringUtils.isBlank(key) || null == value)
            return null;
        JedisCommands jedis = this.getPool().getResource();
        try {
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(((BinaryJedisCommands) jedis).getSet((key.getBytes(charset)), this.redisSerializer.serialize(value))));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public List<Object> multiGet(Collection<String> keys) {
        JedisCommands jedis = this.getReadPool().getResource();
        List<Object> returnList = Collections.emptyList();
        try {
            if (keys.isEmpty()) {
                return returnList;
            }
            final byte[][] rawKeys = new byte[keys.size()][];

            int counter = 0;
            for (String hashKey : keys) {
                if (StringUtils.isNotBlank(hashKey))
                    rawKeys[counter++] = hashKey.getBytes(charset);
            }
            if (jedis instanceof Jedis) {
                List<byte[]> list = ((Jedis) jedis).mget(rawKeys);
                if (null != list && !list.isEmpty()) {
                    returnList = new ArrayList<Object>(list.size());
                    for (byte[] bytes : list) {
                        if (null != bytes)
                            returnList.add(this.redisSerializer.deserialize(bytes));
                    }
                }
            } else {
                throw new RedisException(-1, "不支持[multiSetIfAbsent]操作");
            }
            return returnList;
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
    }

    @Override
    public Integer append(String key, String value) {
        if (StringUtils.isBlank(key))
            return null;
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            Long val = jedis.append(key, value);
            if (null != val) {
                return val.intValue();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }

    }

    @Override
    public String get(String key, long start, long end) {
        if (StringUtils.isBlank(key))
            return null;
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            return jedis.getrange(key, start, end);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
    }

    @Override
    public void set(String key, Object value, long offset) {
        if (StringUtils.isBlank(key))
            return;
        JedisCommands jedis = this.getPool().getResource();
        try {
            ((BinaryJedisCommands) jedis).setrange((key == null ? null : key.getBytes(charset)), offset, null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }
    
    @Override
    public void expire(String key, Integer value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            ((BinaryJedisCommands) jedis).expire((key == null ? null : key.getBytes(charset)), null == value ? 0 : value);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }
    
    
}
