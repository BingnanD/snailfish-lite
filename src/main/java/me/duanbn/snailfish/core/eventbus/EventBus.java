package me.duanbn.snailfish.core.eventbus;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.concurrent.Executor;
import me.duanbn.snailfish.core.concurrent.Executors;
import me.duanbn.snailfish.core.eventbus.EventHandlerRegister.EventHandlerWrapper;
import me.duanbn.snailfish.util.Validation;
import me.duanbn.snailfish.util.Validation.ValidResult;

/**
 * 事件总线
 * 
 * @author shanwei
 *
 */
@Slf4j
public class EventBus {

	@Setter(AccessLevel.PRIVATE)
	private static EventHandlerRegister eventRegister;

	private static Executor executor = Executors.getInstance().create();

	public static class EventBusInjector {
		@Autowired
		private EventHandlerRegister eventRegister;

		@PostConstruct
		public void postConstruct() {
			EventBus.setEventRegister(eventRegister);
		}
	}

	/**
	 * 异步处理消息. <b>此时事件处理器的实现必须是线程安全的</b> 并且事件处理器的优先级会被忽略. 事件总线不会保证按照优先级顺序执行.
	 * 
	 * @param event
	 * @throws EventException
	 */
	public static void dispatchAsync(EventI event) throws EventException {
		ValidResult validResult = Validation.validateBean(event);
		if (validResult.hasErrors()) {
			log.error("{}", validResult.getErrors());
			throw new EventException(validResult.getErrors());
		}

		List<EventHandlerWrapper> handlers = eventRegister.findHandler(event);

		if (handlers == null || handlers.isEmpty()) {
			return;
		}

		for (EventHandlerWrapper eventHandlerWrapper : handlers) {
			executor.submit(() -> {
				try {
					eventHandlerWrapper.eventHandler.handle(event);
				} catch (Exception e) {
					log.error("handle event errr", e);
				}
			});
		}
	}

	public static void dispatch(EventI event) throws EventException {
		ValidResult validResult = Validation.validateBean(event);
		if (validResult.hasErrors()) {
			log.error("{}", validResult.getErrors());
			throw new EventException(validResult.getErrors());
		}

		List<EventHandlerWrapper> handlers = eventRegister.findHandler(event);

		if (handlers == null || handlers.isEmpty()) {
			return;
		}

		for (EventHandlerWrapper eventHandlerWrapper : handlers) {
			try {
				eventHandlerWrapper.eventHandler.handle(event);
			} catch (Exception e) {
				throw new EventException(e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void subscribe(EventHandlerI handler) {
		eventRegister.addHandler(handler);
	}

	@SuppressWarnings("rawtypes")
	public static void unsubscribe(EventHandlerI handlerI) {
		eventRegister.removeHandler(handlerI);
	}

}
