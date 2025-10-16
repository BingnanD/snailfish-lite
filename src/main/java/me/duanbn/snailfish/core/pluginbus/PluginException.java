package me.duanbn.snailfish.core.pluginbus;

/**
 * 命令相关的异常.
 * 
 * @author bingnan.dbn
 */
public class PluginException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PluginException(String msg) {
		super(msg);
	}

	public PluginException(Throwable t) {
		super(t);
	}

	public PluginException(String msg, Throwable t) {
		super(msg, t);
	}

}
