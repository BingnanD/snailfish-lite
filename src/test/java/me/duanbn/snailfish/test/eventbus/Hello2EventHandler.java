package me.duanbn.snailfish.test.eventbus;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.eventbus.EventHandlerI;
import me.duanbn.snailfish.core.eventbus.annotations.EventHandler;

@Slf4j
@EventHandler(priority = 0)
public class Hello2EventHandler implements EventHandlerI<HelloEvent> {

	@Override
	public void handle(HelloEvent event) throws Exception {
		Thread.sleep(500L);
		log.info("{}", "hello2 " + event.getName());
	}

}
