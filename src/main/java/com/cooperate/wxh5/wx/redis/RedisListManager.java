package com.cooperate.wxh5.wx.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.cooperate.wxh5.wx.redis.support.IRedisList;
import com.google.common.primitives.Primitives;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;

/**
 * Redis List 操作
 * 
 * @author sunburst
 *
 */
@Component("redisListManager")
public class RedisListManager extends RedisManager implements IRedisList<String> {

    @Override
    public List<Object> range(String key, long start, long end) {
        List<Object> list = null;
        JedisCommands jedis = this.getPool().getResource();
        try {
            List<byte[]> tmpList = ((BinaryJedisCommands) jedis).lrange(key.getBytes(charset), start, end);
            if (null != tmpList && !tmpList.isEmpty()) {
                list = new ArrayList<Object>(tmpList.size());
                for (byte[] s : tmpList) {
                    if (null != s)
                        list.add(this.redisSerializer.deserialize(s));
                }
            }
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
        return list;

    }

    @Override
    public void trim(String key, long start, long end) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            jedis.ltrim(key, start, end);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long size(String key) {
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            return jedis.llen(key);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }

    }

    @Override
    public Long leftPush(String key, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).lpush((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }

    }

    @Override
    public Long leftPushAll(String key, Object... values) {
        JedisCommands jedis = this.getPool().getResource();
        if (values == null)
            return null;
        try {
            final byte[][] vals = new byte[values.length][];
            int i = 0;
            for (byte[] val : vals) {
                if (null != val)
                    vals[i++] = this.redisSerializer.serialize(val);
            }
            return ((BinaryJedisCommands) jedis).lpush(key.getBytes(charset), vals);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long leftPushIfPresent(String key, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).lpushx((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }

    }

    @Override
    public Long leftPush(String key, Object pivot, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).linsert(key.getBytes(charset), LIST_POSITION.BEFORE, null == pivot ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(pivot), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }

    }

    @Override
    public Long rightPush(String key, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).rpush((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }

    }

    @Override
    public Long rightPushAll(String key, Object... values) {
        JedisCommands jedis = this.getPool().getResource();
        if (values == null)
            return null;
        try {
            final byte[][] vals = new byte[values.length][];
            int i = 0;
            for (byte[] val : vals) {
                if (null != val)
                    vals[i++] = this.redisSerializer.serialize(val);
            }
            return ((BinaryJedisCommands) jedis).rpush(key.getBytes(charset), vals);
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long rightPushIfPresent(String key, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).rpushx((key == null ? null : key.getBytes(charset)), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }

    }

    @Override
    public Long rightPush(String key, Object pivot, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).linsert(key.getBytes(charset), LIST_POSITION.AFTER, null == pivot ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(pivot), null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public void set(String key, long index, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            ((BinaryJedisCommands) jedis).lset((key == null ? null : key.getBytes(charset)), index, null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public Long remove(String key, long i, Object value) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            return ((BinaryJedisCommands) jedis).lrem((key == null ? null : key.getBytes(charset)), i, null == value ? this.redisSerializer.serialize(StringUtils.EMPTY) : this.redisSerializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T index(String key, long index, Class<T> classOfT) {
        JedisCommands jedis = this.getReadPool().getResource();
        try {
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(jedis.lindex(key, index).getBytes()));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getReadPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T leftPop(String key, Class<T> classOfT) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            byte[] bytes = ((BinaryJedisCommands) jedis).lpop((key == null ? null : ((String) key).getBytes(charset)));
            if (null == bytes)
                return null;
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(bytes));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T leftPop(String key, Class<T> classOfT, long timeout, TimeUnit unit) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            final int tm = (int) unit.toSeconds(timeout);
            List<byte[]> lPop = ((Jedis) jedis).blpop(tm, (key == null ? null : ((String) key).getBytes(charset)));
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(lPop.get(1)));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T rightPop(String key, Class<T> classOfT) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            byte[] bytes = ((BinaryJedisCommands) jedis).rpop((key == null ? null : ((String) key).getBytes(charset)));
            if (null == bytes)
                return null;
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(bytes));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T rightPop(String key, Class<T> classOfT, long timeout, TimeUnit unit) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            final int tm = (int) unit.toSeconds(timeout);
            List<byte[]> lPop = ((Jedis) jedis).brpop(tm, (key == null ? null : ((String) key).getBytes(charset)));
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(lPop.get(1)));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T rightPopAndLeftPush(String sourceKey, String destinationKey, Class<T> classOfT) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            byte[] bytes = ((Jedis) jedis).rpoplpush(sourceKey.getBytes(charset), destinationKey.getBytes(charset));
            if (null == bytes)
                return null;
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(bytes));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }

    @Override
    public <T> T rightPopAndLeftPush(String sourceKey, String destinationKey, Class<T> classOfT,
                                     long timeout, TimeUnit unit) {
        JedisCommands jedis = this.getPool().getResource();
        try {
            byte[] bytes = ((Jedis) jedis).brpoplpush(sourceKey.getBytes(charset), destinationKey.getBytes(charset), (int) unit.toSeconds(timeout));
            if (null == bytes)
                return null;
            return Primitives.wrap(classOfT).cast(this.redisSerializer.deserialize(bytes));
        } catch (Exception e) {
            throw new RedisException(-1, e.getMessage(), e);
        } finally {
            this.getPool().returnResource(jedis);
        }
    }
}
