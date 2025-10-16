package me.duanbn.snailfish.test.orm.dao;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataStatDao;
import me.duanbn.snailfish.test.orm.CustomDataSourceConfig;

@Component("testAStatDao2")
public class TestAStatDao2Impl extends AbstractDataStatDao implements TestAStatDao {

    @Override
    protected String datasource() {
        return CustomDataSourceConfig.DS_TWO;
    }

}
