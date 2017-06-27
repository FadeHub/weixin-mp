package com.cooperate.wxh5.wx.redis.lock;

import java.util.concurrent.locks.Lock;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

/**
 * 锁持有工具 
 * <br>
 * <pre>
 *      RedisDistributeLock lock = LockHolder.createLock(&quot;业务编码&quot,&quot;业务ID&quot;, 获取锁超时, 锁超时);
 *      lock.tryLock();
 *      try {
 *       // do some stuff
 *      } finally {
 *          lock.unlock();
 *      }
 * </pre>
 * 
 * @author sunburst
 *
 */
public final class LockHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext; // Spring应用上下文环境

    private static String lockJedisPoolName = "lock_jedis_pool";

    private static Pool<JedisCommands> jedisPool;

    public static final String SPLIT_PREFIX = "#";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LockHolder.applicationContext = applicationContext;
    }

    /**
     * 
     * @param bizCode
     *            [业务CODE]
     * @param lockKey
     *            [锁字段]
     * @param timeoutMsecs
     *            [获取锁超时时间<毫秒>]
     * @param expireMsecs
     *            [锁超时时间<毫秒>]
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Lock createLock(String bizCode, String lockKey, long timeoutMsecs,
                                  long expireMsecs) {
        if (null == jedisPool) {
            jedisPool = (Pool<JedisCommands>) applicationContext.getBean(lockJedisPoolName);
        }
        if (timeoutMsecs <= 0)
            throw new LockException(-1, "锁创建失败.原因：[timeoutMsecs] <= 0");

        if (expireMsecs <= 0)
            throw new LockException(-1, "锁创建失败.原因：[expireMsecs] <= 0");
        return new RedisDistributeLock(jedisPool, bizCode.concat(SPLIT_PREFIX).concat(lockKey), timeoutMsecs, expireMsecs);
    }

    /**
     * 锁超时时间默认为30分钟
     * 
     * @param bizCode
     *            [业务CODE]
     * @param lockKey
     *            [锁字段]
     * @param timeoutMsecs
     *            [获取锁超时时间<毫秒>]
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Lock createLock(String bizCode, String lockKey, long timeoutMsecs) {
        if (null == jedisPool) {
            jedisPool = (Pool<JedisCommands>) applicationContext.getBean(lockJedisPoolName);
        }
        return new RedisDistributeLock(jedisPool, bizCode.concat(SPLIT_PREFIX).concat(lockKey), timeoutMsecs);
    }

    /**
     * 锁超时时间默认为30分钟,获取锁超时时间默认为1分钟
     * 
     * @param bizCode
     *            [业务CODE]
     * @param lockKey
     *            [锁字段]
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Lock createLock(String bizCode, String lockKey) {
        if (null == jedisPool) {
            jedisPool = (Pool<JedisCommands>) applicationContext.getBean(lockJedisPoolName);
        }
        return new RedisDistributeLock(jedisPool, bizCode.concat(SPLIT_PREFIX).concat(lockKey));
    }

    public static String getLockJedisPoolName() {
        return lockJedisPoolName;
    }

    public static void setLockJedisPoolName(String lockJedisPoolName) {
        LockHolder.lockJedisPoolName = lockJedisPoolName;
    }
}
