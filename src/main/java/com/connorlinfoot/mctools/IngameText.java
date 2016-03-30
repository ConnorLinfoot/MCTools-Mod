package com.connorlinfoot.mctools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class IngameText extends Gui {
	private Minecraft mc;

	public IngameText(Minecraft mc) {
		super();
		this.mc = mc;
	}

	@SubscribeEvent
	public void renderScreen(RenderGameOverlayEvent.Post event) {
		if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
			return;
		}
		render(mc, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
	}

	public void render(Minecraft minecraft, int width, int height) {
		EntityPlayer entityPlayer = minecraft.thePlayer;
//		String string = TextFormatting.RED + "" + TextFormatting.BOLD + "WARNING: " + TextFormatting.RED + "We believe a hacker has been found in this game! - " + entityPlayer.getPosition().toString();
//		int strWidth = minecraft.fontRendererObj.getStringWidth(TextFormatting.getTextWithoutFormattingCodes(string));
//		minecraft.fontRendererObj.drawString(string, (width / 2) - (strWidth / 2), (height / 2) + 8, 0xFFFFFF, true);

		// Render text in bottom left
		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo && MCTools.getMcTools().getConfigHandler().isDisplayGui()) {
			ArrayList<String> display = new ArrayList<>();

			display.add(TextFormatting.GOLD + "MC Tools Beta v" + MCTools.VERSION);

			if( MCTools.getMcTools().getConfigHandler().isDisplayFPS() )
				display.add(TextFormatting.GOLD + "FPS: " + TextFormatting.WHITE + Minecraft.getDebugFPS());

			if( MCTools.getMcTools().getConfigHandler().isDisplayCords() ) {
				display.add(TextFormatting.GOLD + "X: " + TextFormatting.WHITE + Math.floor(entityPlayer.posX));
				display.add(TextFormatting.GOLD + "Y: " + TextFormatting.WHITE + Math.floor(entityPlayer.posY));
				display.add(TextFormatting.GOLD + "Z: " + TextFormatting.WHITE + Math.floor(entityPlayer.posZ));
			}

			int lineHeight = 4;
			for( String line : display ) {
				minecraft.fontRendererObj.drawString(line, 4, lineHeight, 0xFFFFFF, true);
				lineHeight = lineHeight + 10;
			}

		}


	}

}
