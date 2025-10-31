package me.duanbn.snailfish.core.pluginbus;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import me.duanbn.snailfish.core.RegisterI;
import me.duanbn.snailfish.util.ObjectUtil;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 插件注册器.
 * 
 * @author bingnan.dbn
 */
@SuppressWarnings({ "rawtypes", "unchecked", })
public class PluginRegister implements RegisterI {

	/** 插件 */
	private Map<String, Pluginable> pluginMap = Maps.newConcurrentMap();
	private Map<Class<? extends Pluginable>, List<String>> pluginNamesMap = Maps.newConcurrentMap();

	// 插件加载器.
	@Resource(name = "internalPluginLoader")
	private PluginLoaderI internalPluginLoader;

	public boolean hasPlugin(Class<? extends Pluginable> pluginableClazz) {
		return this.pluginNamesMap.containsKey(pluginableClazz);
	}

	public boolean hasPlugin(String value) {
		return this.pluginMap.containsKey(value);
	}

	public void putPlugin(String pluginName, Pluginable plugin) {
		this.pluginMap.put(pluginName, plugin);
	}

	public void putPlugin(Class<? extends Pluginable> pluginableClazz, List<String> pluginName) {
		this.pluginNamesMap.put(pluginableClazz, pluginName);
	}

	public List<String> getPluginNames(Class<? extends Pluginable> pluginableClazz) {
		return this.pluginNamesMap.get(pluginableClazz);
	}

	public Pluginable getPlugin(String pluginName) {
		if (StringUtil.isBlank(pluginName)) {
			return null;
		}
		return this.pluginMap.get(pluginName);
	}

	@Override
	public void doRegistration(Class<?> pluginClazz) {
		if (!Pluginable.class.isAssignableFrom(pluginClazz)) {
			throw new IllegalStateException(String.format(
					"There should be at leads one interfaces implemented by given clazz: %s",
					pluginClazz.getName()));
		}

		List<Class> pluginableClasses = getPluginableClasses(pluginClazz);
		for (Class pluginable : pluginableClasses) {
			try {
				// 加载进程内插件
				internalPluginLoader.loadPlugin(this, pluginable);
			} catch (Exception e) {
				throw new PluginException(
						"Load plugin failed:" + pluginClazz + ", plugin point is " + pluginable, e);
			}
		}
	}

	protected List<Class> getPluginableClasses(Class<?> extPtClazz) {
		List<Class> extPtList = Lists.newArrayList();

		Class[] interfaces = ObjectUtil.getInterfaces(extPtClazz, true);
		if (null == interfaces || 0 == interfaces.length) {
			throw new PluginException(String.format(
					"There should be at leads one interfaces implemented by given clazz: %s", extPtClazz.getName()));
		}
		for (Class it : interfaces) {
			if (Pluginable.class.isAssignableFrom(it)) {
				extPtList.add(it);
			}
		}

		if (extPtList.isEmpty())
			throw new PluginException("There should be at least one interface with ExtPt or Driver suffix.");

		return extPtList;
	}

}
