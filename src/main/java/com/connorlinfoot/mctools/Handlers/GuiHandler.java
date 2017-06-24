package com.connorlinfoot.mctools.Handlers;

import com.connorlinfoot.mctools.Hypixel.StatsGui;
import com.connorlinfoot.mctools.QuickActionsGUI;
import com.connorlinfoot.mctools.TextGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.UUID;

public class GuiHandler implements IGuiHandler {
    public static UUID playerClicked;
    public static final int HYPIXEL_STATS_GUI = 1;
    public static final int QUICK_ACTIONS_GUI = 2;
    public static final int TEXT_GUI = 3;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == HYPIXEL_STATS_GUI)
            return new StatsGui(playerClicked);
        else if (ID == QUICK_ACTIONS_GUI)
            return new QuickActionsGUI();
        else if (ID == TEXT_GUI)
            return new TextGUI();

        return null;
    }

}