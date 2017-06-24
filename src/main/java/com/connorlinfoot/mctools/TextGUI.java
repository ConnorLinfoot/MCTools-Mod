package com.connorlinfoot.mctools;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * This GUI is for testing where text can go, if you use it it'll show text behind the mouse and also shows the x (width) and y (height) on the screen
 */

public class TextGUI extends GuiScreen {
    private GuiButton closeButton;

    public TextGUI() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
//        drawCenteredString(this.fontRendererObj, TextFormatting.AQUA + "This is some demo text!", mouseX, mouseY, 0xFFFFFF);
//        drawCenteredString(this.fontRendererObj, TextFormatting.RED + "X (Width): " + mouseX + " - Y (Height): " + mouseY, this.width / 2, this.height - 40, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "This is some demo text!", mouseX, mouseY, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "X (Width): " + mouseX + " - Y (Height): " + mouseY, this.width / 2, this.height - 40, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void initGui() {
        this.buttonList.add(this.closeButton = new GuiButton(1, this.width / 2 - 100, this.height - 24, "Close"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.closeButton) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null)
                this.mc.setIngameFocus();
        }
    }

}
