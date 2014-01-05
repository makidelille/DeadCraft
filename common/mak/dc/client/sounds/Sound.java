package mak.dc.client.sounds;

import mak.dc.lib.Lib;
import net.minecraft.client.Minecraft;

public enum Sound {
		EGG_SPAWN("eggspawn");
		
		
	private String name;

	Sound(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void play(double x, double y, double z, float volume, float pitch) {
		Minecraft.getMinecraft().sndManager.playSound(Lib.MOD_ID + "." + name, (float)x, (float)y, (float)z, volume, pitch);
	}
}
