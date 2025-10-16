package me.duanbn.snailfish.core.ruleengine;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 * 用来处理规则结果的监听器. 系统级监听器，业务逻辑无需感知.
 * 
 * @author bingnan.dbn
 */
public class RuleResultListener extends RuleListenerAdapter {

	@Override
	public boolean doBeforeEvaluate(Rule rule, Facts facts) {
		// 获取一组规则的执行结果.
		RulesResult easyRulesResult = facts.get(me.duanbn.snailfish.core.ruleengine.Rule.ALL_RESULT_KEY);

		// 创建单个规则的执行结果.
		RuleResult eachRuleResult = RuleResult.valueOf(rule.getName());

		easyRulesResult.put(rule.getName(), eachRuleResult);

		// 放在facts中使结果在可以规则内容中被使用
		facts.remove(me.duanbn.snailfish.core.ruleengine.Rule.EACH_RESULT_KEY);
		facts.put(me.duanbn.snailfish.core.ruleengine.Rule.EACH_RESULT_KEY, eachRuleResult);

		return true;
	}

}
