package com.connorlinfoot.mctools.Hypixel;

import com.connorlinfoot.mctools.MCTools;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	private Hypixel hypixel;
	private KeyBinding quickJoinSoloNormalSkywars;
	private KeyBinding quickJoinSoloInsaneSkywars;

	public Keybindings(Hypixel hypixel) {
		this.hypixel = hypixel;
		quickJoinSoloNormalSkywars = new KeyBinding("Quick Join - Solo Normal Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
		quickJoinSoloInsaneSkywars = new KeyBinding("Quick Join - Solo Insane Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
		ClientRegistry.registerKeyBinding(quickJoinSoloNormalSkywars);
		ClientRegistry.registerKeyBinding(quickJoinSoloInsaneSkywars);
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (quickJoinSoloNormalSkywars.isPressed()) {
			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
			if( hypixel.isCurrentlyOnHypixel() )
				entityPlayer.sendChatMessage("/play solo_normal");
			else
				entityPlayer.addChatMessage(new TextComponentString(MCTools.prefix + TextFormatting.RED + "You are currently not on Hypixel"));
		} else if (quickJoinSoloInsaneSkywars.isPressed()) {
			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
			if( hypixel.isCurrentlyOnHypixel() )
				entityPlayer.sendChatMessage("/play solo_insane");
			else
				entityPlayer.addChatMessage(new TextComponentString(MCTools.prefix + TextFormatting.RED + "You are currently not on Hypixel"));
		}
	}

}
