package me.duanbn.snailfish.core;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.Getter;
import me.duanbn.snailfish.core.commandbus.CommandExecutorRegister;
import me.duanbn.snailfish.core.commandbus.annotations.CommandExecutor;
import me.duanbn.snailfish.core.domain.DomainE;
import me.duanbn.snailfish.core.domain.DomainObjectResgister;
import me.duanbn.snailfish.core.domain.DomainV;
import me.duanbn.snailfish.core.domain.annotations.DomainFactory;
import me.duanbn.snailfish.core.domain.annotations.DomainRepository;
import me.duanbn.snailfish.core.domain.annotations.DomainService;
import me.duanbn.snailfish.core.domain.pattern.DomainPatternRegister;
import me.duanbn.snailfish.core.eventbus.EventHandlerRegister;
import me.duanbn.snailfish.core.eventbus.annotations.EventHandler;
import me.duanbn.snailfish.core.pluginbus.PluginRegister;
import me.duanbn.snailfish.core.pluginbus.annotations.Plugin;
import me.duanbn.snailfish.util.collection.Lists;

/**
 * 注册器工厂.
 * 
 * @author zhilin
 * @author bingnan.dbn
 */
public class RegisterFactory {

	/** domain object register */
	@Resource
	@Getter
	private DomainObjectResgister domainRegister;
	/** 领域模式注册器 */
	@Resource
	private DomainPatternRegister domainPatternRegister;
	/** 命令注册器 */
	@Autowired
	private CommandExecutorRegister commandRegister;
	/** 事件注册器 */
	@Autowired
	private EventHandlerRegister eventRegister;
	/** 插件注册器 */
	@Autowired
	@Qualifier("pluginRegister")
	private PluginRegister pluginRegister;

	public RegisterFactory() {
	}

	public List<RegisterI> getRegister(Class<?> clazz) {
		List<RegisterI> registers = Lists.newArrayList();

		if (DomainE.class.isAssignableFrom(clazz) || DomainV.class.isAssignableFrom(clazz)) {
			registers.add(domainRegister);
		}

		DomainFactory domainFactoryAnno = clazz.getAnnotation(DomainFactory.class);
		DomainRepository domainRepositoryAnno = clazz.getAnnotation(DomainRepository.class);
		DomainService domainServiceAnno = clazz.getAnnotation(DomainService.class);
		if (domainFactoryAnno != null || domainRepositoryAnno != null || domainServiceAnno != null) {
			registers.add(domainPatternRegister);
		}

		// command bus
		CommandExecutor commandExecutorAnno = clazz.getAnnotation(CommandExecutor.class);
		if (commandExecutorAnno != null) {
			registers.add(commandRegister);
		}

		// event bus
		EventHandler eventHandlerAnno = clazz.getAnnotation(EventHandler.class);
		if (eventHandlerAnno != null) {
			registers.add(eventRegister);
		}

		// plugin bus
		Plugin pluginAnno = clazz.getAnnotation(Plugin.class);
		if (null != pluginAnno) {
			registers.add(pluginRegister);
		}

		return registers;
	}

}
