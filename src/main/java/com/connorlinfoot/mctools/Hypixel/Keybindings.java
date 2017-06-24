package com.connorlinfoot.mctools.Hypixel;

import com.connorlinfoot.mctools.MCTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Keybindings {
    private Hypixel hypixel;
    private KeyBinding quickJoinRankedSkywars;
    private KeyBinding quickJoinSoloNormalSkywars;
    private KeyBinding quickJoinSoloInsaneSkywars;
    private KeyBinding quickJoinTeamNormalSkywars;
    private KeyBinding quickJoinTeamInsaneSkywars;
    private KeyBinding quickJoinMegaSkywars;

    public Keybindings(Hypixel hypixel) {
        this.hypixel = hypixel;
        quickJoinRankedSkywars = new KeyBinding("Quick Join - Ranked Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
        quickJoinSoloNormalSkywars = new KeyBinding("Quick Join - Solo Normal Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
        quickJoinSoloInsaneSkywars = new KeyBinding("Quick Join - Solo Insane Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
        quickJoinTeamNormalSkywars = new KeyBinding("Quick Join - Team Normal Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
        quickJoinTeamInsaneSkywars = new KeyBinding("Quick Join - Team Insane Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
        quickJoinMegaSkywars = new KeyBinding("Quick Join - Mega Skywars", Keyboard.KEY_NONE, "MC Tools - Hypixel");
        ClientRegistry.registerKeyBinding(quickJoinRankedSkywars);
        ClientRegistry.registerKeyBinding(quickJoinSoloNormalSkywars);
        ClientRegistry.registerKeyBinding(quickJoinSoloInsaneSkywars);
        ClientRegistry.registerKeyBinding(quickJoinTeamNormalSkywars);
        ClientRegistry.registerKeyBinding(quickJoinTeamInsaneSkywars);
        ClientRegistry.registerKeyBinding(quickJoinMegaSkywars);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (quickJoinRankedSkywars.isPressed()) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            if (hypixel.isCurrentlyOnHypixel())
                entityPlayer.sendChatMessage("/play ranked_normal");
            else
                entityPlayer.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "You are currently not on Hypixel"));
        } else if (quickJoinSoloNormalSkywars.isPressed()) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            if (hypixel.isCurrentlyOnHypixel())
                entityPlayer.sendChatMessage("/play solo_normal");
            else
                entityPlayer.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "You are currently not on Hypixel"));
        } else if (quickJoinSoloInsaneSkywars.isPressed()) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            if (hypixel.isCurrentlyOnHypixel())
                entityPlayer.sendChatMessage("/play solo_insane");
            else
                entityPlayer.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "You are currently not on Hypixel"));
        } else if (quickJoinTeamNormalSkywars.isPressed()) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            if (hypixel.isCurrentlyOnHypixel())
                entityPlayer.sendChatMessage("/play teams_normal");
            else
                entityPlayer.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "You are currently not on Hypixel"));
        } else if (quickJoinTeamInsaneSkywars.isPressed()) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            if (hypixel.isCurrentlyOnHypixel())
                entityPlayer.sendChatMessage("/play teams_insane");
            else
                entityPlayer.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "You are currently not on Hypixel"));
        } else if (quickJoinMegaSkywars.isPressed()) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
            if (true) return;
            if (hypixel.isCurrentlyOnHypixel())
                entityPlayer.sendChatMessage("/play mega_normal");
            else
                entityPlayer.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "You are currently not on Hypixel"));
        }
    }

}
