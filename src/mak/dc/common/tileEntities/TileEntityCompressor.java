package mak.dc.common.tileEntities;

import mak.dc.DeadCraft;
import mak.dc.common.items.ItemCompacted;
import mak.dc.common.items.ItemCrystal;
import mak.dc.network.pipeline.packets.DeadCraftCompressorPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityCompressor extends TileEntityDeadCraftWithPower implements IInventory {
    private static final int MAXCHARGESPEED = 50;
    private static final int MAXCHARGE = 5_000;
    public static final int BUILDTIME = 500;
    
    public static final byte slotPower = 0;
    public static final byte slotInput = 1;
    public static final byte slotOutput = 2;
    
    private ItemStack[] inv = new ItemStack[3];
    private ItemStack tempbuffer;
    private int progress;
    private boolean wip;
    private boolean isInverted = false;
    private boolean isSync;
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!worldObj.isRemote){
            if(!this.isSync) sync();
            boolean flag = false;
            if(isInverted) flag = getStackInSlot(slotInput) != null && getStackInSlot(slotInput).getItem() instanceof ItemCompacted;
            else flag = getStackInSlot(slotInput) != null;
            if(flag && getStackInSlot(slotOutput) == null && !wip){
                wip = true;
                tempbuffer = getStackInSlot(slotInput);
                setInventorySlotContents(slotInput, null);
                isSync = false;
            }
            if(progress >= BUILDTIME){
                ItemStack re;
                if(isInverted){
                    re = ItemCompacted.uncompactStack(tempbuffer);
                }else{
                    re = ItemCompacted.compactStackInto(tempbuffer);
                }
                setInventorySlotContents(slotOutput, re);
                tempbuffer = null;
                wip = false;   
                isSync = false;
            }
        }
        if(wip){
            progress++;
        }else{
            progress = 0;
        }
  
        
    }
    
    @Override
    public void syncWithplayer(EntityPlayer player) {
        super.syncWithplayer(player);
        DeadCraft.packetPipeline.sendTo(new DeadCraftCompressorPacket(this), (EntityPlayerMP) player);
    }
    
    
    private void sync() {
        DeadCraft.packetPipeline.sendToDimension(new DeadCraftCompressorPacket(this), this.worldObj.getWorldInfo().getVanillaDimension());
        this.isSync = true;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList items = (NBTTagList) compound.getTag("Items");
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getByte("Slot");
            
            if (slot >= 0 && slot < getSizeInventory()) {
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
            }else if(slot == -1) tempbuffer = ItemStack.loadItemStackFromNBT(item);
            
        }
        
        this.isInverted = compound.getBoolean("isinverted");
        this.wip = compound.getBoolean("wip");
        this.progress = compound.getInteger("progress");
        
    }
    
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        NBTTagList items = new NBTTagList();
        
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            
            if (stack != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte) i);
                stack.writeToNBT(item);
                items.appendTag(item);
            }
        }
        ItemStack temp = tempbuffer;
        if(temp != null){
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("Slot", (byte) -1);
            temp.writeToNBT(item);
            items.appendTag(item);
        }
        
        compound.setTag("Items", items);
        compound.setBoolean("isinverted", isInverted);
        compound.setBoolean("wip", wip);
        compound.setInteger("progress", progress);
        
    }
    
    
    @Override
    public int getMaxChargeSpeed() {
        return MAXCHARGESPEED;
    }
    
    @Override
    public int getMaxPower() {
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



    public int getProgress() {
        return progress;
    }
    
    public boolean isInverted(){
        return isInverted;
    }



    public void setProgress(int data) {
        this.progress = data;
    }

    public boolean hasStarted() {
        return this.wip;
    }

    public void setWorkInProgress(boolean wip2) {
        this.wip = wip2;
        if(!worldObj.isRemote) isSync = false;
        
    }

    public void setInverted(boolean isInverted2) {
        if(!wip) this.isInverted = isInverted2;
        if(!worldObj.isRemote) isSync = false;
    }    
}
