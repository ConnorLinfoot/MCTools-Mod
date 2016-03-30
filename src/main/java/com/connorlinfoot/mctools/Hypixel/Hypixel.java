package com.connorlinfoot.mctools.Hypixel;

import com.connorlinfoot.mctools.Handlers.GuiHandler;
import com.connorlinfoot.mctools.Listeners.PlayerRender;
import com.connorlinfoot.mctools.MCTools;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.util.Callback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hypixel {
	private boolean currentlyOnHypixel = false;
	private boolean playerInGame = false;
	private String subServer = "";
	private GameMode gameMode = GameMode.UNKNOWN;
	private boolean waitingForWhereAmI = false;
	private ArrayList<UUID> ignore = new ArrayList<UUID>(); // This is where we store UUID's that should not be looked up, these are usually NPC's
	private ArrayList<UUID> pending = new ArrayList<UUID>();
	private Map<UUID, HypixelPlayer> hypixelPlayers = new HashMap<UUID, HypixelPlayer>();
	private int swKillsUpdates = 0;
	private long waitUntil = System.currentTimeMillis();
	private long lastServerChecks = System.currentTimeMillis();

	public Hypixel() {
	}

	@SubscribeEvent
	public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		if (FMLClientHandler.instance().getClient().getCurrentServerData() == null)
			return;
		String ip = FMLClientHandler.instance().getClient().getCurrentServerData().serverIP;
		MCTools.getMcTools().outputDebug("Connecting to: " + ip);
		if (ip.toLowerCase().contains("hypixel.net"))
			currentlyOnHypixel = true;
	}

	@SubscribeEvent
	public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		MCTools.getMcTools().outputDebug("Disconnected from a server");
		currentlyOnHypixel = false;
	}

	@SubscribeEvent
	public void onChatRecieved(ClientChatReceivedEvent event) {
		if (!currentlyOnHypixel)
			return;

		// Try and get the chat string to make names clickable
		if (!playerInGame) {
			Pattern rankPattern = Pattern.compile("\\[(.*)\\] ([^\\s]+): (.*)"); // Because im bad with regex we will do 2 tests, one with rank and one without
			Pattern noRankPattern = Pattern.compile("([^\\s]+): (.*)");
			Matcher rankMatcher = rankPattern.matcher(TextFormatting.getTextWithoutFormattingCodes(event.getMessage().getFormattedText()));
			Matcher noRankMatcher = noRankPattern.matcher(TextFormatting.getTextWithoutFormattingCodes(event.getMessage().getFormattedText()));
			String rank = null;
			String username = null;
			String chatMessage = null;
			if (rankMatcher.matches()) {
				rank = rankMatcher.group(1);
				username = rankMatcher.group(2);
				chatMessage = rankMatcher.group(3);
			} else if (noRankMatcher.matches()) {
				username = rankMatcher.group(1);
				chatMessage = rankMatcher.group(2);
			}

			if (username != null && chatMessage != null) {
				// Let's develop our own chat message! :D
				ITextComponent finalComponent = new TextComponentString("");
				PlayerRank playerRank = PlayerRank.DEFAULT;
				if (rank != null) {
					playerRank = PlayerRank.fromNiceName(rank);
					ITextComponent rankComponent = new TextComponentString(playerRank.getPrefix(true));
					finalComponent.appendSibling(rankComponent);
				}

				ITextComponent usernameComponent = new TextComponentString(playerRank.getColor() + username);
				EntityPlayer theChatPlayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username);
				UUID uuid = theChatPlayer.getUniqueID(); // Our UUID for now!
				usernameComponent.setChatStyle(new Style().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "mctools hypixel stats " + uuid)));
				finalComponent.appendSibling(usernameComponent).appendText(": ");

				ITextComponent messageComponent = new TextComponentString(chatMessage);
				finalComponent.appendSibling(messageComponent);

				event.setMessage(finalComponent);
			}
		}

		if (!waitingForWhereAmI)
			return; // We don't care if the player ran the command or something
		String message = event.getMessage().getFormattedText();
		if (message.contains("You are currently on server")) {
			// We know that /whereami ran
//			event.setCanceled(true); // Don't show message to the player
			waitingForWhereAmI = false;
			event.setCanceled(true);
			subServer = message.substring(34, message.length() - 2);
			playerInGame = !subServer.contains("lobby");
			MCTools.getMcTools().outputDebug("Player was found to be on Hypixel subserver: " + subServer);
		} else if (message.contains("Unknown command")) {
			// Oops we must not be on Hypixel!
			MCTools.getMcTools().outputDebug("Oops, we must not be running on Hypixel?!");
			event.setCanceled(true);
			currentlyOnHypixel = false;
			waitingForWhereAmI = false;
		}
	}

	public void runWhereAmICommand() {
		waitingForWhereAmI = true;
		FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/whereami");
	}

	@SubscribeEvent
	public void fixHypixelSwordShieldBug(RenderHandEvent event) {
		if (!currentlyOnHypixel)
			return;
		if (Minecraft.getMinecraft().thePlayer != null) {
			if (Minecraft.getMinecraft().thePlayer.getHeldItemOffhand() != null)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public void tick(TickEvent.ClientTickEvent event) {
		if (!currentlyOnHypixel)
			return;
		// fire once per tick
		if (event.phase == TickEvent.Phase.START) return;
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
			// Find if the player is in a lobby or not
			if (lastServerChecks + 5 * 1000 < System.currentTimeMillis()) {
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
				if (scoreObjective != null) {
					if (scoreObjective.getDisplayName() != null) {
						String gameModeString = TextFormatting.getTextWithoutFormattingCodes(scoreObjective.getDisplayName());
						GameMode oldGamemode = gameMode;
						gameMode = GameMode.UNKNOWN;
						try {
							gameMode = GameMode.valueOf(gameModeString.toUpperCase().replaceAll(" ", "_").replaceAll("-", "_"));
						} catch (Exception ignored) {
						}

						if (!oldGamemode.equals(gameMode)) {
							PlayerRender.aboveHeadCache.clear(); // Clear above head as game mode has changed!
						}

					}
				}
				return;
			}

			MCTools.getMcTools().outputDebug("You are currently playing: " + gameMode.getNiceName());

			if (subServer.contains("lobby")) {
				MCTools.getMcTools().outputDebug("Not loading data because you are in a lobby");
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

			MCTools.getMcTools().outputDebug("Player Cache Size: " + hypixelPlayers.size());
			for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
				final UUID uuid = entityPlayer.getUniqueID();
				if (ignore.contains(uuid))
					continue;
				if (hypixelPlayers.containsKey(uuid)) {
					try {
						if (hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills") == null)
							continue;
						double kills = hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills").getAsInt();
						double deaths = hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("deaths").getAsInt();
						PlayerRender.aboveHeadCache.put(uuid, "" + TextFormatting.AQUA + round(kills / deaths, 2) + " K/D");
					} catch (NullPointerException e) {
						MCTools.getMcTools().outputDebug("Getting player data for: " + uuid.toString() + " (" + entityPlayer.getName() + ")");
						updatePlayerData(uuid);
					}
				} else {
					MCTools.getMcTools().outputDebug("Getting player data for: " + uuid.toString() + " (" + entityPlayer.getName() + ")");
					updatePlayerData(uuid);
				}
			}
		}
	}

	public void updatePlayerData(final UUID uuid) {
		if (swKillsUpdates >= 60) {
			waitUntil = System.currentTimeMillis() + 30 * 1000;
			return;
		}
		if (pending.contains(uuid) || ignore.contains(uuid))
			return;
		pending.add(uuid);
		swKillsUpdates++;
		HypixelAPI.getInstance().getPlayer(null, uuid, new Callback<PlayerReply>(PlayerReply.class) {
			@Override
			public void callback(Throwable failCause, PlayerReply result) {
				pending.remove(uuid);
				if (failCause != null) {
					failCause.printStackTrace();
					return;
				}
				if (!result.isSuccess()) {
					System.out.println("ERROR: " + result.getCause());
					return;
				}
				if (result.getPlayer() == null && result.getCause() == null && result.isSuccess()) {
					// We assume it's because the UUID belongs to an NPC
					ignore.add(uuid);
					return;
				}
				HypixelPlayer hypixelPlayer = new HypixelPlayer(uuid, result);
				hypixelPlayers.put(uuid, hypixelPlayer);
				double kills = hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills").getAsInt();
				double deaths = hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("deaths").getAsInt();
				PlayerRender.aboveHeadCache.put(uuid, "" + TextFormatting.AQUA + round(kills / deaths, 2) + " K/D");
			}
		});
	}

	@SubscribeEvent
	public void onInteract(EntityInteractEvent event) {
		if (!currentlyOnHypixel)
			return;
		if (event.getTarget() instanceof EntityPlayer && event.getEntityPlayer().isSneaking()) {
			EntityPlayer attacked = (EntityPlayer) event.getTarget();
//			FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/party invite " + attacked.getName());
			GuiHandler.playerClicked = attacked.getUniqueID();
			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
			entityPlayer.openGui(MCTools.getMcTools(), GuiHandler.HYPIXEL_STATS_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
		}

	}

	public double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public Map<UUID, HypixelPlayer> getHypixelPlayers() {
		return hypixelPlayers;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public boolean isCurrentlyOnHypixel() {
		return currentlyOnHypixel;
	}

}
