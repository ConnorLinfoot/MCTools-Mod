package com.connorlinfoot.mctools.Hypixel;

import net.minecraft.util.EnumChatFormatting;

/**
 * Information from http://hypixelserver.wikia.com/wiki/Ranks
 */

public enum PlayerRank {
    DEFAULT,
    VIP,
    VIP_PLUS,
    MVP,
    MVP_PLUS,
    YOUTUBE,
    MOJANG,
    MCPROHOSTING,
    SLOTH,
    ANGUS,
    APPLE,
    BUILDTEAM,
    BUILDTEAM_PLUS,
    JR_HELPER,
    HELPER,
    MOD,
    ADMIN,
    OWNER;

    public String getNiceName() {
        switch (this) {
            case DEFAULT:
                return "Default"; // Not sure what to actually have here?
            case VIP:
                return "VIP";
            case VIP_PLUS:
                return "VIP+";
            case MVP:
                return "MVP";
            case MVP_PLUS:
                return "MVP+";
            case YOUTUBE:
                return "YouTube";
            case MOJANG:
                return "Mojang";
            case MCPROHOSTING:
                return "MCProHosting";
            case SLOTH:
                return "Sloth";
            case ANGUS:
                return "Angus";
            case APPLE:
                return "Apple";
            case BUILDTEAM:
                return "Build Team";
            case BUILDTEAM_PLUS:
                return "Build Team + (Admin)";
            case JR_HELPER:
                return "JR Helper";
            case HELPER:
                return "Helper";
            case MOD:
                return "Moderator";
            case ADMIN:
                return "Admin";
            case OWNER:
                return "Owner";
        }
        return this.toString();
    }

    // TODO Actually use GOLD/RED for the +
    public String getPrefix(boolean includeReset) {
        switch (this) {
            case DEFAULT:
                return "";
            case VIP:
                return EnumChatFormatting.GREEN + "[VIP] " + (includeReset ? EnumChatFormatting.RESET : "");
            case VIP_PLUS:
                return EnumChatFormatting.GREEN + "[VIP" + EnumChatFormatting.GOLD + "+" + EnumChatFormatting.GREEN + "] " + (includeReset ? EnumChatFormatting.RESET : "");
            case MVP:
                return EnumChatFormatting.AQUA + "[MVP] " + (includeReset ? EnumChatFormatting.RESET : "");
            case MVP_PLUS:
                return EnumChatFormatting.AQUA + "[MVP" + EnumChatFormatting.RED + "+" + EnumChatFormatting.AQUA + "] " + (includeReset ? EnumChatFormatting.RESET : "");
            case YOUTUBE:
                return EnumChatFormatting.GOLD + "[YT] " + (includeReset ? EnumChatFormatting.RESET : "");
            case MOJANG:
                return EnumChatFormatting.YELLOW + "[MOJANG] " + (includeReset ? EnumChatFormatting.RESET : "");
            case MCPROHOSTING:
                return EnumChatFormatting.GRAY + "[MCProHosting] " + (includeReset ? EnumChatFormatting.RESET : ""); // Not sure what color this should be!
            case SLOTH:
                return EnumChatFormatting.RED + "[SLOTH] " + (includeReset ? EnumChatFormatting.RESET : "");
            case ANGUS:
                return EnumChatFormatting.RED + "[ANGUS] " + (includeReset ? EnumChatFormatting.RESET : "");
            case APPLE:
                return EnumChatFormatting.GOLD + "[APPLE] " + (includeReset ? EnumChatFormatting.RESET : "");
            case BUILDTEAM:
                return EnumChatFormatting.DARK_AQUA + "[BUILDTEAM] " + (includeReset ? EnumChatFormatting.RESET : "");
            case BUILDTEAM_PLUS:
                return EnumChatFormatting.DARK_AQUA + "[BUILDTEAM+] " + (includeReset ? EnumChatFormatting.RESET : "");
            case JR_HELPER:
                return EnumChatFormatting.BLUE + "[JR HELPER] " + (includeReset ? EnumChatFormatting.RESET : "");
            case HELPER:
                return EnumChatFormatting.BLUE + "[HELPER] " + (includeReset ? EnumChatFormatting.RESET : "");
            case MOD:
                return EnumChatFormatting.DARK_GREEN + "[MOD] " + (includeReset ? EnumChatFormatting.RESET : "");
            case ADMIN:
                return EnumChatFormatting.RED + "[ADMIN] " + (includeReset ? EnumChatFormatting.RESET : "");
            case OWNER:
                return EnumChatFormatting.RED + "[OWNER] " + (includeReset ? EnumChatFormatting.RESET : "");
        }
        return this.toString();
    }

    public EnumChatFormatting getColor() {
        switch (this) {
            case DEFAULT:
                return EnumChatFormatting.GRAY;
            case VIP:
            case VIP_PLUS:
                return EnumChatFormatting.GREEN;
            case MVP:
            case MVP_PLUS:
                return EnumChatFormatting.AQUA;
            case YOUTUBE:
                return EnumChatFormatting.GOLD;
            case MOJANG:
                return EnumChatFormatting.YELLOW;
            case MCPROHOSTING:
                return EnumChatFormatting.GRAY; // Not actually sure?
            case SLOTH:
            case ANGUS:
                return EnumChatFormatting.RED;
            case APPLE:
                return EnumChatFormatting.GOLD;
            case BUILDTEAM:
            case BUILDTEAM_PLUS:
                return EnumChatFormatting.DARK_AQUA;
            case JR_HELPER:
            case HELPER:
                return EnumChatFormatting.BLUE;
            case MOD:
                return EnumChatFormatting.DARK_GREEN;
            case ADMIN:
            case OWNER:
                return EnumChatFormatting.RED;
        }
        return EnumChatFormatting.GRAY;
    }

    public static PlayerRank fromNiceName(String name) {
        switch (name.toUpperCase()) {
            default:
                return valueOf(name.toUpperCase().replaceAll(" ", "_"));
            case "VIP":
                return VIP;
            case "VIP+":
                return VIP_PLUS;
            case "MVP":
                return MVP;
            case "MVP+":
                return MVP_PLUS;
            case "YT":
                return YOUTUBE;
        }
    }

}
