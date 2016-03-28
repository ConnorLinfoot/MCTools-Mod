package com.connorlinfoot.mctools.Hypixel;

import net.minecraft.util.text.TextFormatting;

public enum PlayerRank {
	DEFAULT,
	VIP,
	VIP_PLUS,
	MVP,
	MVP_PLUS,
	YOUTUBE,
	HELPER,
	MOD,
	ADMIN;

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
			case HELPER:
				return "Helper";
			case MOD:
				return "Moderator";
			case ADMIN:
				return "Admin";
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
			case HELPER:
				return TextFormatting.BLUE + "[HELPER] " + (includeReset ? TextFormatting.RESET : "");
			case MOD:
				return TextFormatting.DARK_GREEN + "[MOD] " + (includeReset ? TextFormatting.RESET : "");
			case ADMIN:
				return TextFormatting.RED + "[ADMIN] " + (includeReset ? TextFormatting.RESET : "");
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
			case HELPER:
				return TextFormatting.BLUE;
			case MOD:
				return TextFormatting.DARK_GREEN;
			case ADMIN:
				return TextFormatting.RED;
		}
		return TextFormatting.GRAY;
	}

	public static PlayerRank fromNiceName(String name) {
		switch (name.toUpperCase()) {
			default:
				return DEFAULT;
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
			case "HELPER":
				return HELPER;
			case "MOD":
				return MOD;
			case "ADMIN":
				return ADMIN;
		}
	}

}
