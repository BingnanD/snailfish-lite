package me.duanbn.snailfish.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页参数.
 * 
 * @author shanwei
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page implements Serializable {

	private static final long serialVersionUID = 1L;

	private int pageNum;
	private int pageSize;

	public int getStart() {
		return (pageNum - 1) * pageSize;
	}

	public int getLimit() {
		return pageSize;
	}

}
