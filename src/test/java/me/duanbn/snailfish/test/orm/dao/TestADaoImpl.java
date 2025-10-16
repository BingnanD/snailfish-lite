package me.duanbn.snailfish.test.orm.dao;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao;
import me.duanbn.snailfish.test.orm.entity.TestA;

@Component("testADao")
public class TestADaoImpl extends AbstractDataObjectDao<TestA> implements TestADao {

}
