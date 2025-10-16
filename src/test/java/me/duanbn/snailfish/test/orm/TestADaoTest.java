package me.duanbn.snailfish.test.orm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.api.persistence.Order;
import me.duanbn.snailfish.core.orm.rdb.Query;
import me.duanbn.snailfish.core.orm.rdb.Query.Field;
import me.duanbn.snailfish.core.orm.rdb.Query.JoinTable;
import me.duanbn.snailfish.core.orm.rdb.QueryCondition;
import me.duanbn.snailfish.test.Application;
import me.duanbn.snailfish.test.domain.beans.DomainEntityE;
import me.duanbn.snailfish.test.domain.beans.DomainValueObjectV;
import me.duanbn.snailfish.test.orm.dao.TestADao;
import me.duanbn.snailfish.test.orm.entity.TestA;
import me.duanbn.snailfish.test.orm.entity.TestB;
import me.duanbn.snailfish.util.Mock;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.collection.Sets;

@Slf4j
@SpringBootTest(classes = Application.class)
public class TestADaoTest {

    @Resource(name = "testADao")
    private TestADao testADao;
    @Resource(name = "testADao2")
    private TestADao testADao2;
    @Resource(name = "testADao3")
    private TestADao testADao3;
    @Resource(name = "testADao4")
    private TestADao testaDao4;

    private TestA _buildTestA() {
        LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("a", "aaa");
        map.put("b", 1);
        map.put("中文key", "中文value");
        List<String> list = Lists.newArrayList("a", "b", "中文c");
        Set<String> set = Sets.newTreeSet("a", "b", "中文c");

        TestB testB = Mock.mock(TestB.class);
        testB.setList(list);
        testB.setSet(set);
        testB.setMap(map);

        TestA testA = Mock.mock(TestA.class);
        testA.setB2_NULL(null);
        testA.setSh4_NULL(null);
        testA.setI6_NULL(null);
        testA.setF8_NULL(null);
        testA.setD10_NULL(null);
        testA.setS11_NULL(null);
        testA.setBy12_NULL(null);
        testA.setByte14_NULL(null);
        testA.setDeleted(true);
        testA.setMap(map);
        testA.setMap_NULL(null);
        testA.setList(list);
        testA.setList_NULL(null);
        testA.setSet(set);
        testA.setSet_NULL(null);
        testA.setJsonField(testB);
        testA.setJsonField_NULL(null);
        return testA;
    }

    @Test
    public void testInserFour() {
        TestA testa = _buildTestA();
        this.testaDao4.insert(testa);
    }

    @Test
    public void testInsertAndUpdate() {
        for (int i = 0; i < 5; i++) {
            TestA testA = _buildTestA();

            this.testADao.insert(testA);
            this.testADao2.insert(testA);
            this.testADao3.insert(testA);
            this.testaDao4.insert(testA);

            testA.setDeleted(false);
            this.testADao.update(testA);
            this.testADao2.update(testA);

            log.info("{}", testA);
        }
    }

    @Test
    public void testDeleteByIds() {
        this.testADao.deleteByIds(Lists.newArrayList(1163913219909177344L));
    }

    @Test
    public void testDelete() {
        this.testADao.createQuery().delete();
        this.testADao2.createQuery().delete();
    }

    @Test
    public void testClean() {
        this.testADao.clean();
        this.testADao2.clean();
    }

    @Test
    public void testCount() {
        long count = this.testADao.count();
        log.info("{}", count);
        count = this.testADao2.count();
        log.info("{}", count);
    }

    @Test
    public void testFindById() {
        TestA testA = this.testADao.findById(1163891470207373312L);
        log.info("{}", testA);
        List<TestA> result = this.testADao.findByIds(Lists.newArrayList(1151922557940748288L, 1151922558892855296L));
        result.forEach(e -> log.info("{}", e));
    }

    @Test
    public void testQuery() {
        Query query = this.testADao.createQuery();
        List<TestA> result = query.find();
        result.forEach(e -> log.info("{}", e));
    }

    @Test
    public void testQuery2() {
        Query query = this.testADao.createQuery();

        List<TestA> result2 = query.and(QueryCondition.eq("b1", true)).find();
        result2.forEach(e -> log.info("{}", e));

        log.info("{}", query.count());

        query.clean();

        List<TestA> result1 = query.addField(Field.valueOf("b1"), Field.valueOf("s11")).find();
        result1.forEach(e -> log.info("{}", e));
    }

    @Test
    public void testJoin() {
        Query query = this.testADao.createQuery();
        query.addField(Field.valueOf("a.id"), Field.valueOf("b.id"));
        query.leftJoin(JoinTable.valueOf(TestB.class).on("a.id = b.id"));
        query.and(QueryCondition.eq("b.id", 1));
        query.limit(0, 10);
        query.orderBy("b.id", Order.DESC);
        query.find();

        query.count();
    }

    @Test
    public void testIterator() throws Exception {
        Query query = this.testADao.createQuery();
        Iterator<TestA> iterator = query.iterator();
        iterator = query.iterator();
        while (iterator.hasNext()) {
            log.info("{}", iterator.next());
        }
    }

    @Test
    public void testIteratorWithJoin() throws Exception {
        Query query = this.testADao.createQuery();
        query.addField(Field.valueOf("a.*"));
        query.innerJoin(JoinTable.valueOf(DomainEntityE.class).on("a.id = b.test_a_id"));
        query.innerJoin(JoinTable.valueOf(DomainValueObjectV.class).on("a.id = c.test_a_id"));
        Iterator<TestA> iterator = query.iterator();
        while (iterator.hasNext()) {
            log.info("{}", iterator.next());
        }
    }

}
