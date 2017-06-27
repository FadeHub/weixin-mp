package com.cooperate.wxh5.wx.pagination.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.cooperate.wxh5.wx.pagination.PaginationInterceptor;
import com.cooperate.wxh5.wx.pagination.model.DataPaging;

public class Repository implements IRepository {

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return sqlSession.selectOne(statement, parameter);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		return sqlSession.selectList(statement, parameter);
	}

	@Override
	public <E> DataPaging<E> selectPaging(String statement, Object parameter, int offset, int limit) {
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<E> rows = sqlSession.selectList(statement, parameter, rowBounds);
		int total = PaginationInterceptor.getPaginationTotal();
		DataPaging<E> dataPaging = new DataPaging<E>(rows, total);
		return dataPaging;
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return sqlSession.selectMap(statement, parameter, mapKey);
	}

	@Override
	public int insert(String statement, Object parameter) {
		return sqlSession.insert(statement, parameter);
	}

	@Override
	public int update(String statement, Object parameter) {
		return sqlSession.update(statement, parameter);
	}

	@Override
	public int delete(String statement, Object parameter) {
		return sqlSession.delete(statement, parameter);
	}

}
