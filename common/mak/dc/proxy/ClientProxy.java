package mak.dc.proxy;

import mak.dc.client.render.RendererMindController;
import mak.dc.items.DeadCraftItems;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy{

	
	@Override
	public void registerSounds() {
		
	}
	
	@Override
	public void registerRender() {
		MinecraftForgeClient.registerItemRenderer(DeadCraftItems.mindController, new RendererMindController());
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
