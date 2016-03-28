package com.connorlinfoot.mctools.Hypixel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.UUID;

public class GuiHandler implements IGuiHandler {
	public static UUID playerClicked;
	public static final int MOD_TILE_ENTITY_GUI = 25;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == MOD_TILE_ENTITY_GUI)
			return new StatsGui(playerClicked);

		return null;
	}

}