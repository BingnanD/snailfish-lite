/**
 * 
 */
package me.duanbn.snailfish.api.persistence;

/**
 * @author bingnan.dbn
 *
 */
public enum Order {

	/**
	 * 升序.
	 */
	ASC("ASC"),
	/**
	 * 降序.
	 */
	DESC("DESC");

	private String value;

	private Order(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
