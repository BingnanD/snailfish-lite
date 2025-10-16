package me.duanbn.snailfish.core.pluginbus;

public interface PluginLoaderI {

	public void loadPlugin(PluginRegister pluginRegister, Class<ExtensionPointI> extensionPoint)
			throws PluginLoaderException;

}
