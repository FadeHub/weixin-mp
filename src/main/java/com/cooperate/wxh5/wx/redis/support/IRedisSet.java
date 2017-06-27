package com.cooperate.wxh5.wx.redis.support;

import java.util.Set;

public interface IRedisSet<K> extends ICommon<K>{

    void add(K key, Object value);

    boolean isMemberOf(K key, Object value);

    Set<Object> members(K key);

    <T> T pop(K key, Class<T> classOfT);
}
