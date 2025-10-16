package me.duanbn.snailfish.core.pluginbus;

import java.util.function.Function;

/**
 * ExtensionCallbackFunction
 *
 * @author zhilin
 */
public interface ExtensionCallbackFunction<T extends ExtensionPointI, R> extends Function<T, R> {

	/**
	 * apply
	 *
	 * @param extension
	 * @return
	 */
	@Override
	R apply(T extension);

}
