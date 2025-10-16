package me.duanbn.snailfish.core.orm.rdb;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import me.duanbn.snailfish.core.orm.rdb.Query.QueryOpt;
import me.duanbn.snailfish.core.orm.rdb.QueryDefaultImpl.ConditionRelation;
import me.duanbn.snailfish.core.orm.rdb.QueryDefaultImpl.WhereSQL;
import me.duanbn.snailfish.core.orm.rdb.dialect.Dialect;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 查询条件.
 *
 * @author duanbn
 */
public class QueryCondition {

	private Dialect dialect;

	/**
	 * 条件字段.
	 */
	private String field;
	/**
	 * 条件值.
	 */
	private Object value;
	/**
	 * 条件枚举.
	 */
	private QueryOpt opt;

	/**
	 * 保存or查询条件.
	 */
	private QueryCondition[] orCond;

	/**
	 * 保存and查询条件.
	 */
	private QueryCondition[] andCond;

	/**
	 * 在一个Query中的条件
	 */
	private ConditionRelation conditionRelation;

	/**
	 * 构造方法. 防止调用者直接创建此对象.
	 */
	private QueryCondition() {
	}

	/**
	 * 构造方法.
	 *
	 * @param field 条件字段
	 * @param value 条件值
	 * @param opt   条件枚举
	 */
	@SuppressWarnings("rawtypes")
	private QueryCondition(String field, Object value, QueryOpt opt) {
		if (StringUtil.isBlank(field)) {
			throw new IllegalArgumentException("条件字段不能为空, condition field=" + field);
		}

		this.field = field;
		if (value instanceof Enum) {
			this.value = ((Enum) value).name();
		} else {
			this.value = value;
		}
		this.opt = opt;
	}

	/**
	 * 构造方法.null or not null
	 *
	 * @param field 条件字段
	 * @param opt   条件枚举
	 */
	private QueryCondition(String field, QueryOpt opt) {
		if (StringUtil.isBlank(field)) {
			throw new IllegalArgumentException("条件字段不能为空, condition field=" + field);
		}

		this.field = field;
		this.opt = opt;
	}

	/**
	 * 返回当前条件对象表示的sql语句.
	 *
	 * @return sql语句
	 */
	WhereSQL getSql() {
		StringBuilder sqlText = new StringBuilder();
		if (orCond != null && orCond.length > 0) {
			List<Object> paramList = new ArrayList<Object>();
			sqlText.append("(");
			for (QueryCondition cond : orCond) {
				sqlText.append(cond.getSql().getSql()).append(" or ");
				paramList.addAll(cond.getSql().getParams());
			}
			sqlText.delete(sqlText.lastIndexOf(" or "), sqlText.length());
			sqlText.append(")");
			return WhereSQL.valueOf(sqlText.toString(), paramList.toArray(new Object[paramList.size()]));
		} else if (andCond != null && andCond.length > 0) {
			List<Object> paramList = new ArrayList<Object>();
			sqlText.append("(");
			for (QueryCondition cond : andCond) {
				sqlText.append(cond.getSql().getSql()).append(" and ");
				paramList.addAll(cond.getSql().getParams());
			}
			sqlText.delete(sqlText.lastIndexOf(" and "), sqlText.length());
			sqlText.append(")");
			return WhereSQL.valueOf(sqlText.toString(), paramList.toArray(new Object[paramList.size()]));
		} else {
			sqlText.append(dialect.quotes(field)).append(" ").append(opt.getSymbol()).append(" ");
			List<Object> paramList = new ArrayList<Object>();
			switch (opt) {
				case IN:
					int paramLength = Array.getLength(this.value);
					if (this.value != null && paramLength > 0) {

						sqlText.append("(");
						for (int i = 0; i < paramLength; i++) {
							sqlText.append('?').append(',');

							Object val = Array.get(this.value, i);

							if (val != null && (val.getClass() == Boolean.class || val.getClass() == Boolean.TYPE)) {
								if ((Boolean) val) {
									paramList.add(1);
								} else {
									paramList.add(0);
								}
							} else {
								paramList.add(val);
							}
						}
						sqlText.deleteCharAt(sqlText.length() - 1);
						sqlText.append(")");
					} else {
						sqlText.append("(?)");
						paramList.add(this.value);
					}

					break;
				case ISNULL:
					break;
				case ISNOTNULL:
					break;
				default:
					sqlText.append('?');
					Object value = this.value;
					if (value instanceof Boolean && value != null) {
						if ((Boolean) value) {
							paramList.add(1);
						} else {
							paramList.add(0);
						}
					} else {
						paramList.add(value);
					}
					break;
			}

			return WhereSQL.valueOf(sqlText.toString(), paramList);
		}
	}

	@Override
	public String toString() {
		if (this.value != null) {
			return this.value.toString();
		}
		return "";
	}

