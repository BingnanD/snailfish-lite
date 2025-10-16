package me.duanbn.snailfish.test.orm.dao;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao;
import me.duanbn.snailfish.test.orm.entity.TestB;

@Component("testBDao2")
public class TestBDao2Impl extends AbstractDataObjectDao<TestB> implements TestBDao {

    @Override
    protected String datasource() {
        return "dsTwo";
    }

}
