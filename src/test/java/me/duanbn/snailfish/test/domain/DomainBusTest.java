package me.duanbn.snailfish.test.domain;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.concurrent.Executor;
import me.duanbn.snailfish.core.concurrent.Executors;
import me.duanbn.snailfish.core.domain.DomainBus;
import me.duanbn.snailfish.test.Application;
import me.duanbn.snailfish.test.domain.beans.DomainEntityE;
import me.duanbn.snailfish.test.domain.beans.DomainEntityEDTO;
import me.duanbn.snailfish.test.domain.beans.DomainValueObjectVDTO;
import me.duanbn.snailfish.test.domain.factory.DomainEntityFactory;
import me.duanbn.snailfish.test.domain.repository.DomainEntityRepository1;
import me.duanbn.snailfish.test.domain.repository.DomainEntityRepositoryI;
import me.duanbn.snailfish.test.domain.service.DomainEntityService2;
import me.duanbn.snailfish.test.domain.service.DomainEntityServiceI;
import me.duanbn.snailfish.util.Mock;

@Slf4j
@SpringBootTest(classes = Application.class)
@SuppressWarnings({ "rawtypes", })
public class DomainBusTest {

	@Test
	public void testMap() throws InterruptedException {
		DomainEntityEDTO domainEntityDTO = Mock.mock(DomainEntityEDTO.class);
		domainEntityDTO.setEntityId("111");
		domainEntityDTO.setId(null);
		domainEntityDTO.setDomainEnum("C");
		DomainValueObjectVDTO domainValueObjectVDTO = new DomainValueObjectVDTO();
		domainValueObjectVDTO.setValue("abc");
		domainEntityDTO.setValueObject(domainValueObjectVDTO);

		Executors executors = Executors.getInstance();

		int threadNum = 100;
		final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		Executor executor = executors.create();
		for (int i = 0; i < threadNum; i++) {
			executor.submit(() -> {
				for (int j = 0; j < 100; j++) {
					long start = System.currentTimeMillis();
					DomainBus.map(domainEntityDTO, DomainEntityE.class);
					log.info("cost {} ms", System.currentTimeMillis() - start);
				}
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();

		executors.shutdown();
	}

	@Test
	public void testEntityClasses() {
		List<Class> domainEntityClasses = DomainBus.getDomainEntityClasses();
		log.info("{}", domainEntityClasses);
	}

	@Test
	public void testValueObjectClasses() {
		List<Class> domainValueObjectClasses = DomainBus.getDomainValueObjectClasses();
		log.info("{}", domainValueObjectClasses);
	}

	@Test
	public void testFactory() {
		DomainEntityE domainEntity = DomainBus.dispatch(DomainEntityFactory.class, e -> e.create());
		log.info("{}", domainEntity);
	}

	@Test
	public void testRepository() {
		DomainBus.dispatchVoid(DomainEntityRepositoryI.class, e -> e.hello());
		DomainBus.dispatchVoid(DomainEntityRepository1.class, e -> e.hello());
	}

	@Test
	public void testService() {
		DomainBus.dispatchVoid(DomainEntityServiceI.class, "DomainEntityService1", e -> e.hello());
		DomainBus.dispatchVoid(DomainEntityService2.class, e -> e.hello());
	}

}
