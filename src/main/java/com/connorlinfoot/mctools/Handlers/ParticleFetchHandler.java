package com.connorlinfoot.mctools.Handlers;

import com.connorlinfoot.mctools.Listeners.PlayerRender;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ParticleFetchHandler {
	private ArrayList<UUID> toRun = new ArrayList<>();
	private ArrayList<UUID> ran = new ArrayList<>();
	private boolean running = false;
	private HashMap<UUID, EnumParticleTypes> particles = new HashMap<>();

	public void addUUID(UUID uuid) {
		if (!toRun.contains(uuid) && !ran.contains(uuid))
			toRun.add(uuid);
	}

	public void clear() {
		ran.clear();
	}

	public void run() {
		if (running)
			return;
		if (!particles.isEmpty()) {
			PlayerRender.particles = (HashMap<UUID, EnumParticleTypes>) particles.clone();
			particles.clear();
		}
		particles = (HashMap<UUID, EnumParticleTypes>) PlayerRender.particles.clone();
		ArrayList<UUID> toRun = (ArrayList<UUID>) this.toRun.clone();
		for (final UUID uuid : toRun) {
			ran.add(uuid);
			this.toRun.remove(uuid);
			new Thread(new Runnable() {
				public void run() {
					running = true;
					HttpURLConnection conn;
					try {
						URL url = new URL("https://api.mctools.io/v1/particle/" + uuid.toString() + "/?plain=true");
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
						if (!response.toString().contains("NULL")) {
							try {
								particles.put(uuid, EnumParticleTypes.valueOf(String.valueOf(response)));
							} catch (Exception ignored) {
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					running = false;
				}
			}).start();
		}

	}

}
