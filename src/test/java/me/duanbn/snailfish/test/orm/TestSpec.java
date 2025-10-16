/**
 * 
 */
package me.duanbn.snailfish.test.orm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.duanbn.snailfish.core.domain.pattern.DomainSPEC;
import me.duanbn.snailfish.core.orm.rdb.Query;
import me.duanbn.snailfish.core.orm.rdb.QueryCondition;
import me.duanbn.snailfish.test.orm.entity.TestEnum;
import me.duanbn.snailfish.util.Optional;

/**
 * @author bingnan.dbn
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestSpec extends DomainSPEC {

    private TestEnum testEnum;

    @Override
    protected void appendQueryIndividuality(Query query) {
        Optional.ofNullable(this.testEnum).ifPresent(e -> query.and(QueryCondition.eq("test_enum", e)));
    }

}
