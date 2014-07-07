package mak.dc.common.inventory;

import mak.dc.common.items.ItemWithPower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryItemWithPower implements IInventory{

	private ItemStack stack;
	private EntityPlayer player;
	
	public InventoryItemWithPower(ItemStack itemWithPower, EntityPlayer player){
		this.stack = itemWithPower;
		this.player = player;
	}
	
	private boolean isItemStackValid(){
		return stack.getItem() instanceof ItemWithPower;
	}
	
	@Override
	public int getSizeInventory() {
		return isItemStackValid()? ItemWithPower.getInvSize(stack) : 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return isItemStackValid() ? ItemWithPower.getCrystal(stack) : null;
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
        return itemstack;
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return getStackInSlot(var1);
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if(!isItemStackValid()) return;
		ItemWithPower.setCrystal(stack, var2);
			
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
	public void markDirty() {}

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
		return var2.getItem() instanceof ItemWithPower;
	}

}
