package me.duanbn.snailfish.test.commandbus;

import me.duanbn.snailfish.api.dto.SingleResponse;
import me.duanbn.snailfish.core.commandbus.CommandInterceptorI;
import me.duanbn.snailfish.core.commandbus.annotations.CommandInterceptor;

@CommandInterceptor
public class EchoCommandInterceptor implements CommandInterceptorI<EchoCommand, SingleResponse<String>> {

	@Override
	public boolean preHandle(EchoCommand command, SingleResponse<String> response) {
		System.out.println("do pre");
		return true;
	}

	@Override
	public void postHandle(EchoCommand command, SingleResponse<String> response) {
		System.out.println("do post");
	}

}
