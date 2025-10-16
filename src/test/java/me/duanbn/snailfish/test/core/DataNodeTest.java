package me.duanbn.snailfish.test.core;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.DataNode;
import me.duanbn.snailfish.core.domain.DomainBus;
import me.duanbn.snailfish.test.MockObject;
import me.duanbn.snailfish.util.Mock;
import me.duanbn.snailfish.util.collection.Lists;

@Slf4j
public class DataNodeTest {

    /**
     * 
     */
    @Test
    public void test() {
        MockObject mock = Mock.mock(MockObject.class);
        DataNode dataNode = DomainBus.map(mock, DataNode.class);
        log.info("{}", JSON.toJSONString(dataNode, true));
        MockObject mockObj = DomainBus.map(dataNode, MockObject.class);
        log.info("{}", JSON.toJSONString(mockObj, true));

        List<MockObject> mockObjs = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            mockObjs.add(Mock.mock(MockObject.class));
        }
        List<DataNode> dataNodes = DomainBus.mapAsList(mockObjs);
        log.info("{}", JSON.toJSONString(dataNodes, true));
        List<MockObject> mockObjs1 = DomainBus.mapAsList(dataNodes, MockObject.class);
        log.info("{}", JSON.toJSONString(mockObjs1, true));
    }

}
