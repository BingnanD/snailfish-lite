package me.duanbn.snailfish.api.command;

import java.io.Serializable;

import me.duanbn.snailfish.api.Scenario;

/**
 * 命令.
 * 
 * @author bingnan.dbn
 */
public abstract class Command implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 业务id */
	protected Scenario bizId;

}
