package com.connorlinfoot.mctools;

import com.connorlinfoot.mctools.Commands.MCToolsCommand;
import com.connorlinfoot.mctools.Handlers.ConfigHandler;
import com.connorlinfoot.mctools.Handlers.GuiHandler;
import com.connorlinfoot.mctools.Hypixel.Hypixel;
import com.connorlinfoot.mctools.Listeners.PlayerRender;
import net.hypixel.api.HypixelAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

@Mod(modid = MCTools.MODID, version = MCTools.VERSION, guiFactory = "com.connorlinfoot.mctools.Handlers.MCToolsGuiFactory")
public class MCTools {
	private static MCTools mcTools;
	public static final String prefix = TextFormatting.GRAY + "[" + TextFormatting.GOLD + "MC Tools" + TextFormatting.GRAY + "] " + TextFormatting.RESET;
	public static final String MODID = "mctools";
	public static final String VERSION = "1.0";
	private ConfigHandler configHandler;
	private Hypixel hypixel;
	public static KeyBinding quickActions;

	public static UUID UUID;
	String clientUUID;
	boolean started = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mcTools = this;
		configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());

		hypixel = new Hypixel();
		FMLCommonHandler.instance().bus().register(hypixel);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mcTools, new GuiHandler());
		quickActions = new KeyBinding("Quick Actions", Keyboard.KEY_X, "MC Tools");
		ClientRegistry.registerKeyBinding(quickActions);

		// Command test
		ClientCommandHandler.instance.registerCommand(new MCToolsCommand());
		MinecraftForge.EVENT_BUS.register(new IngameText(Minecraft.getMinecraft()));
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (quickActions.isPressed()) {
			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
			if (entityPlayer.isSneaking() && getConfigHandler().isDebug()) {
				entityPlayer.openGui(mcTools, GuiHandler.TEXT_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
			} else {
				if (!hypixel.isCurrentlyOnHypixel()) {
					entityPlayer.addChatMessage(new TextComponentString(prefix + TextFormatting.RED + "You are not currently on any supported servers"));
					return;
				}
				entityPlayer.openGui(mcTools, GuiHandler.QUICK_ACTIONS_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
			}
		}
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
		entityPlayer.openGui(mcTools, GuiHandler.HYPIXEL_STATS_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
	}

	public Hypixel getHypixel() {
		return hypixel;
	}

}
