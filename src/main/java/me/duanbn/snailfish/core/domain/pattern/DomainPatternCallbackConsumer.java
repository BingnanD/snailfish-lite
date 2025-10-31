package me.duanbn.snailfish.core.domain.pattern;

import java.util.function.Consumer;

/**
 * ExtensionCallbackConsumer
 *
 * @author zhilin
 * @author shanwei
 */
public interface DomainPatternCallbackConsumer<T extends DomainPatternI> extends Consumer<T> {

	/**
	 * accept
	 *
	 * @param plugin
	 */
	@Override
	void accept(T plugin);

}
