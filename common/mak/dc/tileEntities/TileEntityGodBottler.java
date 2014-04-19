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
	public int facing;
	private boolean hasStated;
	private boolean isTop;
	private ItemStack[] inventory ;

	private int tick;
	
	
	
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
	
	public void setPair(TileEntityGodBottler te) {
		this.pair = te;
	}
	
	@Override
	public void updateEntity() {
		this.setTick(this.getTick() + 1);
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
		this.facing = nbtTagCompound.getInteger("face");
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("face", this.facing);
		nbtTagCompound.setBoolean("top", this.isTop);
	}



	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int i) {
		this.facing = i;
	}

	public int getTick() {
		return tick / 10000;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public void setup(TileEntityGodBottler te) {
		this.facing = te.getFacing();
		this.pair = te;
		this.allowed = te.allowed;
		this.owner = te.owner;
		this.isManagable = te.isManagable;
		this.locked = te.locked;
		this.setTop();
	}



	
	
}
