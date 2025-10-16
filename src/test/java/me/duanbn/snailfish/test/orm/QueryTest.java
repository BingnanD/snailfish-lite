package me.duanbn.snailfish.test.orm;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.orm.rdb.QueryCondition;
import me.duanbn.snailfish.core.orm.rdb.QueryDefaultImpl;
import me.duanbn.snailfish.util.collection.Sets;

@Slf4j
@SuppressWarnings({ "rawtypes" })
public class QueryTest {

	@Test
	public void test() {
		QueryDefaultImpl query = new QueryDefaultImpl();
		query.and(QueryCondition.or(QueryCondition.like("bbb", "b%"), QueryCondition.like("ccc", "c%")));
		log.info("{}", query.getWhereSql());

		query = new QueryDefaultImpl();
		Collection<String> set = Sets.newHashSet("aaa");
		query.and(QueryCondition.in("aaa", set));
		log.info("{}", query.getWhereSql());

		query = new QueryDefaultImpl<>();
		query.and(QueryCondition.eq("key", "aaa"));
		log.info("{}", query.getWhereSql());
	}

}
