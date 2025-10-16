package me.duanbn.snailfish.core.ruleengine;

/**
 * 规则异常
 * 
 * @author bingnan.dbn
 */
public class RuleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuleException(String errorMessage) {
		super(errorMessage);
	}

	public RuleException(String errorMessage, Throwable e) {
		super(errorMessage, e);
	}
}
