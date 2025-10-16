package me.duanbn.snailfish.core.ruleengine;

import java.io.StringReader;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.YamlRuleDefinitionReader;

import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 抽象规则工厂.
 * 
 * @author bingnan.dbn
 */
public class RuleFactoryImpl implements RuleFactory {

	private Map<String, Rules> rulesCache = Maps.newConcurrentMap();

	@Override
	public Rule create(String ruleText, RuleListenerAdapter... ruleListeners) {
		try {
			return create(ruleText, null, ruleListeners);
		} catch (Exception e) {
			throw new RuntimeException(ruleText, e);
		}
	}

	@Override
	public Rule create(String ruleText, RulesEngineParameters parameters, RuleListenerAdapter... ruleListeners) {
		if (StringUtil.isBlank(ruleText)) {
			throw new IllegalArgumentException("规则内容不能为空");
		}

		String cacheKey = DigestUtils.md5Hex(ruleText);

		Rules rules = this.rulesCache.get(cacheKey);
		if (rules == null) {
			MVELRuleFactory mvELRuleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());
			StringReader ruleContentReader = null;
			try {
				ruleContentReader = new StringReader(ruleText);
				rules = mvELRuleFactory.createRules(ruleContentReader);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (ruleContentReader != null) {
					try {
						ruleContentReader.close();
					} catch (Exception e) {
					}
				}
			}
			this.rulesCache.put(cacheKey, rules);
		}

		DefaultRulesEngine rulesEngine = null;
		if (parameters != null) {
			rulesEngine = new DefaultRulesEngine(parameters);
		} else {
			rulesEngine = new DefaultRulesEngine();
		}

		Rule rule = new Rule(rulesEngine);

		// 注册执行结果的监听器.
		RuleResultListener ruleResultListener = new RuleResultListener();
		rulesEngine.registerRuleListener(ruleResultListener);

		// 注册链式调用监听器.
		RuleChainExecuteListener ruleChainExecuteListener = new RuleChainExecuteListener(rule.getAfterExeRuleMap());
		rulesEngine.registerRuleListener(ruleChainExecuteListener);

		// 注册用户自定义监听器.
		if (ruleListeners != null && ruleListeners.length > 0) {
			for (RuleListenerAdapter ruleListener : ruleListeners) {
				rulesEngine.registerRuleListener(ruleListener);
			}
		}

		// 初始化规则
		rule.init(rules);

		return rule;
	}

}