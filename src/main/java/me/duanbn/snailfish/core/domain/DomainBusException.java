package me.duanbn.snailfish.core.domain;

/**
 * 异常.
 * 
 * @author bingnan.dbn
 */
public class DomainBusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DomainBusException(String msg) {
		super(msg);
	}

	public DomainBusException(Throwable t) {
		super(t);
	}

	public DomainBusException(String msg, Throwable t) {
		super(msg, t);
	}

}
