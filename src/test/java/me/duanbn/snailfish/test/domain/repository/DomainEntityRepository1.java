package me.duanbn.snailfish.test.domain.repository;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.domain.annotations.DomainRepository;
import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao;
import me.duanbn.snailfish.test.orm.entity.TestA;

@Slf4j
@DomainRepository
public class DomainEntityRepository1 extends AbstractDataObjectDao<TestA> implements DomainEntityRepositoryI {

	@Override
	public void hello() {
		log.info("hello command bus");
	}

}
