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

	@Test
	public void testSoWhat() {
		SoWhatEvent event = new SoWhatEvent();

		SoWhatEventHandler soWhatEventHandler = new SoWhatEventHandler();
		SoWhat1EventHandler soWhat1EventHandler = new SoWhat1EventHandler();

		EventBus.subscribe(soWhatEventHandler);
		EventBus.dispatch(event);

		EventBus.subscribe(soWhat1EventHandler);
		EventBus.dispatch(event);

		EventBus.unsubscribe(soWhat1EventHandler);
		EventBus.dispatch(event);

		EventBus.unsubscribe(soWhatEventHandler);

	}

}
