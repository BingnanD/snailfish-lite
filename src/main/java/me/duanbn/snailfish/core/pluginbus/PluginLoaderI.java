package me.duanbn.snailfish.core.pluginbus;

public interface PluginLoaderI {

	public void loadPlugin(PluginRegister pluginRegister, Class<Plugin> pluginable)
			throws PluginLoaderException;

}
