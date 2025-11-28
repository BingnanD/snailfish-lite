package me.duanbn.snailfish.core.orm.rdb;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.orm.PersistenceException;
import me.duanbn.snailfish.core.orm.rdb.dialect.Dialect;
import me.duanbn.snailfish.core.orm.rdb.dialect.GenericDialect;
import me.duanbn.snailfish.core.orm.rdb.dialect.MysqlDialect;
import me.duanbn.snailfish.core.orm.rdb.dialect.PostgresqlDialect;
import me.duanbn.snailfish.util.collection.Maps;

@Slf4j
public class DataSourceManager implements ApplicationContextAware {

    private ApplicationContext appCtx;

    // protected static final String DS_MASTER = "master";
    protected static final String DS_MASTER = "dataSource";

    private Map<String, NamedParameterJdbcTemplate> templateMap = Maps.newConcurrentMap();
    private Map<String, Dialect> dialectMap = Maps.newConcurrentMap();

    public NamedParameterJdbcTemplate jdbcTemplate(String dsName) {
        NamedParameterJdbcTemplate jdbcTemplate = this.templateMap.get(dsName);
        if (jdbcTemplate == null) {
            throw new PersistenceException(
                    "can not found datasource, ensure that have config spring.datasource." + dsName + ".xxxxxxx");
        }
        return jdbcTemplate;
    }

    public Dialect dialect(String dsName) {
        Dialect dialect = this.dialectMap.get(dsName);
        if (dialect == null) {
            throw new PersistenceException(
                    "can not found datasource, ensure that have config spring.datasource." + dsName + ".xxxxxxx");
        }
        return dialect;
    }

    void ensureDatasourceWorks(String dsName) {
        if (!this.appCtx.containsBean(dsName)) {
            throw new PersistenceException(
                    "Can not found datasource by name=" + dsName + " ensure that has a spring datasource bean named "
                            + dsName);
        }

        DataSource ds = (DataSource) this.appCtx.getBean(dsName);

        if (!this.templateMap.containsKey(dsName)) {
            NamedParameterJdbcTemplate customJdbcTemplate = new NamedParameterJdbcTemplate(ds);
            this.templateMap.put(dsName, customJdbcTemplate);
            log.info("load {} datasource done", dsName);
        }

        if (!this.dialectMap.containsKey(dsName)) {
            this.dialectMap.put(dsName, findDialect(ds));
        }

    }

    private Dialect findDialect(DataSource datasource) {
        try {
            DatabaseMetaData dbMetaData = datasource.getConnection().getMetaData();

            String dbProductName = dbMetaData.getDatabaseProductName();

            Dialect dialect = null;
            if (dbProductName.equals("MySQL")) {
                dialect = new MysqlDialect();
            } else if (dbProductName.equals("PostgreSQL")) {
                dialect = new PostgresqlDialect();
            } else if (dbProductName.equals("TDDL")) {
                dialect = new GenericDialect();
            } else if (dbProductName.equals("H2")) {
                dialect = new GenericDialect();
            } else if (dbProductName.equals("DM DBMS")) {
                // dialect = new DaMengDialect();
            } else {
                dialect = new GenericDialect();
            }

            if (log.isDebugEnabled()) {
                log.debug("database product name: {}, dialect=", dbProductName, dialect.getClass().getSimpleName());
            }

            return dialect;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }

}
