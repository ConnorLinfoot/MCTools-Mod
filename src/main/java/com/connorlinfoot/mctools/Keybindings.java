package com.connorlinfoot.mctools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	private MCTools mcTools;
	private KeyBinding toggleSneak;
	private boolean sneaked = false;

	public Keybindings(MCTools mcTools) {
		this.mcTools = mcTools;
		toggleSneak = new KeyBinding("Toggle Sneak", Keyboard.KEY_NONE, "MC Tools - General");
		ClientRegistry.registerKeyBinding(toggleSneak);
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (toggleSneak.isPressed()) {
			sneaked = !sneaked;
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), sneaked);
		}
	}

}
