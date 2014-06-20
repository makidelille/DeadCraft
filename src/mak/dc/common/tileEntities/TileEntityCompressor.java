package mak.dc.common.tileEntities;

import mak.dc.common.items.ItemCompacted;
import mak.dc.common.items.ItemCrystal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileEntityCompressor extends TileEntityDeadCraftWithPower implements IInventory {
    private static final int MAXCHARGESPEED = 50;
    private static final int MAXCHARGE = 5_000;
    private static final int BUILDTIME = 100;
    
    public static final byte slotPower = 0;
    public static final byte slotInput = 1;
    public static final byte slotOutput = 2;
    
    private ItemStack[] inv = new ItemStack[3];
    private ItemStack tempbuffer;
    private int progress;
    private boolean wip;
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        if(getStackInSlot(slotInput) != null && getStackInSlot(slotOutput) == null && !wip){
            wip = true;
            tempbuffer = getStackInSlot(slotInput);
            setInventorySlotContents(slotInput, null);
        }
        if(wip){
            progress++;
        }else{
            progress = 0;
        }
        System.out.println(progress);
        if(progress >= BUILDTIME){
            ItemStack re = ItemCompacted.compactStackInto(tempbuffer);
            setInventorySlotContents(slotOutput, re);
            wip = false;            
        }
        
        
        
    }
    
    
    
    @Override
    protected int getMaxChargeSpeed() {
        return MAXCHARGESPEED;
    }
    
    @Override
    protected int getMaxPower() {
        return MAXCHARGE;
    }

    @Override
    public int getSizeInventory() {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inv[var1];
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
        return inv[var1];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        inv[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return null;
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
    public boolean isItemValidForSlot(int slot, ItemStack var2) {
        switch(slot) {
            case slotInput : return true;
            case slotOutput : return false;
            case slotPower : return var2.getItem() instanceof ItemCrystal;
        }
        return false;
    }
    
}
