package me.duanbn.snailfish.core.eventbus;

/**
 * 事件处理器.
 * 
 * @author zhilin
 *
 */
public interface EventHandlerI<E extends EventI> {

	/**
	 * 处理事件.
	 * 
	 * @param event 事件.
	 * @return 处理结果.
	 */
	void handle(E event) throws Exception;

}
