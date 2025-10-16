package me.duanbn.snailfish.core.commandbus;

/**
 * 命令相关的异常.
 * 
 * @author bingnan.dbn
 */
public class CommandException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandException(String msg) {
		super(msg);
	}

	public CommandException(Throwable t) {
		super(t);
	}

	public CommandException(String msg, Throwable t) {
		super(msg, t);
	}

}
