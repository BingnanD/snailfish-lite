package me.duanbn.snailfish.core.eventbus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.Bootstrap;
import me.duanbn.snailfish.core.RegisterI;
import me.duanbn.snailfish.core.eventbus.annotations.EventHandler;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 事件注册器.
 * 
 * @author zhilin
 * @author bingnan.dbn
 */
@Slf4j
public class EventHandlerRegister implements RegisterI, ApplicationContextAware {

	private ApplicationContext appCtx;

	/** handler hub */
	private Map<Class<EventI>, List<EventHandlerWrapper>> eventHandlerHub = Maps.newConcurrentMap();

	/**
	 * 获取事件处理器
	 * 
	 * @param clazz
	 * @return
	 */
	public List<EventHandlerWrapper> findHandler(EventI event) {
		return eventHandlerHub.get(event.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doRegistration(Class<?> clazz) {
		if (EventHandlerI.class.isAssignableFrom(clazz)) {
			// 获取事件类型
			Class<EventI> genricType = getRegisterObjectGenricType(clazz, 0);

			// 获取事件处理器实例对象.
			EventHandlerI<EventI> eventHandler = (EventHandlerI<EventI>) this.appCtx.getBean(clazz);
			EventHandler annoEventHandler = eventHandler.getClass().getAnnotation(EventHandler.class);
			if (annoEventHandler == null) {
				throw new EventException(clazz + " should be add @EventHandler");
			}
			int priority = annoEventHandler.priority();

			EventHandlerWrapper eventHandlerWrapper = new EventHandlerWrapper();
			eventHandlerWrapper.priority = priority;
			eventHandlerWrapper.eventHandler = eventHandler;

			// 注册事件处理器.
			List<EventHandlerWrapper> handlers = eventHandlerHub.get(genricType);
			if (handlers == null) {
				handlers = Lists.newArrayList();
				eventHandlerHub.put(genricType, handlers);
			}
			handlers.add(eventHandlerWrapper);

			// 根据处理器优先级排序
			Collections.sort(handlers);

			Bootstrap bootstrap = this.appCtx.getBean(Bootstrap.class);
			if (bootstrap.isEnableLog())
				log.info("register event [{}] [{}] done", genricType.getSimpleName(), clazz.getSimpleName());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}

	public static class EventHandlerWrapper implements Comparable<EventHandlerWrapper> {
		int priority;
		EventHandlerI<EventI> eventHandler;

		@Override
		public int compareTo(EventHandlerWrapper arg0) {
			return this.priority - arg0.priority;
		}
	}

}
