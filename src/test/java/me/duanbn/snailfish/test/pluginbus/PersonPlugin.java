package me.duanbn.snailfish.test.pluginbus;

import me.duanbn.snailfish.core.pluginbus.Pluginable;

public interface PersonPlugin extends Pluginable {

	String who();

	void hello();

}
