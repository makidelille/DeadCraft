package mak.dc.proxy;

import mak.dc.blocks.DeadCraftBlocks;
import mak.dc.client.render.TESRInventoryRenderer;
import mak.dc.client.render.TESRInventoryRenderer.TESRIndex;
import mak.dc.client.render.block.SpecialRenderTileEntityGodBottler;
import mak.dc.client.render.item.RendererItemMindController;
import mak.dc.items.DeadCraftItems;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	
	public static int renderInventoryTESRId;
	
	@Override
	public void registerSounds() {
		
	}
	
	@Override
	public void registerRender() {
		MinecraftForgeClient.registerItemRenderer(DeadCraftItems.mindController, new RendererItemMindController());
		renderInventoryTESRId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new TESRInventoryRenderer());
	}
	
	@Override
	public void registerTileEntityRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGodBottler.class, new SpecialRenderTileEntityGodBottler());
		TESRInventoryRenderer.blockByTESR.put(new TESRIndex(DeadCraftBlocks.godBottler, 0), new SpecialRenderTileEntityGodBottler());
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
