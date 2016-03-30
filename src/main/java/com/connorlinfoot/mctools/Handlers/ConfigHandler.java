package com.connorlinfoot.mctools.Handlers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigHandler {
	private File configFile;
	private Configuration config;

	public ConfigHandler(File file) {
		configFile = file;
		config = new Configuration(configFile);

		config.load();
		isSendData();
		isDebug();
		getAPIKey();
		isSwapHands();
		config.save();
	}

	public Property getAPIKeyProperty() {
		return config.get(Configuration.CATEGORY_CLIENT, "Hypixel API Key", "NO_KEY", "Set your API key here or things won't work properly!");
	}

	public String getAPIKey() {
		return getAPIKeyProperty().getString();
	}

	public Property getDebugProperty() {
		return config.get(Configuration.CATEGORY_CLIENT, "Debug Mode", false, "Debug mode will enable specific logging outputs to the console.");
	}

	public boolean isDebug() {
		return getDebugProperty().getBoolean();
	}

	public Property getSendData() {
		return config.get(Configuration.CATEGORY_CLIENT, "Send Data", true, "Whether MC Tools should report back to it's API with anonymous data.");
	}

	public boolean isSendData() {
		return getSendData().getBoolean();
	}

	public Property getSwapHands() {
		return config.get(Configuration.CATEGORY_CLIENT, "Swap Player Hands", false, "");
	}

	public boolean isSwapHands() {
		return getSwapHands().getBoolean();
	}

	public Configuration getConfig() {
		return config;
	}

	public File getConfigFile() {
		return configFile;
	}

}
