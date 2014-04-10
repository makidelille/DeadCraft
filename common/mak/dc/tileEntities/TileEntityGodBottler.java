package mak.dc.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileEntityGodBottler extends TileEntityDeadCraft implements IInventory{
	
	private static final byte deadcraftId = 2;

	private int time;
	private int starsInStock;
	private boolean hasStated;
	private ItemStack[] inventory ;
	
	
	
	public TileEntityGodBottler() {
		super(true);
	}



	



	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return null;
	}



	@Override
	public String getInventoryName() {
		return null;
	}



	@Override
	public int getInventoryStackLimit() {
		return 0;
	}



	@Override
	public int getSizeInventory() {
		return 0;
	}



	@Override
	public ItemStack getStackInSlot(int var1) {
		return null;
	}



	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return this.inventory[var1];
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		
	}



	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}


	
	
}
