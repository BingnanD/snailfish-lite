package me.duanbn.snailfish.test.domain.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.duanbn.snailfish.core.domain.DomainE;
import me.duanbn.snailfish.core.orm.rdb.annotations.Field;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;

@Table(value = "t_domain_entity", alias = "b")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainEntityE extends DomainE {

	@Field("entity_id")
	private Long entityId;
	@Field
	private String name;
	@Field("change_log")
	private String changeLog;

	@Field("domain_enum")
	private DomainEnum domainEnum;

	@Field(value = "test_a_id", index = true)
	private Long testAId;

	private DomainValueObjectV valueObject;

}
