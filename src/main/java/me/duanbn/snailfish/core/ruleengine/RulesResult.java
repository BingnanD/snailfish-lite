package me.duanbn.snailfish.core.ruleengine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.util.collection.Lists;

/**
 * 整体规则执行结果
 * 
 * @author bingnan.dbn
 */
public class RulesResult extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public List<RuleResult> getRuleResult() {
		List<RuleResult> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(this.values())) {
			return result;
		}

		for (Object t : this.values()) {
			if (t instanceof RuleResult) {
				result.add((RuleResult) t);
			}
		}

		return result;
	}

	public List<RuleResult> getEvaluateSuccessRuleResult() {
		List<RuleResult> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(this.values())) {
			return result;
		}

		for (Object t : this.values()) {
			if (t instanceof RuleResult && ((RuleResult) t).evalutate()) {
				result.add((RuleResult) t);
			}
		}

		return result;
	}

	public List<RuleResult> getEvaluateFailureRuleResult() {
		List<RuleResult> result = Lists.newArrayList();

		if (CollectionUtils.isEmpty(this.values())) {
			return result;
		}

		for (Object t : this.values()) {
			if (t instanceof RuleResult && !((RuleResult) t).evalutate()) {
				result.add((RuleResult) t);
			}
		}

		return result;
	}

	public static RulesResult valueOf() {
		return new RulesResult();
	}

	@Override
	public RulesResult clone() {
		RulesResult value = new RulesResult();
		for (Map.Entry<String, Object> entry : this.entrySet()) {
			value.put(entry.getKey(), entry.getValue());
		}
		return value;
	}

	public Date getAsDate(String key, String format) {
		try {
			return new SimpleDateFormat(format).parse(getAsString(key));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public String getAsString(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}

		return String.valueOf(value);
	}

	public Short getAsShort(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return Short.valueOf((String) value);
		} else if (value instanceof Number) {
			return ((Number) value).shortValue();
		}

		throw new ClassCastException(value + " conver to Short");
	}

	public Integer getAsInteger(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return Integer.valueOf((String) value);
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		throw new ClassCastException(value + " covert to Integer");
	}

	public Long getAsLong(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return Long.valueOf((String) value);
		} else if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		throw new ClassCastException(value + " covert to Long");
	}

	public Float getAsFloat(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return Float.valueOf((String) value);
		} else if (value instanceof Number) {
			return ((Number) value).floatValue();
		}

		throw new ClassCastException(value + " covert to Float");
	}

	public Double getAsDouble(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return Double.valueOf((String) value);
		} else if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}

		throw new ClassCastException(value + " covert to Double");
	}

	public Map<String, Object> getAsMap() {
		return this;
	}

}
