package me.duanbn.snailfish.core.orm;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao.SqlBean;

public class PersistenceException extends RuntimeException {

	private static final long serialVersionUID = 2298344068166413582L;

	public PersistenceException(String msg) {
		super(msg);
	}

	public PersistenceException(Throwable e) {
		super(e);
	}

	public PersistenceException(String msg, Throwable e) {
		super(msg, e);
	}

	public PersistenceException(SqlBean sqlBean, Throwable e) {
		super(sqlBean.toString(), e);
	}

}
