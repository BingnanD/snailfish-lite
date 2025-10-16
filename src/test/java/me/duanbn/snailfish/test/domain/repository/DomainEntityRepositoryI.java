package me.duanbn.snailfish.test.domain.repository;

import me.duanbn.snailfish.core.domain.pattern.DomainRepositoryI;
import me.duanbn.snailfish.core.orm.rdb.DataObjectDao;
import me.duanbn.snailfish.test.orm.entity.TestA;

public interface DomainEntityRepositoryI extends DomainRepositoryI, DataObjectDao<TestA, Long> {

	void hello();

}