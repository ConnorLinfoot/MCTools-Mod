package com.connorlinfoot.mctools.Hypixel;

import com.connorlinfoot.mctools.MCTools;
import net.hypixel.api.reply.PlayerReply;

import java.util.UUID;

public class HypixelPlayer {
    private UUID uuid;
    private PlayerRank playerRank = PlayerRank.DEFAULT;
    private PlayerReply playerReply;

    public HypixelPlayer(UUID uuid, PlayerReply playerReply) {
        this.uuid = uuid;
        this.playerReply = playerReply;

        // Load the players rank from data
        String rank = null;
        if (playerReply.getPlayer().get("rank") != null)
            rank = playerReply.getPlayer().get("rank").getAsString();
        else if (playerReply.getPlayer().get("newPackageRank") != null)
            rank = playerReply.getPlayer().get("newPackageRank").getAsString();

        if (rank != null) {
            MCTools.getMcTools().outputDebug("Players Rank: " + rank);
            try {
                playerRank = PlayerRank.valueOf(rank);
            } catch (Exception ignored) {
            }
        }

    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerReply getPlayerReply() {
        return playerReply;
    }

    public PlayerRank getPlayerRank() {
        return playerRank;
    }

}
