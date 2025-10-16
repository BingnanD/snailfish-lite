package me.duanbn.snailfish.core.orm.rdb;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import me.duanbn.snailfish.core.Bootstrap;
import me.duanbn.snailfish.core.domain.pattern.DomainSPEC;
import me.duanbn.snailfish.core.orm.PersistenceException;
import me.duanbn.snailfish.core.orm.rdb.QueryDefaultImpl.WhereSQL;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;
import me.duanbn.snailfish.core.orm.rdb.dialect.Dialect;
import me.duanbn.snailfish.core.orm.rdb.dialect.Dialect.DBCategoryEnum;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDataObjectDao<E> implements ApplicationContextAware, DataObjectDao<E, Long> {

    public static final String DEFAULT_FIELD_GMTCREATE = "GMT_CREATE";
    public static final String DEFAULT_FIELD_MODIFIED = "GMT_MODIFIED";

    protected ApplicationContext appCtx;

    private boolean enableLog;

    protected String datasource() {
        return DataSourceManager.DS_MASTER;
    }

    private Dialect dialect() {
        DataSourceManager dsManager = this.appCtx.getBean(DataSourceManager.class);
        dsManager.ensureDatasourceWorks(datasource());
        return dsManager.dialect(datasource());
    }

    private NamedParameterJdbcTemplate jdbcTemplate() {
        DataSourceManager dsManager = this.appCtx.getBean(DataSourceManager.class);
        dsManager.ensureDatasourceWorks(datasource());
        return dsManager.jdbcTemplate(datasource());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected String tableName(boolean ignoreAlias) {
        Class entityType = _getEntityType();
        Table table = (Table) entityType.getAnnotation(Table.class);
        if (table == null || table.value() == null) {
            throw new PersistenceException(entityType + " must be annotationed by @Table");
        }

        StringBuilder tableName = new StringBuilder();
        Dialect dialect = dialect();
        tableName.append(dialect.quotes(table.value()));
        if (StringUtil.isNotBlank(table.alias()) && !ignoreAlias) {
            tableName.append(" AS ").append(dialect.quotes(table.alias()));
        }

        return tableName.toString();
    }

    @Override
    public Query createQuery(DomainSPEC spec) {
        Query query = this.createQuery();
        spec.appendQuery(query);
        return query;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Query createQuery() {
        return new QueryDefaultImpl(this, dialect());
    }

    /************************************************
     * insert
     ************************************************/
    public Long insert(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("insert entity is null");
        }

        return insert(entity, false, true);
    }

    @Override
    public Long insert(E entity, boolean autoIncrementId, boolean includeSuperClass) {
        SqlBean sqlBean = null;
        try {
            // get primary key
            DbField primaryKey = DataObjectUtil.getPrimaryKey(entity.getClass(), includeSuperClass);

            KeyHolder keyHolder = new GeneratedKeyHolder();
            if (dialect().support(DBCategoryEnum.MYSQL) && autoIncrementId) {
                sqlBean = buildInsertSql(entity, true, includeSuperClass);
                jdbcTemplate().update(sqlBean.sql, sqlBean.getSqlParamSource(), keyHolder);
                long id = keyHolder.getKey().longValue();
                primaryKey.setValue(entity, id);
            } else if (dialect().support(DBCategoryEnum.H2) && autoIncrementId) {
                sqlBean = buildInsertSql(entity, true, includeSuperClass);
                jdbcTemplate().update(sqlBean.sql, sqlBean.getSqlParamSource(), keyHolder);
                long id = keyHolder.getKey().longValue();
                primaryKey.setValue(entity, id);
            } else if (dialect().support(DBCategoryEnum.POSTGRESQL) && autoIncrementId) {
                sqlBean = buildInsertSql(entity, true, includeSuperClass);
                jdbcTemplate().update(sqlBean.sql, sqlBean.getSqlParamSource(), keyHolder);
                long id = ((Number) keyHolder.getKeys().get(primaryKey.name)).longValue();
                primaryKey.setValue(entity, id);
            } else {
                sqlBean = buildInsertSql(entity, autoIncrementId, includeSuperClass);
                jdbcTemplate().update(sqlBean.sql, sqlBean.getSqlParamSource());
            }

            return (Long) primaryKey.getValue(entity);
        } catch (DuplicateKeyException e) {
            throw e;
        } catch (Exception e) {
            if (sqlBean == null) {
                throw new IllegalStateException(e);
            }

            throw new PersistenceException(sqlBean, e);
        } finally {
            if (enableLog) {
                log.info("{}", sqlBean.sql);
                log.info("{}", sqlBean.value);
            }
            if (log.isDebugEnabled()) {
                log.debug("{}", sqlBean.sql);
                log.debug("{}", sqlBean.value);
            }
        }
    }

    public int insertBatch(List<E> entityList) {
        return insertBatch(entityList, false, true);
    }

    public int insertBatch(List<E> entityList, boolean autoIncrementId, boolean includeSuperClass) {
        if (CollectionUtils.isEmpty(entityList)) {
            return 0;
        }

        for (E entity : entityList) {
            insert(entity, autoIncrementId, includeSuperClass);
        }
        return entityList.size();
    }

    protected SqlBean buildInsertSql(E entity, boolean autoIncrementId, boolean includeSuperClass) throws Exception {
        Dialect dialect = dialect();

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName(true)).append(" (");

        Collection<DbField> fieldList = DataObjectUtil.getFields(entity.getClass(), includeSuperClass);

        Map<String, Object> data = Maps.newHashMap();
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (DbField f : fieldList) {
            f.original.setAccessible(true);

            if (f.primaryKey && autoIncrementId) {
                continue;
            }
            if ((f.name.equals(DEFAULT_FIELD_GMTCREATE) || f.name.equals(DEFAULT_FIELD_MODIFIED))
                    && f.original.get(entity) == null) {
                Date date = new Date();
                data.put(f.name, date);
                f.setValue(entity, date);
            } else {
                data.put(f.name, getValue(f.getValue(entity), f.jsonField));
            }

            if (fields.length() > 0) {
                fields.append(",");
            }

            fields.append(dialect.quotes(f.name));

            if (values.length() > 0) {
                values.append(",");
            }
            values.append(":").append(f.name);
        }

        sql.append(fields).append(") values (").append(values).append(")");

        SqlBean sqlBean = new SqlBean(sql.toString(), data);

        return sqlBean;
    }

    /************************************************
     * update
     ************************************************/
    @SuppressWarnings("unchecked")
    public int update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("update entity is null");
        }

        return updateBatch(Lists.newArrayList(entity))[0];
    }

    @SuppressWarnings("unchecked")
    public int update(E entity, boolean includeSuperClass) {
        return updateBatch(Lists.newArrayList(entity), includeSuperClass)[0];
    }

    public int[] updateBatch(List<E> entityList) {
        return updateBatch(entityList, true);
    }

    public int[] updateBatch(List<E> entityList, boolean includeSuperClass) {
        if (CollectionUtils.isEmpty(entityList)) {
            return new int[] { 0 };
        }

        SqlParameterSource[] sqlValues = null;
        StringBuilder sql = new StringBuilder();
        try {
            E entity = entityList.get(0);
            UpdateSqlBean updateSqlBean = buildUpdateFields(entity, includeSuperClass);
            sql.append("UPDATE ").append(tableName(true)).append(" SET ");
            sql.append(updateSqlBean.fieldsFragment).append(" WHERE ");
            sql.append(updateSqlBean.whereFragment);

            sqlValues = new SqlParameterSource[entityList.size()];
            for (int i = 0; i < entityList.size(); i++) {
                entity = entityList.get(i);
                setUpdateDate(entity, includeSuperClass);
                sqlValues[i] = buildUpdateData(entity, includeSuperClass);
            }

            return jdbcTemplate().batchUpdate(sql.toString(), sqlValues);
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql, sqlValues);
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql, sqlValues);
            }
        }
    }

    protected UpdateSqlBean buildUpdateFields(E entity, boolean includeSuperClass) throws Exception {
        Dialect dialect = dialect();

        UpdateSqlBean updateSqlBean = new UpdateSqlBean();

        StringBuilder updateFields = new StringBuilder();
        StringBuilder whereFields = new StringBuilder();

        Collection<DbField> fieldList = DataObjectUtil.getFields(entity.getClass(), includeSuperClass);
        for (DbField f : fieldList) {

            if (f.primaryKey) {
                whereFields.append(dialect.quotes(f.name) + "=:" + f.name);
                continue;
            }

            if (f.name.equals(DEFAULT_FIELD_GMTCREATE)) {
                continue;
            }

            if (updateFields.length() > 0) {
                updateFields.append(",");
            }
            updateFields.append(dialect.quotes(f.name)).append("=:").append(f.name);
        }

        updateSqlBean.fieldsFragment = updateFields.toString();
        updateSqlBean.whereFragment = whereFields.toString();

        return updateSqlBean;
    }

    protected SqlParameterSource buildUpdateData(E entity, boolean includeSuperClass) throws Exception {
        Map<String, Object> data = Maps.newHashMap();

        Collection<DbField> fieldList = DataObjectUtil.getFields(entity.getClass(), includeSuperClass);
        for (DbField f : fieldList) {
            f.original.setAccessible(true);

            if (f.name.equals(DEFAULT_FIELD_MODIFIED)) {
                data.put(f.name, new Date());
                continue;
            }

            data.put(f.name, getValue(f.getValue(entity), f.jsonField));
        }

        SqlParameterSource result = new MapSqlParameterSource(data);
        return result;
    }

    /************************************************
     * delete
     ************************************************/
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableName(true)).append(" WHERE ID in (:ID)");

        Map<String, Object> param = new HashMap<String, Object>();
        try {
            param.put("ID", ids);

            jdbcTemplate().update(sql.toString(), new MapSqlParameterSource(param));
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql.toString(), param);
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql.toString(), param);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void deleteByQuery(Query query) {
        final WhereSQL whereSql = ((QueryDefaultImpl) query).getWhereSql(true);
        final StringBuilder sql = new StringBuilder("DELETE FROM " + this.tableName(true));

        if (!whereSql.isEmptyCondition()) {
            sql.append(" WHERE ");
        } else {
            sql.append(" ");
        }
        sql.append(whereSql.getSql());

        try {
            JdbcOperations jdbcTemplate = jdbcTemplate().getJdbcOperations();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    List<Object> params = whereSql.getParams();
                    PreparedStatement ps = con.prepareStatement(sql.toString());
                    for (int i = 1; i <= params.size(); i++) {
                        ps.setObject(i, params.get(i - 1));
                    }
                    return ps;
                }
            });
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql.toString(), whereSql.getParams());
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql.toString(), whereSql.getParams());
            }
        }
    }

    /************************************************
     * select
     ************************************************/
    @Override
    public boolean exist(Long id) {
        return findById(id) != null;
    }

    @Override
    public E findById(Long id) {
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        List<E> data = findByIds(ids);

        if (CollectionUtils.isEmpty(data)) {
            return null;
        }

        return data.get(0);
    }

    @SuppressWarnings({ "unchecked" })
    public List<E> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<E>();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName(true)).append(" WHERE ID in (:ID)");
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            param.put("ID", ids);

            List<E> data = jdbcTemplate().query(sql.toString(), new MapSqlParameterSource(param),
                    new DataObjectPropertyRowMapper<E>(_getEntityType(), dialect()));

            return data;
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql.toString(), param);
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql.toString(), param);
            }
        }
    }

    @Override
    public long count() {
        final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM " + tableName(false));
        try {
            JdbcOperations jdbcTemplate = jdbcTemplate().getJdbcOperations();
            return jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql.toString());
                    return ps;
                }
            }, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{}", sql.toString());
            }
            if (log.isDebugEnabled()) {
                log.debug("{}", sql.toString());
            }
        }
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public long countByQuery(Query query) {
        final WhereSQL whereSql = ((QueryDefaultImpl) query).getWhereSql(true);
        final String joinSql = query.getJoinSql();
        final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM " + tableName(false));

        if (StringUtil.isNotBlank(joinSql)) {
            sql.append(joinSql);
        }

        if (!whereSql.isEmptyCondition()) {
            sql.append(" WHERE ");
        } else {
            sql.append(" ");
        }
        sql.append(whereSql.getSql());
        try {
            JdbcOperations jdbcTemplate = jdbcTemplate().getJdbcOperations();
            return jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    List<Object> params = whereSql.getParams();
                    PreparedStatement ps = con.prepareStatement(sql.toString());
                    for (int i = 1; i <= ps.getParameterMetaData().getParameterCount(); i++) {
                        ps.setObject(i, params.get(i - 1));
                    }
                    return ps;
                }
            }, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql.toString(), whereSql.getParams());
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql.toString(), whereSql.getParams());
            }
        }
    }

    @Override
    public E findOneByQuery(Query query) {
        List<E> data = findByQuery(query);

        if (CollectionUtils.isEmpty(data)) {
            return null;
        }

        return data.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> findAll() {
        String sql = "SELECT * FROM " + tableName(true);

        try {
            return jdbcTemplate().query(sql, new DataObjectPropertyRowMapper<E>(_getEntityType(), dialect()));
        } catch (Exception e) {
            throw new PersistenceException(sql, e);
        } finally {
            if (enableLog) {
                log.info("{}", sql.toString());
            }
            if (log.isDebugEnabled()) {
                log.debug("{}", sql.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> findByQuery(Query query) {
        final String fieldSql = query.getFieldSql();
        final String joinSql = query.getJoinSql();
        final WhereSQL whereSql = query.getWhereSql();

        StringBuilder sql = new StringBuilder("SELECT " + fieldSql + " FROM " + tableName(false));
        if (StringUtil.isNotBlank(joinSql)) {
            sql.append(joinSql);
        }

        if (!whereSql.isEmptyCondition()) {
            sql.append(" WHERE ");
        } else {
            sql.append(" ");
        }
        sql.append(whereSql.getSql());

        try {
            JdbcOperations jdbcTemplate = jdbcTemplate().getJdbcOperations();
            List<E> result = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    List<Object> params = whereSql.getParams();
                    PreparedStatement ps = con.prepareStatement(sql.toString());
                    for (int i = 1; i <= params.size(); i++) {
                        ps.setObject(i, params.get(i - 1));
                    }
                    return ps;
                }
            }, new DataObjectPropertyRowMapper<E>(_getEntityType(), dialect()));

            if (log.isDebugEnabled()) {
                log.debug("{}", result);
            }

            return result;
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql.toString(), whereSql.getParams());
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql.toString(), whereSql.getParams());
            }
        }
    }

    @Override
    public void clean() {
        String sql = "DELETE FROM " + tableName(true);
        try {
            JdbcOperations jdbcTemplate = jdbcTemplate().getJdbcOperations();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql.toString());
                    return ps;
                }
            });
        } catch (Exception e) {
            throw new PersistenceException(sql, e);
        } finally {
            if (enableLog) {
                log.info("{}", sql.toString());
            }
            if (log.isDebugEnabled()) {
                log.debug("{}", sql.toString());
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getDataObjectClass() {
        return _getEntityType();
    }

    @SuppressWarnings("rawtypes")
    protected Class _getEntityType() {
        Type genType = this.getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class) params[0];
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
        Bootstrap bootstrap = this.appCtx.getBean(Bootstrap.class);
        this.enableLog = bootstrap.isEnableSQLLog();
    }

    /******************************************************************************
     * Uiltity Method
     ******************************************************************************/
    public Object getValue(Object value, boolean jsonField) {
        if (value instanceof Map || value instanceof List || value instanceof Set || jsonField) {
            return JSON.toJSONString(value);
        }

        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }

        if (value instanceof Boolean) {
            if ((Boolean) value) {
                return 1;
            } else {
                return 0;
            }
        }

        return value;
    }

    private void setUpdateDate(E entity, boolean includeSuperClass) {
        DbField updateDateField = DataObjectUtil.getFieldsWithMap(entity.getClass(), includeSuperClass)
                .get(AbstractDataObjectDao.DEFAULT_FIELD_MODIFIED);

        if (updateDateField == null) {
            return;
        }

        try {
            updateDateField.original.set(entity, new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /******************************************************************************
     * Inner Class Define
     ******************************************************************************/
    public static class UpdateSqlBean {
        public String fieldsFragment;
        public String whereFragment;
    }

    public static class DbField {
        public Field original;
        public String name;
        public boolean nullable;
        public boolean index;
        public int length;
        public String comment;
        public boolean primaryKey;
        public boolean jsonField;

        public void setValue(Object target, Object value) {
            try {
                this.original.set(target, value);
            } catch (Exception e) {
                throw new PersistenceException(e);
            }
        }

        public Object getValue(Object target) {
            try {
                return this.original.get(target);
            } catch (Exception e) {
                throw new PersistenceException(e);
            }
        }
    }

    public static class SqlBean {
        private String sql;
        private Map<String, Object> value;

        public SqlBean(String sql, Map<String, Object> value) {
            assert !StringUtil.isBlank(sql) : "sql should not be empty";
            assert value != null : "value should not be null";

            this.sql = sql;
            this.value = value;
        }

        public SqlParameterSource getSqlParamSource() {
            return new MapSqlParameterSource(this.value);
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder(this.sql);
            s.append("\n").append(value);
            return s.toString();
        }
    }

}
