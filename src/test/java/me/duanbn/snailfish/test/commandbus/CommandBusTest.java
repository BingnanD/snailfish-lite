package me.duanbn.snailfish.test.commandbus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;

import me.duanbn.snailfish.api.dto.SingleResponse;
import me.duanbn.snailfish.core.commandbus.CommandBus;
import me.duanbn.snailfish.test.Application;

@SpringBootTest(classes = Application.class)
public class CommandBusTest {

	@Test
	public void test() throws Exception {
		EchoCommand echoCommand = EchoCommand.builder().message("hello tianting command bus").build();
		SingleResponse<String> resp = CommandBus.dispatch(echoCommand);
		System.out.println(JSON.toJSONString(resp, true));
	}

}
