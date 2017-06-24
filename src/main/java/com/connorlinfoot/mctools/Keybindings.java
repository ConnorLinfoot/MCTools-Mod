package com.connorlinfoot.mctools;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Keybindings {
    private MCTools mcTools;

    public Keybindings(MCTools mcTools) {
        this.mcTools = mcTools;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
    }

}
