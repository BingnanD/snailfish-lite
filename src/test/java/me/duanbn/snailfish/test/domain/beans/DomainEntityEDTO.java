package me.duanbn.snailfish.test.domain.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.duanbn.snailfish.api.dto.DomainEDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainEntityEDTO extends DomainEDTO {

    private String entityId;
    private String name;
    private String changeLog;

    private String domainEnum;

    private DomainValueObjectVDTO valueObject;

}
