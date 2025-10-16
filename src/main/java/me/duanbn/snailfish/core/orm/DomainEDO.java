/**
 * 
 */
package me.duanbn.snailfish.core.orm;

import java.util.Date;

import lombok.Data;
import me.duanbn.snailfish.core.orm.rdb.annotations.Field;

/**
 * @author bingnan.dbn
 *
 */
@Data
public class DomainEDO implements DomainDOI {

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
	@Field(value = "is_delete", index = true, comment = "是否逻辑删除, 0表示未删除，1表示已删除")
	protected Boolean deleted;

}
