package me.duanbn.snailfish.test.commandbus;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.duanbn.snailfish.api.command.UpdateCommand;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class EchoCommand extends UpdateCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Setter
	@Getter
	private String message;

}
