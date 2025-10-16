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
public class DomainVDO implements DomainDOI {

	private static final long serialVersionUID = 1L;

	/** 数据库主键 */
	@Field(primaryKey = true, comment = "主键")
	protected Long id;

	/** 创建时间 */
	@Field(value = "gmt_create", isNull = false, index = true, comment = "创建时间")
	protected Date gmtCreate;
	/** 修改时间 */
	@Field(value = "gmt_modified", isNull = false, index = true, comment = "修改时间")
	protected Date gmtModified;

}
