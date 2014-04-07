package mak.dc.blocks;

import mak.dc.entity.EntityItemDeadCraft;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        	if(is.getTagCompound() != null && world.getTileEntity(x, y, z)!= null && ent instanceof EntityPlayer) {
	                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
	                te.setFromNBT(is.getTagCompound());
	                System.out.println("valid");
        	}else if(world.getTileEntity(x, y, z)!= null && ent instanceof EntityPlayer) {
                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
                te.setOwner(((EntityPlayer)ent).getCommandSenderName());
                System.out.println("valid");
            }
        }
       
	        	 
     }
    
    @Override
    	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
	    	if(!world.isRemote) {
		    	if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
		            if(!((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserAllowed(player.getCommandSenderName())) return ;
		            if(player.isSneaking() && ((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserCreator(player.getCommandSenderName())) {
		            	TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
		            	ItemStack is = new ItemStack(this);
		            	is.setTagCompound(te.writeNBTData(new NBTTagCompound()));
		            	System.out.println(is.getTagCompound());
		            	EntityItemDeadCraft entIs = new EntityItemDeadCraft(world, x, y, z, is);       	
		            	world.spawnEntityInWorld(entIs);
//		            	this.dropBlockAsItem(world, x, y, z, is);
		            	world.setBlock(x, y, z, Blocks.air);
		            	world.removeTileEntity(x, y, z);
		            	return;
		            	}
		    }}
	        return ;
    	}
  
    
    @Override
    public boolean onBlockActivated(World world,int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ ) {
        if(!world.isRemote)
	    	if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
	            if(!((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserAllowed(player.getCommandSenderName())) return false;
	        }
        return false;
    }
    
   @Override
	protected void dropBlockAsItem(World world, int x,	int y, int z, ItemStack is) {
   }
    
    

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDeadCraft();
	}


	public boolean canBePlaced(int x, int y, int z) {
		return true;
	}


	public void place(World world, int x, int y, int z, ItemStack is) {
		if(!world.isRemote) {
			world.setBlock(x, y, z, this);
			if(world.getTileEntity(x, y, z)!=null && is.getTagCompound() != null)
				world.getTileEntity(x, y, z).readFromNBT(is.getTagCompound());
		}
		
	}
	
	

	
	
    
 
}
