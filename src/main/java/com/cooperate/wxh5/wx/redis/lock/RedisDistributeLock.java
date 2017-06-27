package com.cooperate.wxh5.wx.redis.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

/**
 * 基于Redis的分布式锁实现
 * 
 * @author sunburst
 *
 */
public final class RedisDistributeLock implements Lock {

    private Pool<JedisCommands> jedisPool;

    /**
     * Lock key path.
     */
    private String lockKey;

    /**
     * Lock expiration in miliseconds.
     */
    private long expireMsecs = 30 * 60 * 1000;

    /**
     * Acquire timeout in miliseconds.
     */
    private long timeoutMsecs = 60 * 1000;

    private boolean locked = false;

    public RedisDistributeLock(Pool<JedisCommands> jedis, String lockKey) {
        this.jedisPool = jedis;
        this.lockKey = lockKey;
    }

    public RedisDistributeLock(Pool<JedisCommands> jedis, String lockKey, long timeoutMsecs) {
        this(jedis, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    public RedisDistributeLock(Pool<JedisCommands> jedis, String lockKey, long timeoutMsecs, long expireMsecs) {
        this(jedis, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * <注意：此方法为在超时范围内阻塞式获取锁，如果超时仍然没有获得锁，将不<b>中断，继续进行后续操作</b>>
     */
    @Override
    public synchronized void lock() {
        long timeout = timeoutMsecs;
        JedisCommands jedis = jedisPool.getResource();
        try {
            while (timeout >= 0) {
                long expires = System.currentTimeMillis() + expireMsecs + 1;
                String expiresStr = String.valueOf(expires);
                if (jedis.setnx(lockKey, expiresStr) == 1) {
                    // lock acquired
                    jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                    locked = true;
                    return;
                }
                String currentValueStr = jedis.get(lockKey);
                if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                    // lock is expired
                    String oldValueStr = jedis.getSet(lockKey, expiresStr);
                    jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                    if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                        // lock acquired
                        locked = true;
                        return;
                    }
                }
                timeout -= 100;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new LockException(-1, e);
                }
            }
        } finally {
            jedisPool.returnResource(jedis);
        }

    }

    /**
     * <注意：此方法为在超时范围内阻塞式获取锁，如果超时仍然没有获得锁，将<b>中断不执行后续操作</b>>
     */
    @Override
    public synchronized void lockInterruptibly() throws InterruptedException {
        long timeout = timeoutMsecs;
        JedisCommands jedis = jedisPool.getResource();
        try {
            while (timeout >= 0) {
                long expires = System.currentTimeMillis() + expireMsecs + 1;
                String expiresStr = String.valueOf(expires);
                if (jedis.setnx(lockKey, expiresStr) == 1) {
                    jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                    // lock acquired
                    locked = true;
                    return;
                }
                String currentValueStr = jedis.get(lockKey);
                if (currentValueStr != null
                    && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                    // lock is expired
                    String oldValueStr = jedis.getSet(lockKey, expiresStr);
                    jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                    if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                        // lock acquired
                        locked = true;
                        return;
                    }
                }
                timeout -= 100;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new LockException(-1, e);
                }
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        throw new LockException(-1, "获取分布式锁超时");
    }

    /**
     * <注意：此方法为非阻塞式获取锁>
     */
    @Override
    public synchronized boolean tryLock() {
        JedisCommands jedis = jedisPool.getResource();
        try {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires);
            if (jedis.setnx(lockKey, expiresStr) == 1) {
                jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                // lock acquired
                locked = true;
                return true;
            }
            String currentValueStr = jedis.get(lockKey);
            if (currentValueStr != null
                && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                // lock is expired
                String oldValueStr = jedis.getSet(lockKey, expiresStr);
                jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    // lock acquired
                    locked = true;
                    return true;
                }
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }

    /**
     * <注意：此方法为在给定时间范围内的非阻塞式获取锁(给定时间为阻塞时间)>
     */
    @Override
    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        JedisCommands jedis = jedisPool.getResource();
        try {
            long timeout = unit.toMillis(time);
            while (timeout >= 0) {
                long expires = System.currentTimeMillis() + expireMsecs + 1;
                String expiresStr = String.valueOf(expires);
                if (jedis.setnx(lockKey, expiresStr) == 1) {
                    jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                    // lock acquired
                    locked = true;
                    return true;
                }
                String currentValueStr = jedis.get(lockKey);
                if (currentValueStr != null
                    && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                    // lock is expired
                    String oldValueStr = jedis.getSet(lockKey, expiresStr);
                    jedis.expire(lockKey, (int) TimeUnit.MILLISECONDS.toSeconds(expireMsecs) + 30);
                    if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                        // lock acquired
                        locked = true;
                        return true;
                    }
                }
                timeout -= 20;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new LockException(-1, e);
                }
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }

    @Override
    public synchronized void unlock() {
        JedisCommands jedis = jedisPool.getResource();
        try {
            if (locked) {
                jedis.del(lockKey);
                locked = false;
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 暂不支持
     */
    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * 域访问器
     */

    public String getLockKey() {
        return lockKey;
    }

    public long getExpireMsecs() {
        return expireMsecs;
    }

    public long getTimeoutMsecs() {
        return timeoutMsecs;
    }

    public boolean isLocked() {
        return locked;
    }
}
