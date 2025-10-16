package me.duanbn.snailfish.test.orm.dao;

import org.springframework.stereotype.Component;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao;
import me.duanbn.snailfish.test.orm.entity.TestB;

@Component("testBDao")
public class TestBDaoImpl extends AbstractDataObjectDao<TestB> implements TestBDao {

}
