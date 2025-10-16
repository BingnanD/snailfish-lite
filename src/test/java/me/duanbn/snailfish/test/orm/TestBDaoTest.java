package me.duanbn.snailfish.test.orm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.test.Application;
import me.duanbn.snailfish.test.orm.entity.TestB;
import me.duanbn.snailfish.util.Mock;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.collection.Sets;

@Slf4j
@SpringBootTest(classes = Application.class)
public class TestBDaoTest {

    private TestB _buildtestB() {
        LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("a", "aaa");
        map.put("b", 1);
        map.put("中文key", "中文value");
        List<String> list = Lists.newArrayList("a", "b", "中文c");
        Set<String> set = Sets.newTreeSet("a", "b", "中文c");

        TestB testB = Mock.mock(TestB.class);
        testB.setB2_NULL(null);
        testB.setSh4_NULL(null);
        testB.setI6_NULL(null);
        testB.setF8_NULL(null);
        testB.setD10_NULL(null);
        testB.setS11_NULL(null);
        testB.setBy12_NULL(null);
        testB.setByte14_NULL(null);
        testB.setDeleted(true);
        testB.setMap(map);
        testB.setMap_NULL(null);
        testB.setList(list);
        testB.setList_NULL(null);
        testB.setSet(set);
        testB.setSet_NULL(null);
        return testB;
    }

    public void testInsert() {
        TestB testB = this._buildtestB();
        log.info("{}", testB);
    }

}
