package me.duanbn.snailfish.test.util;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.test.MockObject;
import me.duanbn.snailfish.util.Mock;

@Slf4j
public class MockTest {

	@Test
	public void test() {
		MockObject mockObject = Mock.mock(MockObject.class);
		log.info("{}", mockObject);
	}

}
