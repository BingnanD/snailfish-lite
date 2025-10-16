package me.duanbn.snailfish.test.domain.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.duanbn.snailfish.api.dto.DomainVDTO;

/**
 * @author bingnan.dbn
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainValueObjectVDTO extends DomainVDTO {

    private String value;

}
