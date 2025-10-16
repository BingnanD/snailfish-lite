package me.duanbn.snailfish.core.ruleengine;

import org.jeasy.rules.core.RulesEngineParameters;

/**
 * 规则工厂.
 * 
 * @author bingnan.dbn
 */
public interface RuleFactory {

	/**
	 * 创建一个可执行的规则实例.
	 * 
	 * @param ruleText      规则内容
	 * @param ruleListeners 规则监听器.
	 * @return
	 */
	Rule create(String ruleText, RuleListenerAdapter... ruleListeners);

	Rule create(String ruleText, RulesEngineParameters parameters, RuleListenerAdapter... ruleListeners);

}