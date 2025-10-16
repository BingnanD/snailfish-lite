package me.duanbn.snailfish.core.commandbus;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.api.command.Command;
import me.duanbn.snailfish.api.dto.Response;
import me.duanbn.snailfish.core.Bootstrap;
import me.duanbn.snailfish.core.RegisterI;
import me.duanbn.snailfish.core.commandbus.annotations.CommandExecutor;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 命令注册器.
 * 
 * @author bingnan.dbn
 */
@Slf4j
public class CommandExecutorRegister implements RegisterI, ApplicationContextAware {

	private ApplicationContext appCtx;

	/** 命令和命令执行器映射 */
	private Map<Class<? extends Command>, CommandInvocation<? extends Response>> commandExecutorMap = Maps
			.newConcurrentMap();

	@SuppressWarnings("rawtypes")
	public boolean containCommandExecutor(Class clazz) {
		return commandExecutorMap.containsKey(clazz);
	}

	public CommandInvocation<? extends Response> getCommandInvocation(Class<Command> clazz) {
		return commandExecutorMap.get(clazz);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doRegistration(Class<?> clazz) throws CommandException {
		// reg command executor
		if (CommandExecutorI.class.isAssignableFrom(clazz)) {
			CommandExecutor annotation = clazz.getAnnotation(CommandExecutor.class);
			if (annotation == null) {
				throw new CommandException("command executor must annotation by @CommandExecutor");
			}

			// init command executor
			CommandExecutorI<Command, Response> commandExecutor = (CommandExecutorI<Command, Response>) this.appCtx
					.getBean(clazz);

			// init command interceptor
			List<CommandInterceptorI> interceptorInstances = Lists.newArrayList();
			Class<? extends CommandInterceptorI<? extends Command, ? extends Response>>[] interceptors = annotation
					.interceptors();
			if (interceptors != null && interceptors.length > 0) {
				for (Class<? extends CommandInterceptorI<? extends Command, ? extends Response>> interceptorClass : interceptors) {
					if (interceptorClass == null) {
						continue;
					}

					CommandInterceptorI<? extends Command, ? extends Response> interceptorInstance = this.appCtx
							.getBean(interceptorClass);
					interceptorInstances.add(interceptorInstance);
				}
			}

			// get command class by command executor generic type
			Class<Command> commandClazz = getRegisterObjectGenricType(clazz, 0);

			CommandInvocation<? extends Response> commandInvocation = CommandInvocation.newInstance();
			commandInvocation.setCommandExecutor(commandExecutor);
			commandInvocation.setCommandInterceptors(interceptorInstances);

			commandExecutorMap.put(commandClazz, commandInvocation);

			Bootstrap bootstrap = this.appCtx.getBean(Bootstrap.class);
			if (bootstrap.isEnableLog())
				log.info("register command [{}] [{}] done", commandClazz.getSimpleName(),
						commandExecutor.getClass().getSimpleName());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}

}
