package com.connorlinfoot.mctools;

import com.connorlinfoot.mctools.Commands.MCToolsCommand;
import com.connorlinfoot.mctools.HUD.Armor;
import com.connorlinfoot.mctools.Handlers.ConfigHandler;
import com.connorlinfoot.mctools.Handlers.GuiHandler;
import com.connorlinfoot.mctools.Handlers.ParticleFetchHandler;
import com.connorlinfoot.mctools.Hypixel.Hypixel;
import com.connorlinfoot.mctools.Listeners.PlayerRender;
import net.hypixel.api.HypixelAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Mod(modid = MCTools.MODID, version = MCTools.VERSION, guiFactory = "com.connorlinfoot.mctools.Handlers.MCToolsGuiFactory", clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class MCTools {
    private static MCTools mcTools;
    public static final String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.GOLD + "MC Tools" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET;
    public static final String MODID = "mctools";
    public static final String VERSION = "0.2";
    private boolean isPlayerReal = false; // Used to know if the player is actually authenticated via the Mojang API
    private ConfigHandler configHandler;
    private Hypixel hypixel = new Hypixel();
    public static ParticleFetchHandler particleFetchHandler;
    public static UUID playerUUID;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        mcTools = this;
        FMLCommonHandler.instance().bus().register(new Keybindings(this)); // Register general key bindings
        configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(hypixel);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        particleFetchHandler = new ParticleFetchHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(mcTools, new GuiHandler());
//		quickActions = new KeyBinding("Quick Actions", Keyboard.KEY_X, "MC Tools");
//		ClientRegistry.registerKeyBinding(quickActions);

        // Command test
        ClientCommandHandler.instance.registerCommand(new MCToolsCommand());
        MinecraftForge.EVENT_BUS.register(new IngameText(Minecraft.getMinecraft()));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        checkIfPlayerIsReal();
    }

    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        checkForUpdates();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
//		if (quickActions.isPressed()) {
//			EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
//			if (entityPlayer.isSneaking() && getConfigHandler().isDebug()) {
//				entityPlayer.openGui(mcTools, GuiHandler.TEXT_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
//			} else {
//				if (!hypixel.isCurrentlyOnHypixel()) {
//					entityPlayer.addChatMessage(new TextComponentString(prefix + TextFormatting.RED + "You are not currently on any supported servers"));
//					return;
//				}
//				entityPlayer.openGui(mcTools, GuiHandler.QUICK_ACTIONS_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
//			}
//		}
    }

    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        Minecraft minecraft = FMLClientHandler.instance().getClient();
        playerUUID = minecraft.getSession().getProfile().getId();
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(new PlayerRender());

        try {
            String key = configHandler.getAPIKey();
            UUID apiKey = null;
            try {
                apiKey = UUID.fromString(key);
            } catch (Exception ignored) {
            }
            if (apiKey != null) {
                HypixelAPI.getInstance().setApiKey(apiKey);
                hypixel.setConnectedToAPI(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MCTools getMcTools() {
        return mcTools;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public void outputDebug(String message) {
        if (!getConfigHandler().isDebug())
            return;
        System.out.println("[MC TOOLS] [DEBUG] " + message);
    }

    //	@SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        EntityPlayerSP entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
        entityPlayer.openGui(mcTools, GuiHandler.HYPIXEL_STATS_GUI, entityPlayer.getEntityWorld(), 0, 0, 0);
    }

    public Hypixel getHypixel() {
        return hypixel;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
    public void tick(TickEvent.ClientTickEvent event) {
        // fire once per tick
        if (event.phase == TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
            // Find if the player is in a lobby or not
            for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                final UUID uuid = entityPlayer.getUniqueID();
                particleFetchHandler.addUUID(uuid);
            }

            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArrow) {
                    EntityArrow arrow = (EntityArrow) entity;
                    if (arrow.shootingEntity != null) {
                        UUID uuid = arrow.shootingEntity.getUniqueID();
                        if (!PlayerRender.particles.containsKey(uuid))
                            continue;
                        EnumParticleTypes particle = PlayerRender.particles.get(uuid);
                        Minecraft.getMinecraft().theWorld.spawnParticle(particle, arrow.posX, arrow.posY, arrow.posZ, 0.0D, 0.0D, 0.0D);
                        Minecraft.getMinecraft().theWorld.spawnParticle(particle, arrow.posX, arrow.posY, arrow.posZ, 0.0D, 0.0D, 0.0D);
                        Minecraft.getMinecraft().theWorld.spawnParticle(particle, arrow.posX, arrow.posY, arrow.posZ, 0.0D, 0.0D, 0.0D);
                    }
                }
            }

            particleFetchHandler.run();
        }
    }

    private void checkIfPlayerIsReal() {
        /**
         * Don't even bother trying to edit this to bypass any API mumbo-jumbo, it has 2 layers of checks to prevent people modifying particles of other players!
         */

        String username = Minecraft.getMinecraft().getSession().getUsername();
        String sessionID = Minecraft.getMinecraft().getSession().getSessionID();
        final String urlString = "http://session.minecraft.net/game/joinserver.jsp?user=" + username + "&sessionId=" + sessionID + "&serverId=1";
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection conn;
                try {
                    URL url = new URL(urlString);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                    }
                    rd.close();
                    if (response.toString().equals("OK")) {
                        isPlayerReal = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isPlayerReal() {
        return isPlayerReal;
    }

    private void checkForUpdates() {
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection conn;
                try {
                    URL url = new URL("http://api.connorlinfoot.com/v1/update/" + VERSION + "/?plain=true");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                    }
                    rd.close();
                    if (response.toString().equals("UPDATE")) {
//                        Minecraft.getMinecraft().thePlayer.sendChatMessage(TextFormatting.AQUA + "An update for MC Tools has been found, you can download the last version at https://github.com/ConnorLinfoot/MCTools-Mod/releases");
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("An update for MC Tools has been found, you can download the last version at https://github.com/ConnorLinfoot/MCTools-Mod/releases");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //	@SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
            try {
                Armor.render();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
