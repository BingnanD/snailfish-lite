package me.duanbn.snailfish.core.commandbus;

/**
 * 命令拦截器.
 * 
 * @author bingnan.dbn
 */
public interface CommandInterceptorI<C, R> {

	/**
	 * 命令被执行前执行此方法.
	 * 
	 * @param command 命令
	 */
	default boolean preHandle(C command, R response) {
		return true;
	}

	/**
	 * 命令被执行后执行此方法.
	 * 
	 * @param command  命令
	 * @param response 命令执行结果
	 */
	default void postHandle(C command, R response) {
	}

}
