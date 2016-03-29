package com.connorlinfoot.mctools;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.UUID;

public class MCToolsCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "mctools";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))) {
			sender.addChatMessage(new TextComponentString(TextFormatting.AQUA + "MC Tools Forge Mod - Version " + MCTools.VERSION));
			sender.addChatMessage(new TextComponentString(TextFormatting.AQUA + "Created By Connor Linfoot"));
			sender.addChatMessage(new TextComponentString(TextFormatting.AQUA + "This command currently does nothing... but hey, it works!"));
			return;
		}

		if (args.length >= 1 && args[0].equalsIgnoreCase("hypixel")) {
			// Hypixel specific commands
			if (args.length >= 3 && args[1].equalsIgnoreCase("stats")) {
				GuiHandler.playerClicked = UUID.fromString(args[2]);
				EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
				entityPlayer.openGui(MCTools.getMcTools(), GuiHandler.HYPIXEL_STATS_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
			}
			return;
		}

		sender.addChatMessage(new TextComponentString(MCTools.prefix + TextFormatting.RED + "Unknown args"));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
