package me.duanbn.snailfish.core.pluginbus;

import java.util.function.Consumer;

/**
 * ExtensionCallbackConsumer
 *
 * @author zhilin
 * @author shanwei
 */
public interface ExtensionCallbackConsumer<T extends ExtensionPointI> extends Consumer<T> {

	/**
	 * accept
	 *
	 * @param extension
	 */
	@Override
	void accept(T extension);

}
