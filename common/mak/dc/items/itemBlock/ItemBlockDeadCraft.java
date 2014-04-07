package mak.dc.items.itemBlock;

import mak.dc.blocks.BlockDeadCraft;
import mak.dc.blocks.DeadCraftBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBlockDeadCraft extends ItemBlock{

	private final BlockDeadCraft blockDC;

	public ItemBlockDeadCraft() { //XXX
		this((BlockDeadCraft) DeadCraftBlocks.eggSpawner);
	}
	

	public ItemBlockDeadCraft(BlockDeadCraft block) {
		super(block);
		this.blockDC = block;
		this.setUnlocalizedName(block.getUnlocalizedName());
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			if(this.blockDC.canBePlaced(x,y,z)) {
				NBTTagCompound tag = is.getTagCompound();
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
				this.blockDC.place(world,x,y,z,is);
				return true;
			}
		}
		
		
		return false;
	}
	
	
	

}
