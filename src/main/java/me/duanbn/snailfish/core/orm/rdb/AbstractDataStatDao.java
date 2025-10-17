package me.duanbn.snailfish.core.orm.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.Bootstrap.BootstrapAttribute;
import me.duanbn.snailfish.core.DataNode;
import me.duanbn.snailfish.core.orm.PersistenceException;

@Slf4j
public class AbstractDataStatDao implements DataStatDao, ApplicationContextAware {

    private ApplicationContext appCtx;
    private boolean enableLog;

    protected String datasource() {
        return DataSourceManager.DS_MASTER;
    }

    private NamedParameterJdbcTemplate jdbcTemplate() {
        DataSourceManager dsManager = this.appCtx.getBean(DataSourceManager.class);
        dsManager.ensureDatasourceWorks(datasource());
        return dsManager.jdbcTemplate(datasource());
    }

    @Override
    public List<DataNode> execute(String sql, List<Object> params) {
        try {
            JdbcOperations jdbcTemplate = jdbcTemplate().getJdbcOperations();
            return jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql.toString());

                    if (params == null || params.size() == 0) {
                        return ps;
                    }

                    for (int i = 1; i <= params.size(); i++) {
                        ps.setObject(i, params.get(i - 1));
                    }
                    return ps;
                }
            }, new DataStatRowMapper());
        } catch (Exception e) {
            throw new PersistenceException(sql.toString(), e);
        } finally {
            if (enableLog) {
                log.info("{} {}", sql, params);
            }
            if (log.isDebugEnabled()) {
                log.debug("{} {}", sql, params);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
        BootstrapAttribute bootstrapAttr = this.appCtx.getBean(BootstrapAttribute.class);
        this.enableLog = bootstrapAttr.isEnableSQLLog();
    }

}
