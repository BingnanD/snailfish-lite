package me.duanbn.snailfish.core.eventbus;

/**
 * 事件相关的异常.
 * 
 * @author bingnan.dbn
 */
public class EventException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventException(String msg) {
		super(msg);
	}

	public EventException(Throwable t) {
		super(t);
	}

	public EventException(String msg, Throwable t) {
		super(msg, t);
	}

}
