package me.duanbn.snailfish.test.commandbus;

import org.springframework.transaction.annotation.Transactional;

import me.duanbn.snailfish.api.dto.SingleResponse;
import me.duanbn.snailfish.core.commandbus.CommandExecutorI;
import me.duanbn.snailfish.core.commandbus.annotations.CommandExecutor;

@CommandExecutor
public class EchoCommandExecutor implements CommandExecutorI<EchoCommand, SingleResponse<String>> {

	@Transactional
	@Override
	public void execute(EchoCommand command, SingleResponse<String> response) {
		System.out.println("do execute");
		response.setData(command.getMessage());
	}

}
