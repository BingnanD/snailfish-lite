package me.duanbn.snailfish.test.domain.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.duanbn.snailfish.core.domain.DomainV;
import me.duanbn.snailfish.core.orm.rdb.annotations.Field;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;

@Table(value = "t_domain_valueobject", alias = "c")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainValueObjectV extends DomainV {

	@Field
	private String value;

	@Field(value = "test_a_id", index = true)
	private Long testAId;

}
