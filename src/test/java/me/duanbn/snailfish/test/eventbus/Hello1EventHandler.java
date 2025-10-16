package me.duanbn.snailfish.test.eventbus;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.eventbus.EventHandlerI;
import me.duanbn.snailfish.core.eventbus.annotations.EventHandler;

@Slf4j
@EventHandler(priority = 1)
public class Hello1EventHandler implements EventHandlerI<HelloEvent> {

	@Override
	public void handle(HelloEvent event) {
		log.info("{}", "hello1 " + event.getName());
	}

}
