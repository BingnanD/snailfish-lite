package me.duanbn.snailfish.test.orm;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.DataNode;
import me.duanbn.snailfish.test.SnailfishTest;
import me.duanbn.snailfish.test.orm.dao.TestAStatDao;
import me.duanbn.snailfish.util.collection.Lists;

@Slf4j
public class TestAStatTest extends SnailfishTest {

    @Resource(name = "testAStatDao")
    private TestAStatDao testAStatDao;
    @Resource(name = "testAStatDao2")
    private TestAStatDao testAStatDao2;

    private String sql = "select sum(i5), sum(i6), count(1), test_enum from t_test_a group by test_enum having test_enum = ?";

    @Test
    public void testTestA() {
        List<DataNode> result = this.testAStatDao.execute(sql, Lists.newArrayList("A"));
        log.info("{}", JSON.toJSONString(result, true));
    }

    @Test
    public void testTestA2() {
        List<DataNode> result = this.testAStatDao2.execute(sql, Lists.newArrayList("A"));
        log.info("{}", JSON.toJSONString(result, true));
    }

}
