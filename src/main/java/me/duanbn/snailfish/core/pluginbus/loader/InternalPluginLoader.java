package me.duanbn.snailfish.core.pluginbus.loader;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.Bootstrap.BootstrapAttribute;
import me.duanbn.snailfish.core.pluginbus.ExtensionPointI;
import me.duanbn.snailfish.core.pluginbus.PluginLoaderException;
import me.duanbn.snailfish.core.pluginbus.PluginLoaderI;
import me.duanbn.snailfish.core.pluginbus.PluginRegister;
import me.duanbn.snailfish.core.pluginbus.annotations.Plugin;
import me.duanbn.snailfish.util.collection.Lists;

@Slf4j
public class InternalPluginLoader implements PluginLoaderI, ApplicationContextAware {

	private ApplicationContext appCtx;

	@Override
	public void loadPlugin(PluginRegister pluginRegister, Class<ExtensionPointI> extensionPoint)
			throws PluginLoaderException {
		// 加载扩展点.
		Map<String, ?> extensionInstanceMap = this.appCtx.getBeansOfType(extensionPoint);
		if (CollectionUtils.isEmpty(extensionInstanceMap)) {
			return;
		}

		List<String> pluginIds = Lists.newArrayList();

		for (Object extensionInstance : extensionInstanceMap.values()) {
			Plugin pluginAnno = extensionInstance.getClass().getAnnotation(Plugin.class);
			if (pluginAnno == null) {
				continue;
			}

			String pluginId = pluginAnno.value();

			if (pluginRegister.hasPlugin(pluginId)) {
				continue;
			}

			pluginRegister.putPlugin(pluginId, (ExtensionPointI) extensionInstance);
			pluginIds.add(pluginId);
		}

		BootstrapAttribute bootstrapAttr = this.appCtx.getBean(BootstrapAttribute.class);
		if (bootstrapAttr.isEnableLog() && !pluginIds.isEmpty())
			log.info("register plugin [{}] {} done", extensionPoint.getSimpleName(), pluginIds);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}

}
