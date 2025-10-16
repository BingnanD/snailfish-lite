package me.duanbn.snailfish.core.pluginbus;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.RulesEngineParameters;
import org.springframework.beans.factory.annotation.Autowired;

import me.duanbn.snailfish.core.RegisterI;
import me.duanbn.snailfish.core.ruleengine.Rule;
import me.duanbn.snailfish.core.ruleengine.RuleFactory;
import me.duanbn.snailfish.core.ruleengine.RuleListenerAdapter;
import me.duanbn.snailfish.core.ruleengine.RuleResult;
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
	private Map<String, ExtensionPointI> pluginMap = Maps.newConcurrentMap();

	/** 扩展点加载规则 */
	protected Map<Class<?>, String> mountRuleContentMap = Maps.newConcurrentMap();
	public static final String EXTENSION = "extension";
	public static final String INFO_PATH = "META-INF/plugin/";
	@Autowired
	private RuleFactory ruleFty;

	// 插件加载器.
	@Resource(name = "internalPluginLoader")
	private PluginLoaderI internalPluginLoader;

	public boolean hasPlugin(String value) {
		return this.pluginMap.containsKey(value);
	}

	public void putPlugin(String code, ExtensionPointI extension) {
		this.pluginMap.put(code, extension);
	}

	public ExtensionPointI getPlugin(String code) {
		if (StringUtil.isBlank(code)) {
			return null;
		}
		return this.pluginMap.get(code);
	}

	public boolean hasPluginMountRuleContent(Class<?> extensionClazz) {
		return this.mountRuleContentMap.containsKey(extensionClazz);
	}

	public void putPluginMountRuleContent(Class<?> extensionClazz, String mountRule) {
		this.mountRuleContentMap.put(extensionClazz, mountRule);
	}

	public String getPluginMountRuleContent(Class<?> extensionClazz) {
		return this.mountRuleContentMap.get(extensionClazz);
	}

	@Override
	public void doRegistration(Class<?> extensionClazz) {
		if (!ExtensionPointI.class.isAssignableFrom(extensionClazz)) {
			throw new IllegalStateException(String.format(
					"There should be at leads one interfaces implemented by given clazz: %s",
					extensionClazz.getName()));
		}

		List<Class> extensionPointClasses = getExtensionPointClasses(extensionClazz);
		for (Class extensionPoint : extensionPointClasses) {
			try {
				// 加载扩展挂载规则.
				if (!this.hasPluginMountRuleContent(extensionPoint)) {
					this.putPluginMountRuleContent(extensionPoint, loadMountRuleContent(extensionPoint));
				}

				// 加载进程内插件
				internalPluginLoader.loadPlugin(this, extensionPoint);
			} catch (Exception e) {
				throw new PluginException(
						"Load plugin failed:" + extensionClazz + ", extension point is " + extensionPoint, e);
			}
		}
	}

	protected String loadMountRuleContent(Class<?> extPtClazz) throws Exception {
		String mountRuleFilePath = INFO_PATH + extPtClazz.getCanonicalName();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(mountRuleFilePath);
		if (inputStream == null) {
			throw new PluginException(
					String.format("Load plugin rule failed, the config file \"%s\" not exist", mountRuleFilePath));
		}
		String mountRuleContent = IOUtils.toString(inputStream, "UTF-8");
		IOUtils.closeQuietly(inputStream);
		return mountRuleContent;
	}

	protected Rule loadMountRule(Class<?> extPtClazz) {
		String mountRuleContent = this.getPluginMountRuleContent(extPtClazz);

		// 创建扩展挂载规则.
		RulesEngineParameters engineParameters = new RulesEngineParameters();
		// 满足第一个执行成功的规则后不再执行后续规则.
		engineParameters.skipOnFirstAppliedRule(true);
		Rule rule = this.ruleFty.create(mountRuleContent, engineParameters, new RuleListenerAdapter() {
			@Override
			public void evaluateSuccess(org.jeasy.rules.api.Rule rule, Facts facts, RuleResult ruleResult) {
				getRulesResult().put(EXTENSION, rule.getName());
			}
		});

		return rule;
	}

	protected List<Class> getExtensionPointClasses(Class<?> extPtClazz) {
		List<Class> extPtList = Lists.newArrayList();

		Class[] interfaces = ObjectUtil.getInterfaces(extPtClazz, true);
		if (null == interfaces || 0 == interfaces.length) {
			throw new PluginException(String.format(
					"There should be at leads one interfaces implemented by given clazz: %s", extPtClazz.getName()));
		}
		for (Class it : interfaces) {
			if (ExtensionPointI.class.isAssignableFrom(it)) {
				extPtList.add(it);
			}
		}

		if (extPtList.isEmpty())
			throw new PluginException("There should be at least one interface with ExtPt or Driver suffix.");

		return extPtList;
	}

}
