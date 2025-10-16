package me.duanbn.snailfish.core.pluginbus;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.AccessLevel;
import lombok.Setter;
import me.duanbn.snailfish.api.Scenario;
import me.duanbn.snailfish.core.ruleengine.Rule;
import me.duanbn.snailfish.core.ruleengine.RulesResult;

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

	public static <T extends ExtensionPointI, R> R dispatch(Class<T> clazz, Scenario bizScenario,
			ExtensionCallbackFunction<T, R> function) {
		T findComponent = loadPlugin(clazz, bizScenario);

		if (findComponent == null) {
			throw new PluginException("not found plugin by " + clazz + " with " + bizScenario);
		}

		return function.apply(findComponent);
	}

	public static <T extends ExtensionPointI> void dispatchVoid(Class<T> clazz, Scenario bizScenario,
			ExtensionCallbackConsumer<T> consumer) {
		T findComponent = loadPlugin(clazz, bizScenario);

		if (findComponent == null) {
			throw new PluginException("not found plugin by " + clazz + " with " + bizScenario);
		}

		consumer.accept(findComponent);
	}

	public static <T> T loadPlugin(Class<? extends ExtensionPointI> targetClazz, Scenario bizScenario) {
		T extension = (T) pluginRegister.getPlugin(pluginId(targetClazz, bizScenario));
		return extension;
	}

	public static String pluginId(Class<? extends ExtensionPointI> targetClazz, Scenario bizScenario) {
		Rule extMountRule = pluginRegister.loadMountRule(targetClazz);

		if (extMountRule == null) {
			throw new PluginException("[" + targetClazz + "] 插件配置错误，没有找到挂载规则");
		}

		extMountRule.putFactValue("plugin", bizScenario);
		RulesResult ruleResult = extMountRule.fire();

		return ruleResult.getAsString(PluginRegister.EXTENSION);
	}

}
