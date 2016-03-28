package com.connorlinfoot.mctools.Hypixel;

import com.connorlinfoot.mctools.MCTools;
import com.google.gson.JsonElement;
import net.hypixel.api.reply.PlayerReply;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsGui extends GuiScreen {
	private UUID playerUuid;
	private GuiButton closeButton;
	private Hypixel hypixel;

	public StatsGui(UUID playerUuid) {
		super();
		this.playerUuid = playerUuid;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (hypixel == null)
			hypixel = MCTools.getMcTools().getHypixel();
		this.drawDefaultBackground();
		if (hypixel.getGameMode() == GameMode.UNKNOWN) {
			drawCenteredString(this.fontRendererObj, "Loading Hypixel Game Mode...", this.width / 2, this.height / 2, 0xFFFFFF);
		} else {
			drawCenteredString(this.fontRendererObj, "Hypixel Player Stats (" + hypixel.getGameMode().getNiceName() + ")", this.width / 2, 20, 0xFFFFFF);

			if (!hypixel.getPlayerDataCache().containsKey(playerUuid)) {
				hypixel.updatePlayerData(playerUuid);
				drawCenteredString(this.fontRendererObj, "Loading Player Stats...", this.width / 2, this.height / 2, 0xFFFFFF);
			} else {

				PlayerReply playerData = hypixel.getPlayerDataCache().get(playerUuid);
//				System.out.println(playerData.getPlayer().getAsJsonObject("stats").getAsJsonObject(hypixel.getGameMode().getAPIName()));

				HashMap<String, String> dataToDisplay = new HashMap<>();
				for (Map.Entry<String, JsonElement> data : playerData.getPlayer().getAsJsonObject("stats").getAsJsonObject(hypixel.getGameMode().getAPIName()).entrySet()) {
					String name = data.getKey();
					try {
						if (data.getValue().getAsString() != null) {
							String val = data.getValue().getAsString();
							dataToDisplay.put(name, val);
						}
					} catch (Exception ignored) {
					}
				}

//				double kills = playerData.getPlayer().getAsJsonObject("stats").getAsJsonObject(hypixel.getGameMode().getAPIName()).get("kills").getAsInt();
//				String killsString = String.format("%,d", (int) kills);
//				double deaths = playerData.getPlayer().getAsJsonObject("stats").getAsJsonObject(hypixel.getGameMode().getAPIName()).get("deaths").getAsInt();
//				String deathsString = String.format("%,d", (int) deaths);

//				drawCenteredString(this.fontRendererObj, "Kills: " + killsString, this.width / 2, this.height / 2 - 10, 0xFFFFFF);
//				drawCenteredString(this.fontRendererObj, "Deaths: " + deathsString, this.width / 2, this.height / 2, 0xFFFFFF);

				int height = 50;
				boolean displayAll = false;

				for (Map.Entry<String, String> data : dataToDisplay.entrySet()) {
					if (displayAll) {
						height = height + 12;
						drawCenteredString(this.fontRendererObj, WordUtils.capitalize(data.getKey().replaceAll("_", " ")) + ": " + data.getValue(), this.width / 2, height, 0xFFFFFF);
						continue;
					}
					switch (data.getKey()) {
						default:
							continue;
						case "coins":
						case "kills":
						case "deaths":
						case "wins":
						case "losses":
						case "games":
							String key = WordUtils.capitalize(data.getKey());
							String value = data.getValue();

							try {
								Integer valueInt = Integer.parseInt(value);
								value = String.format("%,d", valueInt);
							} catch (Exception ignored) {
							}

							drawCenteredString(this.fontRendererObj, key + ": " + value, this.width / 2, height, 0xFFFFFF);
							break;
					}
					height = height + 12;
				}

			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void initGui() {
		this.buttonList.add(this.closeButton = new GuiButton(1, this.width / 2 - 100, this.height - 24, "Close Stats"));
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
