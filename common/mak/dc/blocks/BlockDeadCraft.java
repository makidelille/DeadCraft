package mak.dc.blocks;

import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDeadCraft extends BlockContainer{

    
    protected BlockDeadCraft (int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Override
    public TileEntity createNewTileEntity (World world) {
        return new TileEntityDeadCraft();
    }
      
    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase ent,  ItemStack is) {
        super.onBlockPlacedBy(world, x, y, z, ent, is);
        if(!world.isRemote) {
            if(world.blockHasTileEntity(x, y, z) && ent instanceof EntityPlayer) {
                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getBlockTileEntity(x, y, z);
                te.setOwner(((EntityPlayer)ent).username);
                System.out.println("valid");
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(World world,int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ ) {
        if(world.getBlockTileEntity(x, y, z) != null && world.getBlockTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
            if(!((TileEntityDeadCraft)world.getBlockTileEntity(x, y, z)).isUserAllowed(player.username)) return false;
        }
        return false;
    }
    
 
}
