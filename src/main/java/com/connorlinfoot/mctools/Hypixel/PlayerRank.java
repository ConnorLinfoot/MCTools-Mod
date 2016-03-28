package com.connorlinfoot.mctools.Hypixel;

import net.minecraft.util.text.TextFormatting;

public enum PlayerRank {
	DEFAULT,
	VIP,
	VIP_PLUS,
	MVP,
	MVP_PLUS;

	public String getNiceName() {
		switch (this) {
			case DEFAULT:
				return "Player"; // Not sure what to actually have here?
			case VIP:
				return "VIP";
			case VIP_PLUS:
				return "VIP+";
			case MVP:
				return "MVP";
			case MVP_PLUS:
				return "MVP+";
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
				return TextFormatting.GREEN + "[VIP+] " + (includeReset ? TextFormatting.RESET : "");
			case MVP:
				return TextFormatting.AQUA + "[MVP] " + (includeReset ? TextFormatting.RESET : "");
			case MVP_PLUS:
				return TextFormatting.AQUA + "[MVP+] " + (includeReset ? TextFormatting.RESET : "");
		}
		return this.toString();
	}

}
