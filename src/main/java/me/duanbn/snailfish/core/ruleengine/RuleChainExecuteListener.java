package me.duanbn.snailfish.core.ruleengine;

import java.util.List;
import java.util.Map;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.springframework.util.CollectionUtils;

import lombok.Getter;

/**
 * 链式规则调用.
 * 
 * 当执行完某个规则之后继续执行下一个规则.
 * 
 * @author shanwei
 *
 */
public class RuleChainExecuteListener extends RuleListenerAdapter {

	@Getter
	private Map<String, List<org.jeasy.rules.api.Rule>> afterExeRuleMap;

	public RuleChainExecuteListener(Map<String, List<org.jeasy.rules.api.Rule>> afterExeRuleMap) {
		this.afterExeRuleMap = afterExeRuleMap;
	}

	@Override
	protected void afterExecute(Rule rule, Facts facts, RuleResult ruleResult) {
		String ruleName = rule.getName();

		List<Rule> afterRuleList = this.afterExeRuleMap.get(ruleName);

		if (CollectionUtils.isEmpty(afterRuleList)) {
			return;
		}

		for (Rule afterRule : afterRuleList) {
			if (afterRule.evaluate(facts)) {
				try {
					afterRule.execute(facts);
				} catch (Exception e) {
					throw new RuleException("execute after rule error name=" + afterRule.getName(), e);
				}
			}
		}
	}

}
