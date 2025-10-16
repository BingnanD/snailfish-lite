package me.duanbn.snailfish.core.commandbus;

import me.duanbn.snailfish.api.command.Command;
import me.duanbn.snailfish.api.dto.Response;

/**
 * 命令执行器.
 * 
 * @author bingnan.dbn
 * @author zhilin
 */
public interface CommandExecutorI<C extends Command, R extends Response> {

	/**
	 * 执行某个命令触发的逻辑.
	 * 
	 * @param command
	 * @return
	 */
	void execute(C command, R resp) throws CommandExecuteException;

}
