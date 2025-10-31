package me.duanbn.snailfish.test.pluginbus;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.pluginbus.PluginBus;
import me.duanbn.snailfish.core.pluginbus.PluginBus.PluginSelector;
import me.duanbn.snailfish.test.Application;

@Slf4j
@SpringBootTest(classes = Application.class)
public class PluginBusTest {

	@Test
	public void testStaffPlugin() {
		String result = PluginBus.dispatch(StaffPlugin.class, "a", p -> p.staff());
		log.info("{}", result);

		result = PluginBus.dispatch(StaffPlugin.class, "b", p -> p.staff());
		log.info("{}", result);
	}

	@Test
	public void testPersonPlugin() {
		String result = PluginBus.dispatch(PersonPlugin.class, "a", p -> p.who());
		log.info("{}", result);
		PluginBus.dispatchVoid(PersonPlugin.class, "a", p -> p.hello());

		result = PluginBus.dispatch(PersonPlugin.class, "b", p -> p.who());
		log.info("{}", result);
		PluginBus.dispatchVoid(PersonPlugin.class, "b", p -> p.hello());

		PersionSelector persionSelector = new PersionSelector(true);
		result = PluginBus.dispatch(PersonPlugin.class, persionSelector, p -> p.who());
		log.info("{}", result);
		PluginBus.dispatchVoid(PersonPlugin.class, persionSelector, p -> p.hello());

		persionSelector = new PersionSelector(false);
		result = PluginBus.dispatch(PersonPlugin.class, persionSelector, p -> p.who());
		log.info("{}", result);
		PluginBus.dispatchVoid(PersonPlugin.class, persionSelector, p -> p.hello());
	}

	@Test
	public void testPluginNames() {
		List<String> pluginNames = PluginBus.getPluginNames(PersonPlugin.class);
		log.info("{}", pluginNames);

		pluginNames = PluginBus.getPluginNames(StaffPlugin.class);
		log.info("{}", pluginNames);
	}

	public static class PersionSelector implements PluginSelector {

		private boolean hasCar;

		public PersionSelector(boolean hasCar) {
			this.hasCar = hasCar;
		}

		@Override
		public String select() {
			if (hasCar) {
				return "a";
			} else {
				return "b";
			}
		}
	}

}
