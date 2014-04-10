package mak.dc.proxy;

import mak.dc.blocks.DeadCraftBlocks;
import mak.dc.client.render.RenderGodBottlerTESR;
import mak.dc.client.render.RenderGodBottlerTESR.TESRIndex;
import mak.dc.client.render.RendererMindController;
import mak.dc.client.render.SpecialRenderTileEntityGodBottler;
import mak.dc.items.DeadCraftItems;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	
	public static int renderGodBottlerTESRId;
	
	@Override
	public void registerSounds() {
		
	}
	
	@Override
	public void registerRender() {
		MinecraftForgeClient.registerItemRenderer(DeadCraftItems.mindController, new RendererMindController());
		renderGodBottlerTESRId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new RenderGodBottlerTESR());
	}
	
	@Override
	public void registerTileEntityRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGodBottler.class, new SpecialRenderTileEntityGodBottler());
		RenderGodBottlerTESR.blockByTESR.put(new TESRIndex(DeadCraftBlocks.godBottler,0), new SpecialRenderTileEntityGodBottler());
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
