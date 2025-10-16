package me.duanbn.snailfish.core.orm.rdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.api.persistence.Order;
import me.duanbn.snailfish.api.persistence.OrderBy;
import me.duanbn.snailfish.core.orm.rdb.dialect.Dialect;
import me.duanbn.snailfish.util.Validation;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 查询对象实现.
 * 
 * @author duanbn
 */
public class QueryDefaultImpl<T> implements Query, Cloneable {

    /**
     * dao ref
     */
    @SuppressWarnings("rawtypes")
    private DataObjectDao entityDao;

    /** database dialect */
    private Dialect dialect;

    /**
     * 保存取值的字段.
     */
    protected List<Field> fields = new ArrayList<Field>();

    /**
     * join tables.
     */
    protected List<JoinTable> joinTables = Lists.newArrayList();

    /**
     * 保存查询条件.
     */
    protected List<QueryCondition> condList = new ArrayList<QueryCondition>();

    /**
     * 保存排序条件
     */
    protected List<OrderBy> orderList = new ArrayList<OrderBy>();

    /**
     * 分页开始偏移量
     */
    protected int start = -1;
    /**
     * 分页大小
     */
    protected int limit = -1;

    public QueryDefaultImpl() {
    }

    @SuppressWarnings("rawtypes")
    public QueryDefaultImpl(DataObjectDao entityDao, Dialect dialect) {
        this.entityDao = entityDao;
        this.dialect = dialect;
    }

    @Override
    public Query addField(Field... fields) {
        for (Field field : fields) {
            this.fields.add(field);
        }

        return this;
    }

    @Override
    public boolean isJoinQuery() {
        return !CollectionUtils.isEmpty(this.joinTables);
    }

    @Override
    public Query leftJoin(JoinTable joinTable) {
        joinTable.setJoinType(JoinType.LEFT);
        this.joinTables.add(joinTable);
        return this;
    }

    @Override
    public Query innerJoin(JoinTable joinTable) {
        joinTable.setJoinType(JoinType.INNER);
        this.joinTables.add(joinTable);
        return this;
    }

    @Override
    public Query rightJoin(JoinTable joinTable) {
        joinTable.setJoinType(JoinType.RIGHT);
        this.joinTables.add(joinTable);
        return this;
    }

    @Override
    public Query outerJoin(JoinTable joinTable) {
        joinTable.setJoinType(JoinType.OUTER);
        this.joinTables.add(joinTable);
        return this;
    }

    @Override
    public Query and(QueryCondition cond) {
        if (cond == null) {
            throw new IllegalArgumentException("param should not be null");
        }

        if (!condList.isEmpty())
            cond.setConditionRelation(ConditionRelation.AND);

        condList.add(cond);

        return this;
    }

    @Override
    public Query or(QueryCondition cond) {
        if (cond == null) {
            throw new IllegalArgumentException("param should not be null");
        }

        if (!condList.isEmpty())
            cond.setConditionRelation(ConditionRelation.OR);

        condList.add(cond);

        return this;
    }

    @Override
    public Query orderBy(String field, Order order) {
        if (StringUtil.isBlank(field)) {
            throw new IllegalArgumentException("参数错误, field=" + field);
        }
        if (order == null) {
            throw new IllegalArgumentException("参数错误, order=null");
        }

        orderList.add(new OrderBy(field, order));
        return this;
    }

    @Override
    public Query orderBy(OrderBy orderBy) {
        Validation.assertNotNull(orderBy, "order by param is null");

        this.orderBy(orderBy.getField(), orderBy.getOrder());

        return this;
    }

    @Override
    public Query orderBys(Collection<OrderBy> orderBys) {
        Validation.assertNotEmpty(orderBys, "order by param is empty");
        orderBys.forEach(e -> this.orderBy(e));
        return this;
    }

    @Override
    public Query limit(int start, int limit) {
        if (start < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数错误, start=" + start + ", limit=" + limit);
        }

        this.start = start;
        this.limit = limit;

        return this;
    }

