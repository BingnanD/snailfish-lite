package me.duanbn.snailfish.test.pluginbus.plugin;

import me.duanbn.snailfish.core.pluginbus.annotations.Plugin;
import me.duanbn.snailfish.test.pluginbus.PersonExtPt;

@Plugin("a")
public class PersonAPlugin implements PersonExtPt {

	@Override
	public String who() {
		return "Hi, i am person A";
	}

}
