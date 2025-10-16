package me.duanbn.snailfish.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DomainVDTO implements DomainDTOI {

	private static final long serialVersionUID = 1L;

	/** 数据库主键 */
	protected String id;

	/** 创建时间 */
	protected Date gmtCreate;
	/** 修改时间 */
	protected Date gmtModified;
	/** 删除标识 */
	protected boolean deleted;

	public Long getIdAsLong() {
		if (this.id != null) {
			return Long.parseLong(this.id);
		}
		return null;
	}

}
