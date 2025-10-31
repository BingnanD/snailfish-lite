package me.duanbn.snailfish.core.pluginbus;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.Bootstrap.BootstrapAttribute;
import me.duanbn.snailfish.util.collection.Lists;

@Slf4j
public class InternalPluginLoader implements PluginLoaderI, ApplicationContextAware {

	private ApplicationContext appCtx;

	@Override
	public void loadPlugin(PluginRegister pluginRegister, Class<Plugin> pluginable)
			throws PluginLoaderException {
		// 加载扩展点.
		Map<String, ?> pluginInstanceMap = this.appCtx.getBeansOfType(pluginable);
		if (CollectionUtils.isEmpty(pluginInstanceMap)) {
			return;
		}

		for (Object pluginInstance : pluginInstanceMap.values()) {
			PluginName pluginAnno = pluginInstance.getClass().getAnnotation(PluginName.class);
			if (pluginAnno == null) {
				continue;
			}

			String pluginName = pluginAnno.value();

			if (pluginRegister.hasPlugin(pluginName)) {
				continue;
			}

			pluginRegister.putPlugin(pluginName, (Plugin) pluginInstance);

		}

		if (!pluginRegister.hasPlugin(pluginable)) {
			List<String> pluginNames = Lists.newArrayList(pluginInstanceMap.keySet());
			pluginRegister.putPlugin(pluginable, pluginNames);

			BootstrapAttribute bootstrapAttr = this.appCtx.getBean(BootstrapAttribute.class);
			if (bootstrapAttr.isEnableLog() && !pluginNames.isEmpty())
				log.info("register plugin [{}] {} done", pluginable.getSimpleName(), pluginNames);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}

}
