package me.duanbn.snailfish.test.orm.dao;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao;
import me.duanbn.snailfish.test.orm.entity.TestB;

@Component("testBDao4")
public class TestBDao4Impl extends AbstractDataObjectDao<TestB> implements TestBDao {

    @Override
    protected String datasource() {
        return "dsFour";
    }

}
