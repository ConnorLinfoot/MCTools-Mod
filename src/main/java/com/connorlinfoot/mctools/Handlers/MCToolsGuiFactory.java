package com.connorlinfoot.mctools.Handlers;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.List;
import java.util.Set;

public class MCToolsGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return MCToolsConfig.class;
	}

	private static final Set<RuntimeOptionCategoryElement> fmlCategories = ImmutableSet.of(new RuntimeOptionCategoryElement("HELP", "FML"));

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return fmlCategories;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return new RuntimeOptionGuiHandler() {
			@Override
			public void paint(int x, int y, int w, int h) {
			}

			@Override
			public void close() {
			}

			@Override
			public void addWidgets(List<Gui> widgets, int x, int y, int w, int h) {
				widgets.add(new GuiButton(100, x+10, y+10, "HELLO"));
			}

			@Override
			public void actionCallback(int actionId) {
			}
		};
	}

}