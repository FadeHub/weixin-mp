package com.cooperate.wxh5.wx.pagination.repository;

import java.util.List;
import java.util.Map;

import com.cooperate.wxh5.wx.pagination.model.DataPaging;

public interface IRepository {
	
	<T> T selectOne(String statement, Object parameter);

    <E>List<E> selectList(String statement, Object parameter);

    <E>DataPaging<E> selectPaging(String statement, Object parameter, int offset, int limit);

    <K, V>Map<K, V> selectMap(String statement, Object parameter, String mapKey);

    int insert(String statement, Object parameter);

    int update(String statement, Object parameter);

    int delete(String statement, Object parameter);
	
}
