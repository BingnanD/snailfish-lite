package me.duanbn.snailfish.core.orm.rdb;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.duanbn.snailfish.api.persistence.Order;
import me.duanbn.snailfish.api.persistence.OrderBy;
import me.duanbn.snailfish.core.domain.DomainObjectI;
import me.duanbn.snailfish.core.orm.PersistenceException;
import me.duanbn.snailfish.core.orm.rdb.QueryDefaultImpl.WhereSQL;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * 查询对象. 线程不安全
 * 
 * @author duanbn
 */
public interface Query {

	/**
	 * add field
	 */
	public Query addField(Field... field);

	public boolean isJoinQuery();

	/**
	 * join query by left join.
	 * 
	 * @param joinTable
	 * @return
	 */
	public Query leftJoin(JoinTable joinTable);

	/**
	 * join query by inner join.
	 * 
	 * @param joinTable
	 * @return
	 */
	public Query innerJoin(JoinTable joinTable);

	/**
	 * join query by right join.
	 * 
	 * @param joinTable
	 * @return
	 */
	public Query rightJoin(JoinTable joinTable);

	/**
	 * join query by outer join.
	 * 
	 * @param joinTable
	 * @return
	 */
	public Query outerJoin(JoinTable joinTable);

	/**
	 * and查询条件
	 * 
	 * @param cond
	 * @return
	 */
	public Query and(QueryCondition cond);

	/**
	 * or查询条件.
	 * 
	 * @param cond 查询条件
	 * @return
	 */
	public Query or(QueryCondition cond);

	/**
	 * 添加排序字段
	 * 
	 * @param field
	 * @param order
	 * @return
	 */
	public Query orderBy(String field, Order order);

	/**
	 * add order field for sort.
	 * 
	 * @param orderBy
	 * @return
	 */
	public Query orderBy(OrderBy orderBy);

	/**
	 * add order fields for sort
	 * 
	 * @param orderBys
	 * @return
	 */
	public Query orderBys(Collection<OrderBy> orderBys);

	/**
	 * 分页参数.
	 * 
	 * @param start 开始偏移量
	 * @param limit 页大小
	 */
	public Query limit(int start, int limit);

	/**
	 * 设置limit参数
	 * 
	 * @param limit limit
	 */
	public Query limit(int limit);

	/**
	 * clone.
	 * 
	 * @return
	 */
	public Query clone();

	/**
	 * 清除当前已经设置的查询条件.
	 */
	public void clean();

	/**
	 * join sql fragment.
	 * 
	 * @return
	 */
	public String getJoinSql();

	/**
	 * field sql fragment.
	 * 
	 * @return
	 */
	public String getFieldSql();

	/**
	 * where语句
	 * 
	 * @return
	 */
	public WhereSQL getWhereSql();

	/**
	 * 查询条件命中的数量
	 * 
	 * @return
	 */
	long count();

	/**
	 * 查询条件命中一条记录
	 * 
	 * @return
	 */
	<T> T load();

	/**
	 * 查询条件查出一个列表
	 * 
	 * @return
	 */
	<T> List<T> find();

	/**
	 * 删除查询条件命中的数据
	 */
	void delete();

	<T> Iterator<T> iterator();

	public static class JoinTable {
		@Getter
		private String name;
		@Getter
		private String alias;
		@Getter
		@Setter
		private JoinType joinType;
		@Getter
		private String on;

		private JoinTable() {
		}

		private JoinTable(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}

		public JoinTable on(String value) {
			this.on = value;
			return this;
		}

		public static JoinTable valueOf(String name, String alias) {
			return new JoinTable(name, alias);
		}

		public static JoinTable valueOf(Class<? extends DomainObjectI> clazz) {
			Table tableAnno = clazz.getAnnotation(Table.class);
			if (tableAnno == null) {
				throw new PersistenceException("get table info err, clazz=" + clazz);
			}
			String name = tableAnno.value();
			String alias = tableAnno.alias();
			return new JoinTable(name, alias);
		}

		@Override
		public String toString() {
			return "JoinTable [name=" + name + ", alias=" + alias + ", joinType=" + joinType + "]";
		}

	}

	public enum JoinType {

		LEFT("LEFT JOIN"),
		INNER("INNER JOIN"),
		RIGHT("RIGHT JOIN"),
		OUTER("OUTER JOIN");

		@Getter
		private String fragment;

		private JoinType(String fragment) {
			this.fragment = fragment;
		}

	}

	public static class Field {
		@Getter
		private String name;
		@Getter
		private String alias;

		public Field(String name) {
			this(name, null);
		}

		public Field(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}

		public static Field valueOf(String name) {
			return new Field(name);
		}

		public static Field valueOf(String name, String alias) {
			return new Field(name, alias);
		}

		@Override
		public String toString() {
			StringBuilder ts = new StringBuilder();
			ts.append(this.name);
			if (!StringUtil.isBlank(this.alias)) {
				ts.append(" AS ").append(this.alias);
			}
			return ts.toString();
		}
	}

	public enum QueryOpt {

		/**
		 * 等于.
		 */
		EQ("="),
		/**
		 * 不等于.
		 */
		NOTEQ("<>"),
		/**
		 * 大于.
		 */
		GT(">"),
		/**
		 * 大于等于.
		 */
		GTE(">="),
		/**
		 * 小于.
		 */
		LT("<"),
		/**
		 * 小于等于.
		 */
		LTE("<="),
		/**
		 * in查询.
		 */
		IN("in"),
		/**
		 * like查询.
		 */
		LIKE("like"),
		/**
		 * is null
		 */
		ISNULL("is null"),
		/**
		 * is not null
		 */
		ISNOTNULL("is not null");

		/**
		 * 操作符
		 */
		private String symbol;

		private QueryOpt(String symbol) {
			this.symbol = symbol;
		}

		public String getSymbol() {
			return this.symbol;
		}

	}

}
