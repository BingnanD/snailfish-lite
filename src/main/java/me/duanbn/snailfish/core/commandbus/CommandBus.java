package me.duanbn.snailfish.core.commandbus;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.api.command.Command;
import me.duanbn.snailfish.api.dto.Response;
import me.duanbn.snailfish.util.ObjectUtil;
import me.duanbn.snailfish.util.Validation;
import me.duanbn.snailfish.util.Validation.ValidResult;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 命令总线.
 * 
 * @author zhilin
 * @author bingnan.dbn
 */
@Slf4j
public class CommandBus {

	/** 命令执行器注册表 */
	@Setter(AccessLevel.PRIVATE)
	private static CommandExecutorRegister commandExecutorRegister;

	public static class CommandExecutorInjector {
		@Autowired
		private CommandExecutorRegister commandExecutorRegister;

		@PostConstruct
		public void postConstruct() {
			CommandBus.setCommandExecutorRegister(commandExecutorRegister);
		}
	}

	/**
	 * 触发有个命令的执行.
	 * 
	 * @param <T>
	 * @param command
	 * @return
	 * @throws CommandException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Response> T dispatch(Command command) {
		Class commandClazz = command.getClass();
		if (!commandExecutorRegister.containCommandExecutor(commandClazz)) {
			throw new CommandException("没有找到对应的命令执行器, command class=" + commandClazz);
		}

		CommandInvocation commandInvocation = commandExecutorRegister.getCommandInvocation(commandClazz);

		ValidResult validResult = Validation.validateBean(command);
		if (validResult.hasErrors()) {
			Response commandResp = createResponse(commandInvocation, commandClazz);
			commandResp.setSuccess(false);
			commandResp.setCode(Response.CODE_PARAM_FAILURE);
			commandResp.setMessage(validResult.getErrors());
			return (T) commandResp;
		}

		Response commandResp = null;
		try {
			commandResp = commandInvocation.execute(command);
		} catch (CommandExecuteException e) {
			log.error("execute command error", e);
			commandResp = createResponse(commandInvocation, commandClazz);
			commandResp.setSuccess(false);
			if (StringUtil.isNotBlank(e.getCode())) {
				commandResp.setCode(e.getCode());
			} else {
				commandResp.setCode(Response.CODE_INTERNAL_ERROR);
			}
			commandResp.setMessage(e.getMessage());
		} catch (Exception e) {
			log.error("execute command error", e);
			commandResp = createResponse(commandInvocation, commandClazz);
			commandResp.setSuccess(false);
			commandResp.setCode(Response.CODE_INTERNAL_ERROR);
			commandResp.setMessage(e.getMessage());
		}

		CommandLog commandLog = new CommandLog();
		commandLog.setCommandType(command.getClass().getName());
		commandLog.setCommand(command);
		commandLog.setResponse(commandResp);

		log.info("{}", JSON.toJSONString(commandLog));

		return (T) commandResp;
	}

	public static <T extends Command> T createCommand(Class<T> commandClazz) {
		return ObjectUtil.create(commandClazz);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T extends Response> T createResponse(CommandInvocation commandInvocation, Class commandClazz) {
		Response commandResp = null;
		try {
			commandResp = (T) commandInvocation.getRespType().newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new CommandException("总线内部异常，创建返回值实例失败 clazz=" + commandClazz);
		}
		return (T) commandResp;
	}

	@Data
	private static class CommandLog {
		private String commandType;
		private Command command;
		private Response response;
	}

}