	/**
	 * 等于条件.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition eq(String field, Object value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.EQ);
		return cond;
	}

	/**
	 * 不等于条件.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition noteq(String field, Object value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.NOTEQ);
		return cond;
	}

	/**
	 * 大于条件.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition gt(String field, Object value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.GT);
		return cond;
	}

	/**
	 * 大于等于条件.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition gte(String field, Object value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.GTE);
		return cond;
	}

	/**
	 * 小于条件.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition lt(String field, Object value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.LT);
		return cond;
	}

	/**
	 * 小于等于条件.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition lte(String field, Object value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.LTE);
		return cond;
	}

	public static QueryCondition in(String field, List<? extends Object> values) {
		return in(field, values.toArray(new Object[values.size()]));
	}

	public static QueryCondition in(String field, Set<? extends Object> values) {
		return in(field, values.toArray(new Object[values.size()]));
	}

	public static QueryCondition in(String field, Collection<? extends Object> values) {
		return in(field, values.toArray(new Object[values.size()]));
	}

	/**
	 * in操作.
	 *
	 * @param field  条件字段
	 * @param values 字段值
	 * @return 当前条件对象
	 */
	public static QueryCondition in(String field, Object... values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, byte[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, int[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, short[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, long[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, float[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, double[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	public static QueryCondition in(String field, boolean[] values) {
		QueryCondition cond = new QueryCondition(field, values, QueryOpt.IN);
		return cond;
	}

	/**
	 * like查询.
	 *
	 * @param field 条件字段
	 * @param value 字段值
	 */
	public static QueryCondition like(String field, String value) {
		QueryCondition cond = new QueryCondition(field, value, QueryOpt.LIKE);
		return cond;
	}

	/**
	 * or查询.
	 *
	 * @param conds 查询条件
	 */
	public static QueryCondition or(QueryCondition... conds) {
		if (conds == null || conds.length < 2) {
			throw new IllegalArgumentException("参数错误, or查询条件最少为2个");
		}
		QueryCondition cond = new QueryCondition();
		cond.setOrCond(conds);
		return cond;
	}

	public static QueryCondition or(List<QueryCondition> conds) {
		if (conds == null || conds.size() < 2) {
			throw new IllegalArgumentException("参数错误, or查询条件最少为2个");
		}
		return or(conds.toArray(new QueryCondition[conds.size()]));
	}

	/**
	 * and查询
	 * 
	 * @param conds
	 * @return
	 */
	public static QueryCondition and(QueryCondition... conds) {
		if (conds == null || conds.length < 2) {
			throw new IllegalArgumentException("参数错误, and查询条件最少为2个");
		}
		QueryCondition cond = new QueryCondition();
		cond.setAndCond(conds);
		return cond;
	}

	public static QueryCondition and(List<QueryCondition> conds) {
		if (conds == null || conds.size() < 2) {
			throw new IllegalArgumentException("参数错误, or查询条件最少为2个");
		}
		return and(conds.toArray(new QueryCondition[conds.size()]));
	}

	/**
	 * 为null 查询.
	 *
	 * @param field 条件字段
	 * @param clazz class 查询条件
	 */
	public static QueryCondition isNull(String field) {
		QueryCondition cond = new QueryCondition(field, QueryOpt.ISNULL);
		return cond;
	}

	/**
	 * 不为null查询.
	 *
	 * @param field 条件字段
	 * @param clazz class 查询条件
	 */
	public static QueryCondition isNotNull(String field) {
		QueryCondition cond = new QueryCondition(field, QueryOpt.ISNOTNULL);
		return cond;
	}

	boolean isAndCondAllEQ() {
		if (andCond.length == 0) {
			return false;
		}

		for (QueryCondition oneAnd : andCond) {
			if (oneAnd.getOpt() != QueryOpt.EQ) {
				return false;
			}
		}

		return true;
	}

	Dialect getDialect() {
		return this.dialect;
	}

	void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	String getField() {
		return field;
	}

	void setField(String field) {
		this.field = field;
	}

	Object getValue() {
		return value;
	}

	void setValue(Object value) {
		this.value = value;
	}

	QueryOpt getOpt() {
		return opt;
	}

	void setOpt(QueryOpt opt) {
		this.opt = opt;
	}

	QueryCondition[] getOrCond() {
		return orCond;
	}

	void setOrCond(QueryCondition[] orCond) {
		this.orCond = orCond;
	}

	QueryCondition[] getAndCond() {
		return andCond;
	}

	void setAndCond(QueryCondition[] andCond) {
		this.andCond = andCond;
	}

	ConditionRelation getConditionRelation() {
		return conditionRelation;
	}

	void setConditionRelation(ConditionRelation conditionRelation) {
		this.conditionRelation = conditionRelation;
	}
}
