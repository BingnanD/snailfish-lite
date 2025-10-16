package me.duanbn.snailfish.api.dto;

import java.util.Collection;

import lombok.Getter;

/**
 * MultiResponse
 *
 * @author zhilin
 * @author bingnan.dbn
 */
@Getter
public class PageResponse<T> extends Response {

	/** (de)serialization */
	private static final long serialVersionUID = 4683238591912052316L;

	private long total;
	private Collection<T> data;

	private long pageNum;
	private long pageSize;
	private long totalPage;

	public PageResponse() {
		this.setCode(CODE_SUCCESS);
		this.setSuccess(true);
	}

	public void setData(Collection<T> data, long total, Page page) {
		this.setData(data, total, page.getPageNum(), page.getPageSize());
	}

	public void setData(Collection<T> data, long total, long pageNum, long pageSize) {
		this.setCode("200");
		this.setSuccess(true);
		this.data = data;
		this.total = total;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
	}

}
