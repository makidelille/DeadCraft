package mak.dc.common.inventory;

import mak.dc.common.items.ItemCrystal;
import mak.dc.common.items.ItemWithPower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryItemWithPower implements IInventory{

	private ItemStack stack;
	private EntityPlayer player;
	
	private ItemStack inv;
	
	public InventoryItemWithPower(ItemStack itemWithPower, EntityPlayer player){
		this.stack = itemWithPower;
		this.player = player;
		
		
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		readFromNBT(stack.getTagCompound());
	}
	
	private boolean isItemStackValid(){
		return stack.getItem() instanceof ItemWithPower;
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
            } else {
                itemstack = itemstack.splitStack(count);
            }
        }
        markDirty();
        return itemstack;
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return getStackInSlot(var1);
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inv = var2;		
		markDirty();
	}

	@Override
	public String getInventoryName() {
		return stack.getDisplayName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return stack.hasDisplayName();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		if (inv != null && inv.stackSize == 0){
			inv = null;
		}
		// be sure to write to NBT when the inventory changes!
		writeToNBT(stack.getTagCompound());
		player.setCurrentItemOrArmor(0, stack);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return var1 == player;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return var2.getItem() instanceof ItemCrystal;
	}

	private void writeToNBT(NBTTagCompound tag) {		
		ItemWithPower.setCrystal(stack, inv);
	}
	
	private void readFromNBT(NBTTagCompound tag) {
		inv = ItemWithPower.getCrystal(stack);
	}
	
}
