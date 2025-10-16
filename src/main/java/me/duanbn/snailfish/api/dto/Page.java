package me.duanbn.snailfish.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("分页")
public class Page implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "页号", required = true)
	private int pageNum;
	@ApiModelProperty(value = "页长", required = true)
	private int pageSize;

	public int getStart() {
		return (pageNum - 1) * pageSize;
	}

	public int getLimit() {
		return pageSize;
	}

}
