/**
 * 
 */
package me.duanbn.snailfish.core.orm.rdb;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.duanbn.snailfish.api.persistence.Order;
import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao.DbField;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;

/**
 * the iterator for query database table.
 * 
 * @author bingnan.dbn
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked", })
public class DataObjectIterator<E> implements Iterator<E> {

    private static final int DEFAULT_PAGE_SIZE = 2000;

    private static final Lock loadLock = new ReentrantLock();
    private final BlockingDeque<E> recordQ = new LinkedBlockingDeque<>(DEFAULT_PAGE_SIZE);
    private Long latestId;

    private final Query query;
    private final DataObjectDao dao;

    public DataObjectIterator(Query query, DataObjectDao dao) {
        this.latestId = 0L;
        this.query = query;
        this.dao = dao;
    }

    @Override
    public boolean hasNext() {
        if (this.recordQ.isEmpty()) {
            try {
                loadLock.lock();
                if (this.recordQ.isEmpty()) {
                    loadData();
                }
                if (!this.recordQ.isEmpty()) {
                    E last = this.recordQ.getLast();
                    this.latestId = DataObjectUtil.getPrimaryKeyValue(last);
                }
            } finally {
                loadLock.unlock();
            }
        }

        return !this.recordQ.isEmpty();
    }

    @Override
    public E next() {
        return this.recordQ.poll();
    }

    private void loadData() {
        Query innerQuery = query.clone();
        List<E> datas = innerQuery.and(QueryCondition.gt(getPkField(), this.latestId)).orderBy(getPkField(), Order.ASC)
                .limit(DEFAULT_PAGE_SIZE).find();
        this.recordQ.addAll(datas);
    }

    private String getPkField() {
        Class dataObjectClass = this.dao.getDataObjectClass();
        DbField primaryKey = DataObjectUtil.getPrimaryKey(dataObjectClass, true);

        StringBuilder stringBuilder = new StringBuilder();
        if (this.query.isJoinQuery()) {
            Table tableAnno = (Table) dataObjectClass.getAnnotation(Table.class);
            stringBuilder.append(tableAnno.alias()).append(".").append(primaryKey.name);
        } else {
            stringBuilder.append(primaryKey.name);
        }
        return stringBuilder.toString();
    }

}
