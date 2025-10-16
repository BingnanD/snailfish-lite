package me.duanbn.snailfish.core.commandbus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.api.command.Command;
import me.duanbn.snailfish.api.dto.MultiResponse;
import me.duanbn.snailfish.api.dto.Response;

import lombok.Setter;

/**
 * 命令执行器内部实现
 * 
 * @author zhilin
 * @author bingnan.dbn
 */
@SuppressWarnings({ "rawtypes", "unchecked", })
public class CommandInvocation<R extends Response> {

	/** 命令执行器 */
	@Setter
	private CommandExecutorI commandExecutor;
	/** 命令拦截器 */
	@Setter
	private List<CommandInterceptorI> commandInterceptors;

	/**
	 * 命令执行
	 * 
	 * @return
	 */
	public <T extends Response> T execute(Command command) throws Exception {

		Class<T> _getGenricType = getRespType();

		T respIns = (T) _getGenricType.newInstance();
		respIns.setSuccess(true);
		respIns.setMessage("success");

		// execute pre interceptor
		if (!CollectionUtils.isEmpty(commandInterceptors)) {
			for (int i = 0; i < commandInterceptors.size(); i++) {
				CommandInterceptorI commandInterceptor = commandInterceptors.get(i);
				boolean isContinue = commandInterceptor.preHandle(command, respIns);
				if (!isContinue) {
					return (T) respIns;
				}
			}
		}

		// execute command
		this.commandExecutor.execute(command, respIns);

		// execute post interceptor
		if (!CollectionUtils.isEmpty(commandInterceptors)) {
			for (int i = commandInterceptors.size() - 1; i >= 0; i--) {
				CommandInterceptorI commandInterceptor = commandInterceptors.get(i);
				commandInterceptor.postHandle(command, respIns);
			}
		}

		// tidy response, set total, page property and so on.
		if (respIns instanceof MultiResponse) {
			MultiResponse multiResp = (MultiResponse) respIns;
			if (multiResp.getData() != null) {
				multiResp.setTotal(multiResp.getData().size());
			}
		}

		return (T) respIns;
	}

	public static CommandInvocation<? extends Response> newInstance() {
		return new CommandInvocation();
	}

	public Class getRespType() {
		Class commandExecutorClass = getTargetClass(this.commandExecutor);
		Type genType = commandExecutorClass.getGenericInterfaces()[0];
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return (Class) ((ParameterizedType) params[1]).getRawType();
	}

	public Class<?> getTargetClass(CommandExecutorI commandExecutor) {
		if (!AopUtils.isAopProxy(commandExecutor)) {
			return commandExecutor.getClass();
		} else if (AopUtils.isJdkDynamicProxy(commandExecutor)) {
			try {
				return ((Advised) commandExecutor).getTargetSource().getTarget().getClass();
			} catch (Exception ex) {
				throw new RuntimeException("Failed to get original class from JDK Proxy", ex);
			}
		} else {
			try {
				Class<?> clazz = commandExecutor.getClass();
				while (clazz.getName().contains("$$")) {
					clazz = clazz.getSuperclass();
				}
				return clazz;
			} catch (Exception ex) {
				throw new RuntimeException("Failed to get original class from CGLIB Proxy", ex);
			}
		}
	}

}
