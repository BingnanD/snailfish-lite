package me.duanbn.snailfish.core.domain.pattern;

import java.util.function.Function;

/**
 * ExtensionCallbackFunction
 *
 * @author zhilin
 */
public interface DomainPatternCallbackFunction<T extends DomainPatternI, R> extends Function<T, R> {

	/**
	 * apply
	 *
	 * @param plugin
	 * @return
	 */
	@Override
	R apply(T plugin);

}
