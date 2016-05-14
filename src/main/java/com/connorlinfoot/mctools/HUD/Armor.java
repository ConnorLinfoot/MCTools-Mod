package com.connorlinfoot.mctools.HUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Armor {
	private static List<ItemStack> items = new ArrayList<>();

	public static void render() {
//		if (PixelPlusMod.getInstance().CONFIG.armorhud_enabled) {
		if (true) {
			getInventory();
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			RenderHelper.enableGUIStandardItemLighting();
			itemRenderer.zLevel = 200.0F;
//			int h = PixelPlusMod.getInstance().CONFIG.armorhud_y;
			int h = 100;
			for(int i = 0; i < items.size(); i++) {
//				itemRenderer.renderItemAndEffectIntoGUI(items.get(i), PixelPlusMod.getInstance().CONFIG.armorhud_x, h);
				if( items.get(i).isItemDamaged() ) {
					Minecraft.getMinecraft().fontRendererObj.drawString(items.get(i).getItemDamage() + "/" + items.get(i).getMaxDamage(), 100-20, h, 0xFFFFFF);
				}
				itemRenderer.renderItemAndEffectIntoGUI(items.get(i), 100, h);
				h += 16;
			}
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public static void getInventory() {
		items.clear();
		if(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null) {
			items.add(0, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
		}
		if(Minecraft.getMinecraft().thePlayer.inventory.offHandInventory[0] != null) {
			items.add(Minecraft.getMinecraft().thePlayer.inventory.offHandInventory[0]);
		}
		if(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(3) != null) {
			items.add(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(3));
		}
		if(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2) != null) {
			items.add(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2));
		}
		if(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(1) != null) {
			items.add(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(1));
		}
		if(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(0) != null) {
			items.add(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(0));
		}
	}

}