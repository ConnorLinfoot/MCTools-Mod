package com.connorlinfoot.mctools.Hypixel;

import com.connorlinfoot.mctools.MCTools;
import com.connorlinfoot.mctools.PlayerRender;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.util.Callback;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Hypixel {
	private boolean shouldBeRunning = false;
	private String subServer = "";
	private GameMode gameMode = GameMode.UNKNOWN;
	private boolean waitingForWhereAmI = false;
	private Map<UUID, PlayerReply> playerDataCache = new HashMap<UUID, PlayerReply>();
	//	private Map<UUID, Integer> swKillsCache = new HashMap<UUID, Integer>();
	private int swKillsUpdates = 0;
	private long waitUntil = System.currentTimeMillis();
	private long lastServerChecks = System.currentTimeMillis();

	public Hypixel() {
	}

	@SubscribeEvent
	public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		String ip = FMLClientHandler.instance().getClient().getCurrentServerData().serverIP;
		MCTools.getMcTools().outputDebug("Connecting to: " + ip);
		if (ip.toLowerCase().contains("hypixel.net"))
			shouldBeRunning = true;
	}

	@SubscribeEvent
	public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		MCTools.getMcTools().outputDebug("Disconnected from a server");
		shouldBeRunning = false;
	}

	@SubscribeEvent
	public void onChatRecieved(ClientChatReceivedEvent event) {
		if (!waitingForWhereAmI)
			return; // We don't care if the player ran the command or something
		String message = event.getMessage().getFormattedText();
		if (message.contains("You are currently on server")) {
			// We know that /whereami ran
//			event.setCanceled(true); // Don't show message to the player
			waitingForWhereAmI = false;
			event.setCanceled(true);
			subServer = message.substring(34, message.length() - 2);
			MCTools.getMcTools().outputDebug("Player was found to be on Hypixel subserver: " + subServer);
		} else if (message.contains("Unknown command")) {
			// Oops we must not be on Hypixel!
			MCTools.getMcTools().outputDebug("Oops, we must not be running on Hypixel?!");
			event.setCanceled(true);
			shouldBeRunning = false;
			waitingForWhereAmI = false;
		}
	}

	public void runWhereAmICommand() {
		waitingForWhereAmI = true;
		FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/whereami");
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public void tick(TickEvent.ClientTickEvent event) {
		if (!shouldBeRunning)
			return;
		// fire once per tick
		if (event.phase == TickEvent.Phase.START) return;
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
			// Find if the player is in a lobby or not
			if (lastServerChecks + 10 * 1000 < System.currentTimeMillis()) {
				subServer = "";
				lastServerChecks = System.currentTimeMillis();
			}

			if (subServer.equals("")) {
				if (waitingForWhereAmI)
					return;
				runWhereAmICommand();
				MCTools.getMcTools().outputDebug("Running /whereami");

				// Use scoreboard to try and work out the gamemode
				Scoreboard scoreboard = mc.thePlayer.getWorldScoreboard();
				ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(1);
				if( scoreObjective != null ) {
					if( scoreObjective.getDisplayName() != null ) {
						String gameModeString = TextFormatting.getTextWithoutFormattingCodes(scoreObjective.getDisplayName());
						GameMode oldGamemode = gameMode;
						gameMode = GameMode.UNKNOWN;
						try{
							gameMode = GameMode.valueOf(gameModeString.toUpperCase().replaceAll(" ", "_").replaceAll("-", "_"));
						} catch(Exception ignored) {}

						if(!oldGamemode.equals(gameMode)) {
							PlayerRender.aboveHeadCache.clear(); // Clear above head as game mode has changed!
						}

					}
				}
				return;
			}

			MCTools.getMcTools().outputDebug("You are currently playing: " + gameMode.toString());

			if (subServer.contains("lobby") && false) {
				MCTools.getMcTools().outputDebug("Not doing above heads because you are in a lobby");
				return;
			}

			if (MCTools.getMcTools().getConfigHandler().getAPIKey() == null || MCTools.getMcTools().getConfigHandler().getAPIKey().equals("NO_KEY") || MCTools.getMcTools().getConfigHandler().getAPIKey().equals("")) {
				MCTools.getMcTools().outputDebug("API Key has not been set!");
				return;
			}

			if (System.currentTimeMillis() < waitUntil) {
				if (swKillsUpdates > 0) {
					swKillsUpdates = 0;
				}
				return;
			}

			MCTools.getMcTools().outputDebug("Player Cache Size: " + playerDataCache.size());
			for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
				final UUID uuid = entityPlayer.getUniqueID();
				if (playerDataCache.containsKey(uuid)) {
					try {
						if (playerDataCache.get(uuid).getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills") == null)
							continue;
						double kills = playerDataCache.get(uuid).getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills").getAsInt();
						double deaths = playerDataCache.get(uuid).getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("deaths").getAsInt();
						PlayerRender.aboveHeadCache.put(uuid, "" + TextFormatting.AQUA + round(kills / deaths, 2) + " K/D");
					} catch (NullPointerException e) {
						MCTools.getMcTools().outputDebug("Getting player data for: " + uuid.toString());
						updateSWKills(uuid);
					}
				} else {
					MCTools.getMcTools().outputDebug("Getting player data for: " + uuid.toString());
					updateSWKills(uuid);
				}
			}
		}
	}

	public void updateSWKills(final UUID uuid) {
		if (swKillsUpdates >= 60) {
			waitUntil = System.currentTimeMillis() + 30 * 1000;
			return;
		}
		swKillsUpdates++;
		HypixelAPI.getInstance().getPlayer(null, uuid, new Callback<PlayerReply>(PlayerReply.class) {
			@Override
			public void callback(Throwable failCause, PlayerReply result) {
				if (failCause != null) {
					failCause.printStackTrace();
					return;
				}
				if (!result.isSuccess()) {
//					System.out.println("ERROR: " + result.getCause());
				}
				playerDataCache.put(uuid, result);
				double kills = playerDataCache.get(uuid).getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills").getAsInt();
				double deaths = playerDataCache.get(uuid).getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("deaths").getAsInt();
				PlayerRender.aboveHeadCache.put(uuid, "" + TextFormatting.AQUA + round(kills / deaths, 2) + " K/D");
//				swKillsCache.put(uuid, result.getPlayer().getAsJsonObject("stats").getAsJsonObject("SkyWars").get("kills").getAsInt());
			}
		});
	}

	public double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
