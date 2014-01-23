package mak.dc.proxy;

import mak.dc.client.render.RendererMindController;
import mak.dc.lib.ItemInfo;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy{

	
	@Override
	public void registerSounds() {
		
	}
	
	@Override
	public void registerRender() {
		MinecraftForgeClient.registerItemRenderer(ItemInfo.MINDCONTROLLER_ID + 256, new RendererMindController());
	}
}