    @Override
    public Query limit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("设置limit参数错误， limit=" + limit);
        }

        this.limit = limit;

        return this;
    }

    @Override
    public void clean() {
        this.fields = Lists.newArrayList();
        this.joinTables = Lists.newArrayList();
        this.condList.clear();
        this.orderList.clear();
        this.start = -1;
        this.limit = -1;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    public List<QueryCondition> getCondList() {
        return this.condList;
    }

    public List<OrderBy> getOrderList() {
        return this.orderList;
    }

    public int getStart() {
        return this.start;
    }

    public int getLimit() {
        return this.limit;
    }

    public boolean hasQueryFields() {
        return this.fields != null && this.fields.size() > 0;
    }

    public boolean isEffect() {
        if (this.condList.isEmpty() && this.orderList.isEmpty() && start == -1 && limit == -1) {
            return false;
        }

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Query clone() {
        QueryDefaultImpl clone = new QueryDefaultImpl(this.entityDao, this.dialect);
        clone.fields.addAll(this.fields);
        clone.joinTables.addAll(this.joinTables);
        clone.condList.addAll(this.condList);
        clone.orderList.addAll(this.orderList);
        clone.start = this.start;
        clone.limit = this.limit;
        return clone;
    }

    @Override
    public WhereSQL getWhereSql() {
        WhereSQL sql = getWhereSql(false);
        return sql;
    }

    @Override
    public String getJoinSql() {
        if (CollectionUtils.isEmpty(this.joinTables)) {
            return "";
        }

        StringBuilder sqlText = new StringBuilder();

        this.joinTables.forEach(joinTable -> {
            sqlText.append(" ");
            sqlText.append(joinTable.getJoinType().getFragment()).append(" ")
                    .append(this.dialect.quotes(joinTable.getName()))
                    .append(" as ").append(dialect.quotes(joinTable.getAlias()));
            sqlText.append(" on ").append(joinTable.getOn());
        });

        return sqlText.toString();
    }

    @Override
    public String getFieldSql() {
        if (CollectionUtils.isEmpty(this.fields)) {
            return "*";
        }

        StringBuilder sqlText = new StringBuilder();
        for (Field field : this.fields) {
            sqlText.append(dialect.quotes(field.toString())).append(",");
        }
        if (sqlText.length() > 0) {
            sqlText.deleteCharAt(sqlText.length() - 1);
        }

        return sqlText.toString();
    }

    public WhereSQL getWhereSql(boolean ignoreOrderAndLimit) {
        StringBuilder sqlText = new StringBuilder();
        StringBuilder whereSql = new StringBuilder();
        StringBuilder orderSql = new StringBuilder();
        StringBuilder findInSet = new StringBuilder();
        StringBuilder limitSql = new StringBuilder();
        List<Object> paramList = new ArrayList<Object>();

        // 添加查询条件
        // 当查询条件中只有一个in查询，则需要对此in查询进行排序
        List<QueryCondition> inConditionList = new ArrayList<QueryCondition>();
        if (!condList.isEmpty()) {
            QueryCondition cond = null;
            for (int i = 0; i < condList.size(); i++) {
                cond = condList.get(i);
                cond.setDialect(dialect);

                if (i > 0) {
                    whereSql.append(" ").append(cond.getConditionRelation().getValue()).append(" ")
                            .append(cond.getSql().getSql());
                } else {
                    whereSql.append(cond.getSql().getSql()); // first one
                }

                if (QueryOpt.IN == cond.getOpt()) {
                    inConditionList.add(cond);
                }

                paramList.addAll(cond.getSql().getParams());
            }

            // sort by find in set
            if (!inConditionList.isEmpty() && inConditionList.size() == 1) {
                QueryCondition inCondition = inConditionList.get(0);
                findInSet.append("FIND_IN_SET(").append(inCondition.getField()).append(",'");
                for (Object param : inCondition.getSql().getParams()) {
                    findInSet.append(param).append(',');
                }
                findInSet.deleteCharAt(findInSet.length() - 1);
                findInSet.append("')");
            }
        }

        // 添加排序条件
        if (orderList != null && !orderList.isEmpty()) {
            orderSql.append(" ORDER BY ");
            for (OrderBy orderBy : orderList) {
                orderSql.append(orderBy.getField());
                orderSql.append(" ");
                orderSql.append(orderBy.getOrder().getValue());
                orderSql.append(",");
            }
            orderSql.deleteCharAt(orderSql.length() - 1);
        } else if (!StringUtil.isBlank(findInSet.toString())) {
            orderSql.append(" ORDER BY ");
            orderSql.append(findInSet);
        }

        // 添加分页
        if (start > -1 && limit > -1 && !ignoreOrderAndLimit) {
            limitSql.append(" LIMIT ?,?");
            paramList.add(start);
            paramList.add(limit);
        } else if (limit != -1 && !ignoreOrderAndLimit) {
            limitSql.append(" LIMIT ?");
            paramList.add(limit);
        }

        sqlText.append(whereSql);
        if (!ignoreOrderAndLimit) {
            sqlText.append(orderSql);
            sqlText.append(limitSql);
        }

        WhereSQL sql = WhereSQL.valueOf(sqlText.toString(), paramList);
        if (condList.isEmpty()) {
            sql.setEmptyCondition(true);
        } else {
            sql.setEmptyCondition(false);
        }

        return sql;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        if (fields != null && fields.size() > 0) {
            info.append("fields:");
            for (Field field : fields) {
                info.append(field).append(",");
            }
            info.deleteCharAt(info.length() - 1);
        }
        if (!StringUtil.isBlank(getWhereSql().getSql())) {
            info.append(" wheresql:").append(getWhereSql());
        }
        if (!CollectionUtils.isEmpty(this.condList)) {
            info.append(" ").append(this.condList);
        }
        return info.toString();
    }

    @Override
    public long count() {
        return this.entityDao.countByQuery(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T load() {
        return (T) this.entityDao.findOneByQuery(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> find() {
        return this.entityDao.findByQuery(this);
    }

    @Override
    public <E> Iterator<E> iterator() {
        return new DataObjectIterator<E>(this, this.entityDao);
    }

    @Override
    public void delete() {
        this.entityDao.deleteByQuery(this);
    }

    enum ConditionRelation {
        AND("and"),
        OR("or");

        private String value;

        private ConditionRelation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class WhereSQL {

        /**
         * sql语句
         */
        private String sql;

        /**
         * 查询参数
         */
        private List<Object> params;

        private boolean emptyCondition;

        private WhereSQL() {
        }

        private WhereSQL(String sql, List<Object> paramList) {
            this.sql = sql;
            this.params = paramList;
        }

        public static final WhereSQL valueOf(String sql, Object... params) {
            return new WhereSQL(sql, Arrays.asList(params));
        }

        public static final WhereSQL valueOf(String sql, List<Object> paramList) {
            return new WhereSQL(sql, paramList);
        }

        @Override
        public String toString() {
            return getSql();
        }

        public String getSql() {
            return sql;
        }

        public List<Object> getParams() {
            return params;
        }

        public boolean isEmptyCondition() {
            return emptyCondition;
        }

        public void setEmptyCondition(boolean emptyCondition) {
            this.emptyCondition = emptyCondition;
        }
    }

}
