package me.duanbn.snailfish.core.pluginbus;

import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.Setter;

/**
 * 插件总线
 * 
 * @author zhilin
 * @author bingnan.dbn
 *
 */
@SuppressWarnings({ "unchecked" })
public class PluginBus {

	@Setter(AccessLevel.PRIVATE)
	protected static PluginRegister pluginRegister;

	public static class PluginBusInjector {
		@Resource(name = "pluginRegister")
		private PluginRegister pluginRegister;

		@PostConstruct
		public void postConstruct() {
			PluginBus.setPluginRegister(pluginRegister);
		}
	}

	public static List<String> getPluginNames(Class<? extends Pluginable> pluginableClazz) {
		return pluginRegister.getPluginNames(pluginableClazz);
	}

	public static <T extends Pluginable, R> R dispatch(Class<T> clazz,
			PluginSelector selector,
			ExtensionCallbackFunction<T, R> function) {
		return dispatch(clazz, selector.select(), function);
	}

	public static <T extends Pluginable, R> R dispatch(Class<T> clazz, String pluginName,
			ExtensionCallbackFunction<T, R> function) {
		T findComponent = (T) pluginRegister.getPlugin(pluginName);

		if (findComponent == null) {
			throw new PluginException("not found plugin by " + clazz + " with " + pluginName);
		}

		return function.apply(findComponent);
	}

	public static <T extends Pluginable> void dispatchVoid(Class<T> clazz, PluginSelector selector,
			ExtensionCallbackConsumer<T> consumer) {
		dispatchVoid(clazz, selector.select(), consumer);
	}

	public static <T extends Pluginable> void dispatchVoid(Class<T> clazz, String pluginName,
			ExtensionCallbackConsumer<T> consumer) {
		T findComponent = (T) pluginRegister.getPlugin(pluginName);

		if (findComponent == null) {
			throw new PluginException("not found plugin by " + clazz + " with " + pluginName);
		}

		consumer.accept(findComponent);
	}

	public static interface PluginSelector {
		String select();
	}

}
