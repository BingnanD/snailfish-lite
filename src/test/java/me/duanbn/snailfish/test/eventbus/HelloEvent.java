package me.duanbn.snailfish.test.eventbus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.duanbn.snailfish.core.eventbus.EventI;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class HelloEvent implements EventI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

}
