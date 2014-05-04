package mak.dc.blocks;

import mak.dc.items.ItemWrench;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockDeadCraft extends Block implements ITileEntityProvider {

    
	private static int DeadCraftId;
	
	public static void init() {
		DeadCraftId = 0;
	}
	
	public static void setid(int id) {
		DeadCraftId = id;
	}
	
	public static int getId() {
		return DeadCraftId;
	}
	
	public static int getNextId() {
		return DeadCraftId++;
	}
	
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
                te.readNBTData(is.getTagCompound());
        	}else if(world.getTileEntity(x, y, z)!= null && ent instanceof EntityPlayer) {
                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
                te.setOwner(((EntityPlayer)ent).getCommandSenderName());
            }
        }
       
	        	 
     }
    
    @Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
    	if(!world.isRemote) {
	    	if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
	            if(!((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserAllowed(player.getCommandSenderName())) return ;
	            if(player.isSneaking() && ((TileEntityDeadCraft)world.getTileEntity(x, y, z)).isUserCreator(player.getCommandSenderName()) && player.getHeldItem().getItem() instanceof ItemWrench) {
	            	this.breakBlock(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
	            	return;
	            	}
	    }}
        return ;
	}
  
    
    @Override
    public void breakBlock(World world, int x, int y,int z, Block block, int meta) {
    	ItemStack is = new ItemStack(this);
    	TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
    	is.setTagCompound(te.writeNBTData(new NBTTagCompound()));
    	this.dropBlockAsItem(world, x, y, z, is);       	
    	world.removeTileEntity(x, y, z);
    	world.setBlock(x, y, z, Blocks.air);
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
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDeadCraft(true);
	}


	public boolean canBePlaced(World world,int x, int y, int z) {
		return world.isAirBlock(x, y, z);
	}

	

	
	
    
 
}
