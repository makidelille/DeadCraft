package mak.dc.blocks;

import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.util.NBTTagCompoundDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockDeadCraft extends Block implements ITileEntityProvider { //TODO add ItemBlock with the data of the config

    
    protected BlockDeadCraft (Material par2Material) {
        super(par2Material);
        this.setBlockUnbreakable();
        this.setResistance(-1F);
    }

      
    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase ent,  ItemStack is) {
        super.onBlockPlacedBy(world, x, y, z, ent, is);
        if(!world.isRemote) {
        	if(is.getTagCompound() != null)
	            if(world.getTileEntity(x, y, z)!= null && ent instanceof EntityPlayer) {
	                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
	                te.setOwner(((EntityPlayer)ent).getCommandSenderName());
	                System.out.println("valid");
	            }
	         else if(world.getTileEntity(x, y, z)!= null && ent instanceof EntityPlayer) {
	                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
	                te.readFromNBT(is.getTagCompound());
	                System.out.println("valid");
	            }
	        	 
        }
    }
    
    @Override
    public boolean onBlockActivated(World world,int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ ) {
        if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
            if(!((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserAllowed(player.getCommandSenderName())) return false;
            if(player.isSneaking() && ((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserCreator(player.getCommandSenderName())) {
            	ItemStack is = new ItemStack(this);
            	is.setTagCompound(new NBTTagCompoundDeadCraft(((TileEntityDeadCraft)world.getTileEntity(x, y, z))));
            	this.dropBlockAsItem(world, x, y, z, is);
            	return true;
            }
        }
        return false;
    }
    
    

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDeadCraft();
	}
	
	

	
	
    
 
}
