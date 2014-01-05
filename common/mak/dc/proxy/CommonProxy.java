package mak.dc.proxy;

import mak.dc.client.sounds.SoundsHandler;
import cpw.mods.fml.common.FMLLog;

public class CommonProxy {

	public void registerRender() {
				
	}

	public void registerSounds() {
		new SoundsHandler();
		FMLLog.info("Dead Craft sounds Initialized");
	}

}
