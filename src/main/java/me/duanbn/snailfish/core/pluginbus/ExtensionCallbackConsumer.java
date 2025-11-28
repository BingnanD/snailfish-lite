package me.duanbn.snailfish.core.pluginbus;

import java.util.function.Consumer;

/**
 * ExtensionCallbackConsumer
 *
 * @author zhilin
 * @author shanwei
 */
public interface ExtensionCallbackConsumer<T extends Pluginable> extends Consumer<T> {

	/**
	 * accept
	 *
	 * @param plugin
	 */
	@Override
	void accept(T plugin);

}
