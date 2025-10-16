package me.duanbn.snailfish.core.ruleengine;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

/**
 * easy rule listener adapter.
 * 
 * @author bingnan.dbn
 */
public class RuleListenerAdapter implements RuleListener {

	private Facts facts;

	protected RulesResult getRulesResult() {
		return facts.get(me.duanbn.snailfish.core.ruleengine.Rule.ALL_RESULT_KEY);
	}

	private RuleResult getRuleResult(Rule rule) {
		RulesResult easyRulesResult = facts.get(me.duanbn.snailfish.core.ruleengine.Rule.ALL_RESULT_KEY);
		return (RuleResult) easyRulesResult.get(rule.getName());
	}

	@Override
	public final boolean beforeEvaluate(Rule rule, Facts facts) {
		this.facts = facts;
		return doBeforeEvaluate(rule, facts);
	}

	@Override
	public final void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
		RuleResult ruleResult = getRuleResult(rule);
		ruleResult.evaluate(evaluationResult);

		if (evaluationResult) {
			evaluateSuccess(rule, facts, ruleResult);
		} else {
			evaluateFailure(rule, facts, ruleResult);
		}
	}

	@Override
	public final void beforeExecute(Rule rule, Facts facts) {
		RuleResult ruleResult = getRuleResult(rule);
		beforeExecute(rule, facts, ruleResult);
	}

	@Override
	public final void onSuccess(Rule rule, Facts facts) {
		RuleResult ruleResult = getRuleResult(rule);
		afterExecute(rule, facts, ruleResult);
	}

	@Override
	public final void onFailure(Rule rule, Facts facts, Exception exception) {
		RuleResult ruleResult = getRuleResult(rule);
		afterExecute(rule, facts, ruleResult);
	}

	protected boolean doBeforeEvaluate(Rule rule, Facts facts) {
		return true;
	}

	protected void evaluateSuccess(Rule rule, Facts facts, RuleResult ruleResult) {
	}

	protected void evaluateFailure(Rule rule, Facts facts, RuleResult ruleResult) {
	}

	protected void beforeExecute(Rule rule, Facts facts, RuleResult ruleResult) {
	}

	protected void afterExecute(Rule rule, Facts facts, RuleResult ruleResult) {
	}

}
