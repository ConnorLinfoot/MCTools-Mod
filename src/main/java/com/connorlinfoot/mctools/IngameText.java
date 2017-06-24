package com.connorlinfoot.mctools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
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
//        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
//            return;
//        }
//        render(mc, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
    }

    public void render(Minecraft minecraft, int width, int height) {
        EntityPlayer entityPlayer = minecraft.thePlayer;
//		String string = EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "WARNING: " + EnumChatFormatting.RED + "We believe a hacker has been found in this game! - " + entityPlayer.getPosition().toString();
//		int strWidth = minecraft.fontRendererObj.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(string));
//		minecraft.fontRendererObj.drawString(string, (width / 2) - (strWidth / 2), (height / 2) + 8, 0xFFFFFF, true);

        // Render text in bottom left
        if (!Minecraft.getMinecraft().gameSettings.showDebugInfo && MCTools.getMcTools().getConfigHandler().isDisplayGui()) {
            ArrayList<String> display = new ArrayList<>();

            display.add(EnumChatFormatting.GOLD + "MC Tools Beta v" + MCTools.VERSION);

            if (MCTools.getMcTools().getConfigHandler().isDisplayFPS())
                display.add(EnumChatFormatting.GOLD + "FPS: " + EnumChatFormatting.WHITE + Minecraft.getDebugFPS());

            if (MCTools.getMcTools().getConfigHandler().isDisplayCords()) {
                display.add(EnumChatFormatting.GOLD + "X: " + EnumChatFormatting.WHITE + Math.floor(entityPlayer.posX));
                display.add(EnumChatFormatting.GOLD + "Y: " + EnumChatFormatting.WHITE + Math.floor(entityPlayer.posY));
                display.add(EnumChatFormatting.GOLD + "Z: " + EnumChatFormatting.WHITE + Math.floor(entityPlayer.posZ));
            }

            int lineHeight = 4;
            for (String line : display) {
                minecraft.fontRendererObj.drawString(line, 4, lineHeight, 0xFFFFFF, true);
                lineHeight = lineHeight + 10;
            }

        }


    }

}
