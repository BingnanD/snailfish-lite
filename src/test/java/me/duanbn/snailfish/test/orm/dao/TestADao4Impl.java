package me.duanbn.snailfish.test.orm.dao;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao;
import me.duanbn.snailfish.test.orm.CustomDataSourceConfig;
import me.duanbn.snailfish.test.orm.entity.TestA;

@Component("testADao4")
public class TestADao4Impl extends AbstractDataObjectDao<TestA> implements TestADao {

    @Override
    protected String datasource() {
        return CustomDataSourceConfig.DS_FOUR;
    }

}