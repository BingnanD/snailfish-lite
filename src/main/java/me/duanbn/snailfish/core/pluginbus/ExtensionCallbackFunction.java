package me.duanbn.snailfish.core.pluginbus;

import java.util.function.Function;

/**
 * ExtensionCallbackFunction
 *
 * @author zhilin
 */
public interface ExtensionCallbackFunction<T extends Plugin, R> extends Function<T, R> {

	/**
	 * apply
	 *
	 * @param plugin
	 * @return
	 */
	@Override
	R apply(T plugin);

}
