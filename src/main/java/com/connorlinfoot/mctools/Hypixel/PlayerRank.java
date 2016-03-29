package com.connorlinfoot.mctools.Hypixel;

import net.minecraft.util.text.TextFormatting;

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
				return TextFormatting.GREEN + "[VIP] " + (includeReset ? TextFormatting.RESET : "");
			case VIP_PLUS:
				return TextFormatting.GREEN + "[VIP" + TextFormatting.GOLD + "+" + TextFormatting.GREEN + "] " + (includeReset ? TextFormatting.RESET : "");
			case MVP:
				return TextFormatting.AQUA + "[MVP] " + (includeReset ? TextFormatting.RESET : "");
			case MVP_PLUS:
				return TextFormatting.AQUA + "[MVP" + TextFormatting.RED + "+" + TextFormatting.AQUA + "] " + (includeReset ? TextFormatting.RESET : "");
			case YOUTUBE:
				return TextFormatting.GOLD + "[YT] " + (includeReset ? TextFormatting.RESET : "");
			case MOJANG:
				return TextFormatting.YELLOW + "[MOJANG] " + (includeReset ? TextFormatting.RESET : "");
			case MCPROHOSTING:
				return TextFormatting.GRAY + "[MCProHosting] " + (includeReset ? TextFormatting.RESET : ""); // Not sure what color this should be!
			case SLOTH:
				return TextFormatting.RED + "[SLOTH] " + (includeReset ? TextFormatting.RESET : "");
			case ANGUS:
				return TextFormatting.RED + "[ANGUS] " + (includeReset ? TextFormatting.RESET : "");
			case APPLE:
				return TextFormatting.GOLD + "[APPLE] " + (includeReset ? TextFormatting.RESET : "");
			case BUILDTEAM:
				return TextFormatting.DARK_AQUA + "[BUILDTEAM] " + (includeReset ? TextFormatting.RESET : "");
			case BUILDTEAM_PLUS:
				return TextFormatting.DARK_AQUA + "[BUILDTEAM+] " + (includeReset ? TextFormatting.RESET : "");
			case JR_HELPER:
				return TextFormatting.BLUE + "[JR HELPER] " + (includeReset ? TextFormatting.RESET : "");
			case HELPER:
				return TextFormatting.BLUE + "[HELPER] " + (includeReset ? TextFormatting.RESET : "");
			case MOD:
				return TextFormatting.DARK_GREEN + "[MOD] " + (includeReset ? TextFormatting.RESET : "");
			case ADMIN:
				return TextFormatting.RED + "[ADMIN] " + (includeReset ? TextFormatting.RESET : "");
			case OWNER:
				return TextFormatting.RED + "[OWNER] " + (includeReset ? TextFormatting.RESET : "");
		}
		return this.toString();
	}

	public TextFormatting getColor() {
		switch (this) {
			case DEFAULT:
				return TextFormatting.GRAY;
			case VIP:
			case VIP_PLUS:
				return TextFormatting.GREEN;
			case MVP:
			case MVP_PLUS:
				return TextFormatting.AQUA;
			case YOUTUBE:
				return TextFormatting.GOLD;
			case MOJANG:
				return TextFormatting.YELLOW;
			case MCPROHOSTING:
				return TextFormatting.GRAY; // Not actually sure?
			case SLOTH:
			case ANGUS:
				return TextFormatting.RED;
			case APPLE:
				return TextFormatting.GOLD;
			case BUILDTEAM:
			case BUILDTEAM_PLUS:
				return TextFormatting.DARK_AQUA;
			case JR_HELPER:
			case HELPER:
				return TextFormatting.BLUE;
			case MOD:
				return TextFormatting.DARK_GREEN;
			case ADMIN:
			case OWNER:
				return TextFormatting.RED;
		}
		return TextFormatting.GRAY;
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
