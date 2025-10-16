package me.duanbn.snailfish.core.domain.pattern;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import me.duanbn.snailfish.api.dto.Page;
import me.duanbn.snailfish.api.persistence.Order;
import me.duanbn.snailfish.api.persistence.OrderBy;
import me.duanbn.snailfish.core.orm.rdb.Query;
import me.duanbn.snailfish.core.orm.rdb.QueryCondition;
import me.duanbn.snailfish.util.Optional;
import me.duanbn.snailfish.util.collection.Lists;

/**
 * 领域模型规格. 用于业务逻辑的校验、查询条件.
 * 
 * @author bingnan.dbn
 *
 */
@Data
public abstract class DomainSPEC implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 分页参数 */
	protected Page page;
	/** 排序参数 */
	protected List<OrderBy> orderBy = Lists.newArrayList();
	/** 租户UUID */
	protected String tanentUUID;

	public void appendQuery(Query query) {
		Optional.ofNullable(this.page).ifPresent(e -> query.limit(e.getStart(), e.getLimit()));
		Optional.ofEmptyable(this.tanentUUID).ifPresent(e -> query.and(QueryCondition.eq("tanent_uuid", e)));
		Optional.ofEmptyable(this.orderBy).ifPresent(e -> query.orderBys(e));

		appendQueryIndividuality(query);
	}

	protected abstract void appendQueryIndividuality(Query query);

	public List<OrderBy> orderBy(String field, Order order) {
		this.orderBy.add(new OrderBy(field, order));
		return this.orderBy;
	}

}
