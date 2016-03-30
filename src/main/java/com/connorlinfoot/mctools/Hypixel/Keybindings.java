package com.connorlinfoot.mctools.Hypixel;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	public static KeyBinding quickJoinSoloNormalSkywars;
	public static KeyBinding quickJoinSoloInsaneSkywars;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		quickJoinSoloNormalSkywars = new KeyBinding("Quick Join - Solo Normal Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
		quickJoinSoloInsaneSkywars = new KeyBinding("Quick Join - Solo Insane Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
		ClientRegistry.registerKeyBinding(quickJoinSoloNormalSkywars);
		ClientRegistry.registerKeyBinding(quickJoinSoloInsaneSkywars);
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (quickJoinSoloNormalSkywars.isPressed()) {
			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
			entityPlayer.sendChatMessage("/play solo_normal");
		} else if (quickJoinSoloInsaneSkywars.isPressed()) {
			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
			entityPlayer.sendChatMessage("/play solo_insane");
		}
	}

}
