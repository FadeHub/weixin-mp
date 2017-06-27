package com.cooperate.wxh5.wx.pagination.dialect;

/**
 * Date Created 2014-2-18
 *
 * @author loafer[zjh527@163.com]
 * @version 2.0
 */
public abstract class DialectFactory {
	public static Dialect buildDialect(Dialect.Type dialectType) {
		switch (dialectType) {
		case MYSQL:
			return new MySQLDialect();
		case ORACLE:
			return new OracleDialect();
		default:
			throw new UnsupportedOperationException();
		}
	}
}