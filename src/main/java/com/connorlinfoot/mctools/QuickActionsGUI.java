package com.connorlinfoot.mctools;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class QuickActionsGUI extends GuiScreen {
    private GuiButton closeButton;

    public QuickActionsGUI() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
//        drawCenteredString(this.fontRendererObj, TextFormatting.YELLOW + "// TODO", this.width / 2, 40, 0xFFFFFF);
//        drawCenteredString(this.fontRendererObj, TextFormatting.AQUA + "MC Tools", mouseX, mouseY - 20, 0xFFFFFF);
//        drawCenteredString(this.fontRendererObj, TextFormatting.RED + "X: " + mouseX + " - Y: " + mouseY, this.width / 2, this.height / 2, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "// TODO", this.width / 2, 40, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "MC Tools", mouseX, mouseY - 20, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "X: " + mouseX + " - Y: " + mouseY, this.width / 2, this.height / 2, 0xFFFFFF);
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
