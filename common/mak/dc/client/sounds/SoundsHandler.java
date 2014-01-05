package mak.dc.client.sounds;

import mak.dc.lib.Lib;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundsHandler {
	
	public SoundsHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@ForgeSubscribe
	public void onSoundsLoad(SoundLoadEvent event) {
		for (Sound sound : Sound.values()) {
			addSound(event, sound);
		}
	}
	
	private void addSound(SoundLoadEvent event, Sound sound) {
		event.manager.soundPoolSounds.addSound(Lib.MOD_ID + "." + sound.getName() + ".ogg");
	}
}
