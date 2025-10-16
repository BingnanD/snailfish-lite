package me.duanbn.snailfish.test.domain;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.domain.DomainE;
import me.duanbn.snailfish.test.domain.beans.DomainEntityE;
import me.duanbn.snailfish.test.domain.beans.DomainValueObjectV;
import me.duanbn.snailfish.util.Random;
import me.duanbn.snailfish.util.collection.Sets;

@Slf4j
public class DomainEntityTest {

	@Test
	public void test() {
		String uuid = Random.javaUUID();
		Date gmtCreate = new Date();
		Date gmtModified = new Date();

		DomainEntityE subDomainEntity = new DomainEntityE();
		subDomainEntity.setUuid(uuid);
		subDomainEntity.setGmtCreate(gmtCreate);
		subDomainEntity.setGmtModified(gmtModified);
		subDomainEntity.setId(1L);
		subDomainEntity.setName("this is a sub domain entity");

		DomainEntityE subDomainEntity1 = new DomainEntityE();
		subDomainEntity1.setUuid(uuid);
		subDomainEntity1.setGmtCreate(gmtCreate);
		subDomainEntity1.setGmtModified(gmtModified);
		subDomainEntity1.setId(1L);
		subDomainEntity1.setName("this is a sub domain entity");

		log.info("{}", subDomainEntity);
		log.info("{}", subDomainEntity1);
		log.info("{}", subDomainEntity == subDomainEntity1);
		log.info("{}", subDomainEntity.equals(subDomainEntity1));

		Set<DomainE> set = Sets.newHashSet();
		set.add(subDomainEntity);
		set.add(subDomainEntity1);

		log.info("{}", set.size());

		DomainValueObjectV domainValueObjectV = new DomainValueObjectV();
		log.info("{}", domainValueObjectV);

	}

}
