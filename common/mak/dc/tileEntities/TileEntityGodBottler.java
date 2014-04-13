package mak.dc.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.obj.Face;

public class TileEntityGodBottler extends TileEntityDeadCraft implements IInventory{
	
	private static final byte deadcraftId = 2;

	private TileEntityGodBottler pair;
	
	private int time;
	private int starsInStock;
	private byte facing;
	private boolean hasStated;
	private boolean isTop;
	private ItemStack[] inventory ;
	
	
	
	public TileEntityGodBottler(boolean top, byte facing) {
		super(true);
		this.isTop = top;
		this.facing = facing;
	}
	
	public TileEntityGodBottler(boolean top) {
		this(top, (byte) 0);
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
		return isTop ? pair.getInventoryName() : "GodBottler";
	}



	@Override
	public int getInventoryStackLimit() {
		return isTop ? pair.getInventoryStackLimit() : 1;
	}



	@Override
	public int getSizeInventory() {
		return isTop ? pair.getSizeInventory() : inventory.length;
	}



	@Override
	public ItemStack getStackInSlot(int var1) {
		return isTop ? pair.getStackInSlot(var1) : inventory[var1];
	}



	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return isTop? pair.getStackInSlotOnClosing(var1) :this.inventory[var1];
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if (isTop) pair.setInventorySlotContents(var1, var2);
	}



	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}



	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		if (isTop) return pair.isItemValidForSlot(var1, var2);
		return true;
	}



	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		if (isTop) return pair.isUseableByPlayer(var1);
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.isTop = nbtTagCompound.getBoolean("top");
		this.facing = nbtTagCompound.getByte("face");
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setByte("face", this.facing);
		nbtTagCompound.setBoolean("top", this.isTop);
	}



	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}

	public byte getFacing() {
		return facing;
	}

	public void setFacing(byte facing) {
		this.facing = facing;
	}


	
	
}
