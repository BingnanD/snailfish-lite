package me.duanbn.snailfish.api;

/**
 * 场景相关异常.
 * 
 * @author bingnan.dbn
 */
public class ScenarioException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScenarioException(String msg) {
		super(msg);
	}

	public ScenarioException(Throwable t) {
		super(t);
	}

	public ScenarioException(String msg, Throwable t) {
		super(msg, t);
	}

}
