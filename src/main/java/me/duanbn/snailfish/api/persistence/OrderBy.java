/**
 * 
 */
package me.duanbn.snailfish.api.persistence;

import java.io.Serializable;

/**
 * @author bingnan.dbn
 *
 */
public class OrderBy implements Serializable {

	private static final long serialVersionUID = 1L;

	private String field;
	private Order order;

	public OrderBy(String field, Order order) {
		this.field = field.toUpperCase();
		this.order = order;
	}

	public String getField() {
		return field;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return "OrderBy [field=" + field + ", order=" + order + "]";
	}

}
