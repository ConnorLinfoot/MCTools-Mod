package com.connorlinfoot.mctools.Listeners;

import com.connorlinfoot.mctools.MCTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRender {
    public static Map<UUID, String> aboveHeadCache = new HashMap<>();
    public static HashMap<UUID, EnumParticleTypes> particles = new HashMap<>();

    @SubscribeEvent
    public void render(RenderPlayerEvent.Pre event) {
        EntityPlayer entityPlayer = event.entityPlayer;

        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) && particles.containsKey(entityPlayer.getUniqueID()) && !entityPlayer.isInvisible() && !entityPlayer.isSneaking()) {
            Minecraft.getMinecraft().theWorld.spawnParticle(particles.get(entityPlayer.getUniqueID()), entityPlayer.posX, entityPlayer.posY + 2, entityPlayer.posZ, 0.0D, 0.0D, 0.0D);
            Minecraft.getMinecraft().theWorld.spawnParticle(particles.get(entityPlayer.getUniqueID()), entityPlayer.posX, entityPlayer.posY + 2, entityPlayer.posZ, 0.0D, 0.0D, 0.0D);
            Minecraft.getMinecraft().theWorld.spawnParticle(particles.get(entityPlayer.getUniqueID()), entityPlayer.posX, entityPlayer.posY + 2, entityPlayer.posZ, 0.0D, 0.0D, 0.0D);
            Minecraft.getMinecraft().theWorld.spawnParticle(particles.get(entityPlayer.getUniqueID()), entityPlayer.posX, entityPlayer.posY + 2, entityPlayer.posZ, 0.0D, 0.0D, 0.0D);
        }

        String s;
        if (entityPlayer.getUniqueID().equals(MCTools.playerUUID)) return;
        if (!entityPlayer.isSneaking() && !entityPlayer.isInvisible() && (s = aboveHeadCache.get(entityPlayer.getUniqueID())) != null) {
            double offset = 0.3;
            Scoreboard scoreboard = entityPlayer.getWorldScoreboard();
            ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(2);

            if (scoreObjective != null && scoreObjective.getName() != null && !scoreObjective.getName().equals("")) {
                offset *= 2;
            }

            renderName(event.renderer, s, entityPlayer, event.x, event.y + offset, event.z);
        }
    }

    public void renderName(RenderPlayer renderer, String str, EntityPlayer entityIn, double x, double y, double z) {
        FontRenderer fontrenderer = renderer.getFontRendererFromRenderManager();
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderer.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderer.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

}
