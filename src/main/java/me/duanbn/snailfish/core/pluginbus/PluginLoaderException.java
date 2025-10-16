package me.duanbn.snailfish.core.pluginbus;

/**
 * 命令相关的异常.
 * 
 * @author bingnan.dbn
 */
public class PluginLoaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PluginLoaderException(String msg) {
		super(msg);
	}

	public PluginLoaderException(Throwable t) {
		super(t);
	}

	public PluginLoaderException(String msg, Throwable t) {
		super(msg, t);
	}

}
