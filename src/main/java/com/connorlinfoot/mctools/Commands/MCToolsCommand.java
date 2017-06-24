package com.connorlinfoot.mctools.Commands;

import com.connorlinfoot.mctools.Handlers.GuiHandler;
import com.connorlinfoot.mctools.Listeners.PlayerRender;
import com.connorlinfoot.mctools.MCTools;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "MC Tools Forge Mod - Version " + MCTools.VERSION));
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Created By Connor Linfoot"));
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "This command currently does nothing... but hey, it works!"));
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
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("ping")) {
            EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Pong!"));
            return;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("clear")) {
            PlayerRender.particles.clear();
            MCTools.particleFetchHandler.clear();

            return;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("run")) {
            MCTools.particleFetchHandler.run();
            return;
        }
//        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Cleared particles"));
        sender.addChatMessage(new ChatComponentText(MCTools.prefix + EnumChatFormatting.RED + "Unknown args"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
