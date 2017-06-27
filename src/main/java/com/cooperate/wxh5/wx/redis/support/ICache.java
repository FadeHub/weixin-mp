package com.cooperate.wxh5.wx.redis.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * K,V缓存接口
 * 
 * @author sunburst
 *
 * @param <K>
 */
public interface ICache<K> extends ICommon<K>{

    /**
     * set一个值,如果原值存在覆盖之
     * 
     * @param key
     * @param value
     */
    void set(K key, Object value);

    /**
     * set一个值,如果原值存在覆盖之
     * 
     * @param key
     * @param value
     * @param timeout
     *            [超时]
     * @param unit
     *            [时间单位]
     */
    void set(K key, Object value, long timeout, TimeUnit unit);

    /**
     * set一个值,如果值存在返回false,否则true
     * 
     * @param key
     * @param value
     * @return
     */
    Boolean setIfAbsent(K key, Object value);

    /**
     * 批量set
     * 
     * @param m
     */
    void multiSet(Map<? extends K, Object> m);

    /**
     * 批量set,如果值存在返回false,否则true
     * 
     * @param m
     * @return
     */
    Boolean multiSetIfAbsent(Map<? extends K, Object> m);

    /**
     * @param key
     * @param classOfT
     *            [值类型]
     * @return
     */
    <T> T get(Object key, Class<T> classOfT);

    /**
     * @param key
     * @param value
     * @param classOfT
     *            [值类型]
     * @return
     */
    <T> T getAndSet(K key, Object value, Class<T> classOfT);

    /**
     * 获取多个key值
     * @param keys
     * @return
     */
    List<Object> multiGet(Collection<K> keys);

    /**
     * 在key对应原值后添加value
     * @param key
     * @param value
     * @return
     */
    Integer append(K key, String value);

    /**
     * 截取key对应的指
     * @param key
     * @param start
     * @param end
     * @return
     */
    String get(K key, long start, long end);

    /**
     * 在给定偏移量后设置值
     * @param key
     * @param value
     * @param offset
     */
    void set(K key, Object value, long offset);

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    boolean isExist(K key);

    /**
     * 设置key过期时间
     * 
     * @param key
     * @param value
     */
    void expire(String key, Integer value);
    
    
}
