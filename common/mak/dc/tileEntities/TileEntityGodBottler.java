package mak.dc.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileEntityGodBottler extends TileEntityDeadCraft implements IInventory{
	
	private static final byte deadcraftId = 2;

	private int time;
	private int starsInStock;
	private boolean hasStated;
	private boolean isTop;
	private ItemStack[] inventory ;
	
	
	
	public TileEntityGodBottler(boolean top) {
		super(true);
		this.isTop = top;
	}
	
	public boolean isTop() {
		return this.isTop;
	}
	
	public void setTop() {
		this.isTop = true;
	}	



	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return null;
	}



	@Override
	public String getInventoryName() {
		return isTop ? null : "GodBottler";
	}



	@Override
	public int getInventoryStackLimit() {
		return isTop ? 0 : 1;
	}



	@Override
	public int getSizeInventory() {
		return isTop ? 0 : inventory.length;
	}



	@Override
	public ItemStack getStackInSlot(int var1) {
		return isTop ? null : inventory[var1];
	}



	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return isTop? null :this.inventory[var1];
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if (isTop) return;
	}



	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}



	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		if (isTop) return false;
		return true;
	}



	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		if (isTop) return false;
		return true;
	}



	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}


	
	
}
