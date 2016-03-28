package com.connorlinfoot.mctools;

import com.connorlinfoot.mctools.Hypixel.GuiHandler;
import com.connorlinfoot.mctools.Hypixel.Hypixel;
import net.hypixel.api.HypixelAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.UUID;

@Mod(modid = MCTools.MODID, version = MCTools.VERSION, guiFactory = "com.connorlinfoot.mctools.MCToolsGuiFactory")
public class MCTools {
	private static MCTools mcTools;
	public static final String MODID = "mctools";
	public static final String VERSION = "1.0";
	private ConfigHandler configHandler;
	private Hypixel hypixel;

	public static UUID UUID;
	String clientUUID;
	boolean started = false;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mcTools, new GuiHandler());
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mcTools = this;
		configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());

		hypixel = new Hypixel();
		FMLCommonHandler.instance().bus().register(hypixel);
	}

	@EventHandler
	public void init(FMLPostInitializationEvent event) {
		Minecraft minecraft = FMLClientHandler.instance().getClient();
		UUID = minecraft.getSession().getProfile().getId();
		clientUUID = UUID.toString().replaceAll("-", "");
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(new PlayerRender());

		try {
			String key = configHandler.getAPIKey();
			HypixelAPI.getInstance().setApiKey(java.util.UUID.fromString(key));

			started = true;
//			Hypixel hypixel = new Hypixel();
//			FMLCommonHandler.instance().bus().register(hypixel);
//			hypixel.updatePlayerData(UUID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	@SubscribeEvent
	public void test(Event event) {
		String name = event.getClass().getName();
		if ((name.toLowerCase().contains("chat") || name.toLowerCase().contains("entity") || name.toLowerCase().contains("world") || name.toLowerCase().contains("player")) && !name.toLowerCase().contains("render") && !name.toLowerCase().contains("tick") && !name.toLowerCase().contains("living"))
			System.out.println(name);
	}

	public static MCTools getMcTools() {
		return mcTools;
	}

	public ConfigHandler getConfigHandler() {
		return configHandler;
	}

	public void outputDebug(String message) {
		if (!getConfigHandler().isDebug())
			return;
		System.out.println("[MC TOOLS] [DEBUG] " + message);
	}

	//	@SubscribeEvent
	public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
		entityPlayer.openGui(mcTools, GuiHandler.MOD_TILE_ENTITY_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
	}

	public Hypixel getHypixel() {
		return hypixel;
	}

}
