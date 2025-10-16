package me.duanbn.snailfish.core.domain;

import java.util.Date;

import lombok.Data;
import me.duanbn.snailfish.core.orm.rdb.annotations.Field;
import me.duanbn.snailfish.util.Random;

@Data
public abstract class DomainE implements DomainObjectI {

	private static final long serialVersionUID = 1L;

	/** 数据库主键 */
	@Field(primaryKey = true, comment = "主键")
	protected Long id;
	/** 实体uuid */
	@Field(isNull = false, index = true, comment = "实体UUID")
	protected String uuid;

	/** 实体创建时间 */
	@Field(value = "gmt_create", isNull = false, index = true, comment = "创建时间")
	protected Date gmtCreate;
	/** 实体修改时间 */
	@Field(value = "gmt_modified", isNull = false, index = true, comment = "修改时间")
	protected Date gmtModified;
	/** 实体操作人uuid */
	@Field(value = "is_delete", index = true, comment = "是否逻辑删除, 0表示未删除，1表示已删除")
	protected Boolean deleted = false;

	public DomainE() {
		this.id = Random.snowFlakeId();
		this.uuid = Random.javaUUID();
	}

}
