package mak.dc.common.proxy;

import mak.dc.client.render.TESRInventoryRenderer;
import mak.dc.client.render.TESRInventoryRenderer.TESRIndex;
import mak.dc.client.render.block.SpecialRenderTileEntityCompressor;
import mak.dc.client.render.block.SpecialRenderTileEntityEnderConverter;
import mak.dc.client.render.block.SpecialRenderTileEntityGodBottler;
import mak.dc.client.render.item.RendererItemGodCan;
import mak.dc.client.render.item.RendererItemMindController;
import mak.dc.common.blocks.DeadCraftBlocks;
import mak.dc.common.items.DeadCraftItems;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.tileEntities.TileEntityEnderConverter;
import mak.dc.common.tileEntities.TileEntityGodBottler;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    
    public static int renderInventoryTESRId;
    
    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }
    
    @Override
    public void registerRender() {
        MinecraftForgeClient.registerItemRenderer(DeadCraftItems.mindController, new RendererItemMindController());
        MinecraftForgeClient.registerItemRenderer(DeadCraftItems.godCan, new RendererItemGodCan());
        renderInventoryTESRId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new TESRInventoryRenderer());
    }
    
    @Override
    public void registerSounds() {
        
    }
    
    @Override
    public void registerTileEntityRender() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGodBottler.class, new SpecialRenderTileEntityGodBottler());
        TESRInventoryRenderer.blockByTESR.put(new TESRIndex(DeadCraftBlocks.godBottler, 0), new SpecialRenderTileEntityGodBottler());
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnderConverter.class, new SpecialRenderTileEntityEnderConverter());
        TESRInventoryRenderer.blockByTESR.put(new TESRIndex(DeadCraftBlocks.enderConverter, 0), new SpecialRenderTileEntityEnderConverter());
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompressor.class, new SpecialRenderTileEntityCompressor());
        TESRInventoryRenderer.blockByTESR.put(new TESRIndex(DeadCraftBlocks.compressor, 0), new SpecialRenderTileEntityCompressor());
    }
    
}
