package mak.dc.proxy;

import mak.dc.client.gui.GuiHandler;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy {

	public void registerRender() {
				
	}

	public void registerSounds() {
		
	}

	public World getClientWorld() {
		return null;
	}

	public void init() {
		registerRender();
		registerTileEntityRender();
		registerSounds();
		new GuiHandler();
		
	}

	public void registerTileEntityRender() {		
	}

}
