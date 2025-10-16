package me.duanbn.snailfish.api.dto;

import java.util.ArrayList;
import java.util.List;

import me.duanbn.snailfish.api.persistence.Order;
import me.duanbn.snailfish.api.persistence.OrderBy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 领域模型规格.
 * 
 * @author bingnan.dbn
 *
 */
@Data
public class DomainSpecDTO implements DomainDTOI {

	private static final long serialVersionUID = 1L;

	/** 分页参数 */
	@ApiModelProperty("分页参数")
	protected Page page;
	protected List<OrderBy> orderBy = new ArrayList<>();
	/** 租户UUID */
	@ApiModelProperty("租户UUID")
	protected String tanentUUID;

	public List<OrderBy> orderBy(String field, Order order) {
		this.orderBy.add(new OrderBy(field, order));
		return this.orderBy;
	}

}
