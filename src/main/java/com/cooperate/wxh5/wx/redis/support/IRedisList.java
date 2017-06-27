package com.cooperate.wxh5.wx.redis.support;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IRedisList<K> extends ICommon<K>{

    List<Object> range(K key, long start, long end);
    
    void trim(K key, long start, long end);

    Long size(K key);

    Long leftPush(K key, Object value);

    Long leftPushAll(K key, Object... values);

    Long leftPushIfPresent(K key, Object value);

    Long leftPush(K key, Object pivot, Object value);

    Long rightPush(K key, Object value);

    Long rightPushAll(K key, Object... values);

    Long rightPushIfPresent(K key, Object value);

    Long rightPush(K key, Object pivot, Object value);

    void set(K key, long index, Object value);

    Long remove(K key, long i, Object value);

    <T> T index(K key, long index, Class<T> classOfT);

    <T> T leftPop(K key, Class<T> classOfT);

    <T> T leftPop(K key, Class<T> classOfT, long timeout, TimeUnit unit);

    <T> T rightPop(K key, Class<T> classOfT);

    <T> T rightPop(K key, Class<T> classOfT, long timeout, TimeUnit unit);

    <T> T rightPopAndLeftPush(K sourceKey, K destinationKey, Class<T> classOfT);

    <T> T rightPopAndLeftPush(K sourceKey, K destinationKey, Class<T> classOfT, long timeout,
                              TimeUnit unit);

}
