package com.cooperate.wxh5.wx.redis.support;

import java.util.Collection;
import java.util.Set;

public interface ICommon<K> {
    /**
     * 删除key&值
     * 
     * @param key
     */
    void delete(K key);

    /**
     * 批量删除
     * 
     * @param keys
     */
    void deleteAll(Collection<K> keys);

    /**
     * 判断是否存在
     * 
     * @param key
     * @return
     */
    boolean isExist(K key);

    /**
     * 查询key
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);
}
