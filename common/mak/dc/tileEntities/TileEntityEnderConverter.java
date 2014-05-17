package mak.dc.tileEntities;

import mak.dc.DeadCraft;
import mak.dc.blocks.BlockEnderConverter;
import mak.dc.network.packet.DeadCraftEnderConverterPacket;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnderConverter extends TileEntityDeadCraft implements IPowerSender,IInventory{

	//TODO
	
	public static final int MAXPROCESSTIME = 200; // in ticks
	public static final int MAXPOWER = 5000;
	
	private int power;
	private int amountAsked;
	private ItemStack inv;
	private int processTime;
	
	public TileEntityEnderConverter() {
		super(true);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		this.power++;
		if(power >= MAXPOWER) this.power = 0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.power = nbtTagCompound.getInteger("power");
		this.processTime = nbtTagCompound.getInteger("process");
		NBTTagList items = (NBTTagList) nbtTagCompound.getTag("Items");
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
			int slot = item.getByte("Slot");
			
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("power", power);
		nbtTagCompound.setInteger("process", processTime);
		
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {		
			ItemStack stack = getStackInSlot(i);
			
			if (stack != null) {
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte)i);
				stack.writeToNBT(item);
				items.appendTag(item);
			}
		}
		nbtTagCompound.setTag("Items", items);
		
	}


	@Override
	public void sendPowerTo(IPowerReceiver te) {
		if(worldObj.isRemote) {
			if(power >= amountAsked) {
				te.recievePower(amountAsked);
				this.power -= amountAsked;
			}
		}
		
	}


	@Override
	public void onAskedPower(int amount) {
		if(worldObj.isRemote) {
			if(power - amount >=0)
				this.amountAsked = amount;
			else
				this.amountAsked = power;
		}		
		
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv;
	}

	@Override
	public ItemStack decrStackSize(int i, int count) {
		ItemStack itemstack = getStackInSlot(i);
		
		if (itemstack != null) {
			if (itemstack.stackSize <= count) {
				setInventorySlotContents(i, null);
			}else{
				itemstack = itemstack.splitStack(count);
			}}
		return itemstack;	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return inv;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack itemstack) {
		inv = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
	            itemstack.stackSize = getInventoryStackLimit();
		
		
	}

	@Override
	public String getInventoryName() {
		return blockType.getLocalizedName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		BlockEnderConverter.renderPass = pass;
		return pass == 0 || pass == 1 || pass == 2;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

}
