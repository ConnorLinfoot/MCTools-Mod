package com.connorlinfoot.mctools.Handlers;

import com.connorlinfoot.mctools.MCTools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class MCToolsConfig extends GuiConfig {

	public MCToolsConfig(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(MCTools.getMcTools().getConfigHandler().getConfig().getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(), MCTools.MODID, false, false, "MC Tools Configuration", "");
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		MCTools.getMcTools().getConfigHandler().getConfig().save();
	}

}