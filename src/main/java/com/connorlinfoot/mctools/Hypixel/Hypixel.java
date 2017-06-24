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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hypixel {
    private boolean run_secondary_tasks = false;
    private boolean currentlyOnHypixel = false;
    private boolean runningAutoGG = false;
    private boolean playerInGame = false;
    private boolean connectedToAPI = false;
    private String subServer = "";
    private GameMode gameMode = GameMode.UNKNOWN;
    private boolean waitingForWhereAmI = false;
    private ArrayList<UUID> ignore = new ArrayList<>(); // This is where we store playerUUID's that should not be looked up, these are usually NPC's
    private ArrayList<UUID> pending = new ArrayList<>();
    private Map<UUID, HypixelPlayer> hypixelPlayers = new HashMap<>();
    private int swKillsUpdates = 0;
    private long waitUntil = System.currentTimeMillis();
    private long last5SecondRun = System.currentTimeMillis();
    private ArrayList<String> ggTriggers = new ArrayList<>();
    private ChatChannel currentChat = ChatChannel.ALL;

    public Hypixel() {
        loadGGTriggers();
        FMLCommonHandler.instance().bus().register(new Keybindings(this)); // Register key bindings
    }

    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (FMLClientHandler.instance().getClient().getCurrentServerData() == null)
            return;
        String ip = FMLClientHandler.instance().getClient().getCurrentServerData().serverIP;
        System.out.println("[MCTools] Connecting to: " + ip);
        if (ip.toLowerCase().contains("hypixel.net"))
            currentlyOnHypixel = true;
    }

    @SubscribeEvent
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        MCTools.getMcTools().outputDebug("Disconnected from a server");
        currentlyOnHypixel = false;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!currentlyOnHypixel || event.type != 0)
            return;
        String message = event.message.getUnformattedText();

        // Keep track of chat channel
        if (runningAutoGG && (message.startsWith("You're already in this channel") || message.startsWith("You are now in the"))) {
            event.setCanceled(true);
        } else if (message.startsWith("You are now in the ")) {
            currentChat = ChatChannel.fromChar(message.substring(19, 20));
        } else if (message.startsWith("The party leader") && message.contains("party has been disbanded")) {
            currentChat = ChatChannel.ALL;
        } else if (message.startsWith("You are not in a") && message.contains("and have been moved to")) {
            currentChat = ChatChannel.ALL;
        }

        // Auto GG
        if (playerInGame && MCTools.getMcTools().getConfigHandler().isAutoGG() && !getGGTriggers().isEmpty() && !runningAutoGG) {
            if (!runningAutoGG) {
                for (String trigger : getGGTriggers()) {
                    if (message.contains(trigger)) {
                        runningAutoGG = true;
                        new Thread() {
                            public void run() {
                                try {
                                    ChatChannel.ALL.switchTo();
                                    Thread.sleep(200L);
                                    FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("gg");
                                    if (currentChat != ChatChannel.ALL) {
                                        Thread.sleep(200L);
                                        currentChat.switchTo();
                                    }
                                    Thread.sleep(1000L);
                                    runningAutoGG = false;
                                } catch (Exception ignored) {

                                }
                            }
                        }.start();
                        break;
                    }
                }
            }
        }

        if (!waitingForWhereAmI)
            return; // We don't care if the player ran the command or something

        if (message.contains("You are currently on server")) {
            // We know that /whereami ran
            waitingForWhereAmI = false;
            event.setCanceled(true);
            subServer = message.substring(28, message.length());
            playerInGame = !subServer.contains("lobby");
            MCTools.getMcTools().outputDebug("Player was found to be on Hypixel subserver: " + subServer);
        } else if (message.contains("Unknown command")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Oops, we must not be running on Hypixel?"));
            event.setCanceled(true);
            currentlyOnHypixel = false;
            waitingForWhereAmI = false;
        }
    }

    private void runWhereAmICommand() {
        waitingForWhereAmI = true;
        FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/whereami");
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
    public void tick(TickEvent.ClientTickEvent event) {
        if (!currentlyOnHypixel)
            return;

        // fire once per tick
        if (event.phase == TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
            // Run the below once every 5 seconds
            if (last5SecondRun + 5 * 1000 < System.currentTimeMillis()) {
                run_secondary_tasks = true;
                last5SecondRun = System.currentTimeMillis();

                // Find if the player is in a lobby or not
                if (!waitingForWhereAmI) {
                    runWhereAmICommand();
                    MCTools.getMcTools().outputDebug("Running /whereami");
                }

                // Use scoreboard to try and work out the gamemode
                Scoreboard scoreboard = mc.thePlayer.getWorldScoreboard();
                ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(1);
                if (scoreObjective != null) {
                    if (scoreObjective.getDisplayName() != null) {
                        String gameModeString = EnumChatFormatting.getTextWithoutFormattingCodes(scoreObjective.getDisplayName());
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

            if (run_secondary_tasks) {
                run_secondary_tasks = false;

                if (getGameMode() == GameMode.UNKNOWN) {
                    MCTools.getMcTools().outputDebug("We don't know what game you're playing!");
                    return;
                }

                MCTools.getMcTools().outputDebug("You are currently playing: " + gameMode.getNiceName());

                if (!playerInGame) {
                    MCTools.getMcTools().outputDebug("Not loading data because you are in a lobby");
                    return;
                }

                if (MCTools.getMcTools().getConfigHandler().getAPIKey() == null || MCTools.getMcTools().getConfigHandler().getAPIKey().equals("NO_KEY") || MCTools.getMcTools().getConfigHandler().getAPIKey().equals("")) {
                    MCTools.getMcTools().outputDebug("API Key has not been set!");
                    // TODO Close GUI if open here
                    return;
                }

                if (!isConnectedToAPI()) {
                    MCTools.getMcTools().outputDebug("MC Tools is currently not connected to the Hypixel API");
                    // TODO Close GUI if open here
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
                            PlayerRender.aboveHeadCache.put(uuid, "" + EnumChatFormatting.AQUA + round(kills / deaths, 2) + " K/D");
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
                    System.out.println("Hypixel API Error: " + failCause.getMessage());
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Hypixel API Error: " + failCause.getMessage()));
                    return;
                }
                if (!result.isSuccess()) {
                    System.out.println("ERROR: " + result.getCause());
                    return;
                }
                if (result.getPlayer() == null && result.getCause() == null && result.isSuccess()) {
                    // We assume it's because the playerUUID belongs to an NPC
                    ignore.add(uuid);
                    return;
                }
                HypixelPlayer hypixelPlayer = new HypixelPlayer(uuid, result);
                hypixelPlayers.put(uuid, hypixelPlayer);
                double kills = hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("kills").getAsInt();
                double deaths = hypixelPlayers.get(uuid).getPlayerReply().getPlayer().getAsJsonObject("stats").getAsJsonObject(gameMode.getAPIName()).get("deaths").getAsInt();
                PlayerRender.aboveHeadCache.put(uuid, "" + EnumChatFormatting.AQUA + round(kills / deaths, 2) + " K/D");
            }
        });
    }

    @SubscribeEvent
    public void onInteract(EntityInteractEvent event) {
        if (!currentlyOnHypixel || playerInGame)
            return;
        if (event.target instanceof EntityPlayer && event.entityPlayer.isSneaking()) {
            System.out.println(event.target.getName());
            EntityPlayer attacked = (EntityPlayer) event.target;
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

    public boolean isConnectedToAPI() {
        return connectedToAPI;
    }

    public void setConnectedToAPI(boolean connectedToAPI) {
        this.connectedToAPI = connectedToAPI;
    }

    private void loadGGTriggers() {
        System.out.println("Loading gg triggers...");
        try {
            String raw = IOUtils.toString(new URL("http://api.connorlinfoot.com/v1/ggtriggers/?plain"));
            ggTriggers = new ArrayList(Arrays.asList(raw.split("\n")));
            System.out.println(ggTriggers);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public ArrayList<String> getGGTriggers() {
        return ggTriggers;
    }

}
