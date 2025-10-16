package me.duanbn.snailfish.test.pluginbus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import me.duanbn.snailfish.api.Scenario;
import me.duanbn.snailfish.core.pluginbus.PluginBus;
import me.duanbn.snailfish.test.Application;

@SpringBootTest(classes = Application.class)
public class PluginBusTest {

	@Test
	public void test() {
		Scenario bizIdentity = Scenario.newInstance(SelectPerson.A);
		String who = PluginBus.dispatch(PersonExtPt.class, bizIdentity, plugin -> plugin.who());
		System.out.println(who);

		bizIdentity = Scenario.newInstance(SelectPerson.B);
		who = PluginBus.dispatch(PersonExtPt.class, bizIdentity, plugin -> plugin.who());
		System.out.println(who);
	}

}
