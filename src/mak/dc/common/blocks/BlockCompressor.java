package mak.dc.common.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import mak.dc.DeadCraft;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.network.proxy.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCompressor extends BlockDeadCraft {
    
    protected BlockCompressor() {
        super(Material.iron);
    }
   
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && player != null) {
            FMLNetworkHandler.openGui(player, DeadCraft.instance, 4, world, x, y, z);
        }
        return true;
    }
    
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public int getRenderType() {
       return ClientProxy.renderInventoryTESRId;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityCompressor();
    }
    
}
