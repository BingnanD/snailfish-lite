package me.duanbn.snailfish.test.pluginbus.plugin;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.pluginbus.Plugin;
import me.duanbn.snailfish.test.pluginbus.PersonPlugin;
import me.duanbn.snailfish.test.pluginbus.StaffPlugin;

@Slf4j
@Plugin("b")
public class PersonBPlugin implements PersonPlugin, StaffPlugin {

	@Override
	public String who() {
		return "Hi, i am person B";
	}

	@Override
	public void hello() {
		log.info(("hello i am Person B"));
	}

	@Override
	public String staff() {
		return "house";
	}

}
