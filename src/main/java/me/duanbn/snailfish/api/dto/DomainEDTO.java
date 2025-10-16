package me.duanbn.snailfish.api.dto;

import java.util.Date;

import lombok.Data;

/**
 * Data Transfer Object
 *
 * @author zhilin
 */
@Data
public abstract class DomainEDTO implements DomainDTOI {

	private static final long serialVersionUID = 3843386621052856607L;

	/** 数据库主键 */
	protected String id;
	/** 实体uuid */
	protected String uuid;

	/** 实体创建时间 */
	protected Date gmtCreate;
	/** 实体修改时间 */
	protected Date gmtModified;
	/** 实体是否被逻辑删除 */
	protected boolean deleted = false;

	public Long getIdAsLong() {
		if (this.id != null) {
			return Long.parseLong(this.id);
		}
		return null;
	}

}
