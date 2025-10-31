package me.duanbn.snailfish.test.pluginbus.plugin;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.pluginbus.annotations.Plugin;
import me.duanbn.snailfish.test.pluginbus.PersonPlugin;
import me.duanbn.snailfish.test.pluginbus.StaffPlugin;

@Slf4j
@Plugin("a")
public class PersonAPlugin implements PersonPlugin, StaffPlugin {

	@Override
	public String who() {
		return "Hi, i am person A";
	}

	@Override
	public void hello() {
		log.info(("hello i am Person A"));
	}

	@Override
	public String staff() {
		return "car";
	}

}
