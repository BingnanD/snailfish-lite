package me.duanbn.snailfish.core.ruleengine;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;

import lombok.Getter;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 表示一个规则. <b>这是一个非线程安全的类，同一个实例误在多线程中使用.</b>
 * 
 * @author bingnan.dbn
 */
public class Rule {

	/** 所有规则共享的结果key */
	public static final String ALL_RESULT_KEY = "$$";
	/** 单个规则的结果key */
	public static final String EACH_RESULT_KEY = "$";
	/** 查找后置规则正则表达式 */
	public static final String AFTERRULE_PARTTERN = "^\\w+@@\\w+$";

	/** easy rule facts */
	protected Facts facts;
	/** easy rule engine ref */
	protected DefaultRulesEngine rulesEngine;

	/** easyrule一组规则中的映射 */
	protected Map<String, org.jeasy.rules.api.Rule> ruleMap;

	/** 某个规则触发之后的规则 */
	@Getter
	private Map<String, List<org.jeasy.rules.api.Rule>> afterExeRuleMap;

	/**
	 * 创建规则实例
	 * 
	 * @param ruleContent yaml格式
	 */
	Rule(DefaultRulesEngine rulesEngine) {
		this.ruleMap = Maps.newLinkedHashMap();
		this.rulesEngine = rulesEngine;
		this.afterExeRuleMap = Maps.newHashMap();
	}

	public void init(Rules rules) {
		this.ruleMap.clear();
		this.facts = new Facts();
		for (org.jeasy.rules.api.Rule rule : rules) {
			String ruleName = rule.getName();
			if (Pattern.matches(AFTERRULE_PARTTERN, ruleName)) {
				String targetRuleName = ruleName.substring(0, ruleName.indexOf("@@"));
				List<org.jeasy.rules.api.Rule> afterRuleList = this.afterExeRuleMap.get(targetRuleName);
				if (afterRuleList == null) {
					afterRuleList = Lists.newArrayList();
					this.afterExeRuleMap.put(targetRuleName, afterRuleList);
				}
				afterRuleList.add(rule);
			} else {
				this.ruleMap.put(ruleName, rule);
			}
		}
	}

	/**
	 * 判断是否包含某个规则.
	 * 
	 * @param ruleName 规则名称.
	 * @return
	 */
	public boolean hasRule(String ruleName) {
		return this.ruleMap.containsKey(ruleName);
	}

	/**
	 * 执行规则.
	 * 
	 * @param name 规则名称.
	 * @return
	 */
	public RuleResult fire(String name) {
		org.jeasy.rules.api.Rule rule = this.ruleMap.get(name);
		if (rule == null) {
			throw new RuleException("没有找到相关规则描述 name=" + name);
		}

		Rules rules = new Rules();
		rules.register(rule);

		// 规则执行之前先初始化整体规则的结果.
		facts.put(ALL_RESULT_KEY, RulesResult.valueOf());

		this.rulesEngine.fire(rules, facts);

		RulesResult easyRulesResult = facts.get(ALL_RESULT_KEY);

		return (RuleResult) easyRulesResult.get(name);
	}

	/**
	 * 执行规则.
	 * 
	 * @return
	 */
	public RulesResult fire() {
		// 规则执行之前先初始化整体规则的结果.
		facts.put(ALL_RESULT_KEY, RulesResult.valueOf());

		Rules rules = new Rules();
		for (org.jeasy.rules.api.Rule rule : this.ruleMap.values()) {
			rules.register(rule);
		}

		this.rulesEngine.fire(rules, facts);

		return facts.get(ALL_RESULT_KEY);
	}

	public Facts getRuleFacts() {
		return this.facts;
	}

	/**
	 * clean facts
	 */
	public void cleanFact() {
		this.facts = new Facts();
	}

	/**
	 * 设置facts
	 * 
	 * @param key
	 * @param value
	 */
	public void putFactValue(String key, Object value) {
		facts.put(key, value);
	}

	/**
	 * 去除facts.
	 * 
	 * @param key
	 */
	public void removeFactValue(String key) {
		facts.remove(key);
	}

}
