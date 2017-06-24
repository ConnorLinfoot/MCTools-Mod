package com.connorlinfoot.mctools.Hypixel;

import net.minecraftforge.fml.client.FMLClientHandler;

public enum ChatChannel {
    ALL,
    PARTY,
    GUILD;

    public ChatChannel switchTo() {
        String channel = this.toString().toLowerCase();
        FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/chat " + channel);
        return this;
    }

    public static ChatChannel fromChar(String c) {
        c = c.toLowerCase();
        switch (c) {
            case "a":
                return ALL;
            case "p":
                return PARTY;
            case "g":
                return GUILD;
        }
        return ALL;
    }

}
