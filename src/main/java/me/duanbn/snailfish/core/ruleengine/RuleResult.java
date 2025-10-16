package me.duanbn.snailfish.core.ruleengine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 单个规则执行结果
 * 
 * @author bingnan.dbn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RuleResult extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	private static final String EVALUATE_KEY = "evaluate";

	/** 规则名称 */
	private String ruleName;
	/** 规则结果属性 */
	private Map<String, Object> attr = Maps.newLinkedHashMap();
	/** 规则结果数据 */
	private Map<String, Object> data = Maps.newLinkedHashMap();

	public void evaluate(boolean value) {
		putAttr(EVALUATE_KEY, value);
	}

	public boolean evalutate() {
		return (Boolean) getAttr(EVALUATE_KEY);
	}

	public void putAttr(String key, Object value) {
		this.attr.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttr(String key) {
		return (T) this.attr.get(key);
	}

	public String getAttrAsString(String key) {
		Object value = this.attr.get(key);
		if (value == null) {
			return null;
		}

		return String.valueOf(value);
	}

	public Integer getAttrAsInteger(String key) {
		Object value = this.attr.get(key);
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

	public void putData(String key, Object value) {
		this.data.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T) this.data.get(key);
	}

	public String getDataAsString(String key) {
		Object value = this.data.get(key);
		if (value == null) {
			return null;
		}

		return String.valueOf(value);
	}

	public Long getDataAsLong(String key) {
		Object value = this.data.get(key);
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

	public Integer getDataAsInteger(String key) {
		Object value = this.data.get(key);
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

	@Override
	public RuleResult clone() {
		RuleResult value = new RuleResult(this.ruleName);
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

	public RuleResult() {
	}

	public RuleResult(String ruleName) {
		this.ruleName = ruleName;
	}

	public static RuleResult valueOf(String ruleName) {
		return new RuleResult(ruleName);
	}

}
