package me.duanbn.snailfish.test.eventbus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.eventbus.EventBus;
import me.duanbn.snailfish.test.Application;

@Slf4j
@SpringBootTest(classes = Application.class)
public class EventBusTest {

	@Test
	public void test() throws Exception {
		HelloEvent helloEvent = HelloEvent.builder().name("shanwei").build();

		long start = System.currentTimeMillis();
		EventBus.dispatch(helloEvent);
		log.info("{}", System.currentTimeMillis() - start);

		start = System.currentTimeMillis();
		EventBus.dispatchAsync(helloEvent);
		log.info("{}", System.currentTimeMillis() - start);

		Thread.sleep(1000L);
	}

}
