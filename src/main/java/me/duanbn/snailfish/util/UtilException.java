package me.duanbn.snailfish.util;

public class UtilException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UtilException(String msg) {
		super(msg);
	}

	public UtilException(Throwable t) {
		super(t);
	}

	public UtilException(String msg, Throwable t) {
		super(msg, t);
	}

}
