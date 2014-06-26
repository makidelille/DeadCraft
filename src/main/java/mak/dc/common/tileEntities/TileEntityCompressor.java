package mak.dc.common.tileEntities;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.common.items.ItemCompacted;
import mak.dc.common.items.ItemCrystal;
import mak.dc.common.util.Config.ConfigLib;
import mak.dc.network.pipeline.packets.DeadCraftCompressorPacket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

public class TileEntityCompressor extends TileEntityDeadCraftWithPower implements IInventory, ISidedInventory {
    private static final int MAXCHARGESPEED = ConfigLib.BASE_MAXCHARGE;
    private static final int MAXCHARGE = ConfigLib.BASE_MAXCHARGE;
    public static final int POWERUSE = ConfigLib.MIN_CONSO;
    public static final float COMPRESSMULT = 5f;
    
    public static final int BUILDTIME = ConfigLib.COMPRESS_TIME;
    
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
        if (!worldObj.isRemote) {
            if (!this.isSync) sync();
            if(!this.hasReceive && this.getCharge() <= (getMaxPower() - MAXCHARGESPEED) &&getStackInSlot(slotPower) != null && getStackInSlot(slotPower).getItem() instanceof ItemCrystal){
                int power = MAXCHARGESPEED - ItemCrystal.dischargeItem(getStackInSlot(slotPower), MAXCHARGESPEED);
                this.setCharge(getCharge() + power);
            }
            boolean flag = false;
            if (isInverted) flag = getStackInSlot(slotInput) != null && getStackInSlot(slotInput).getItem() instanceof ItemCompacted && getStackInSlot(slotInput).stackSize == 1;
            else if (!isInverted) flag = getStackInSlot(slotInput) != null;
            if (flag && getStackInSlot(slotOutput) == null && !wip && getCharge()>POWERUSE) {
                wip = true;
                tempbuffer = getStackInSlot(slotInput);
                setInventorySlotContents(slotInput, null);
                isSync = false;
            }
            if (progress >= BUILDTIME) {
                ItemStack re;
                if (isInverted) {
                    re = ItemCompacted.uncompactStack(tempbuffer);
                } else {
                    re = ItemCompacted.compactStackInto(tempbuffer);
                    tempbuffer = null;
                }
                setInventorySlotContents(slotOutput, re);
                wip = false;
                isSync = false;
            }
            if (wip) {
                if(getCharge() >= POWERUSE * (isInverted ? 1:COMPRESSMULT)){
                    setCharge((int) (getCharge() - POWERUSE * (isInverted ? 1:COMPRESSMULT)));
                    progress++;
                }else{
                    wip = false;
                    if(getStackInSlot(slotOutput) == null){
                        setInventorySlotContents(slotOutput, tempbuffer);
                        tempbuffer = null;
                    }
                }
            } else {
                progress = 0;
            }
        }
        if (worldObj.isRemote) {
            if (wip) {
                progress++;
            } else {
                progress = 0;
            }
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
            } else if (slot == -1) tempbuffer = ItemStack.loadItemStackFromNBT(item);
            
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
        if (temp != null) {
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
    public List<String> getInfo() {
        ArrayList<String> l = new ArrayList<String>();
        l.add(StatCollector.translateToLocal("dc.block.compressor.info.progress") + " : " + getProgress() +"/" + BUILDTIME);
        l.add(StatCollector.translateToLocal("dc.block.compressor.info.mode") + " : " + (isInverted ? StatCollector.translateToLocal("dc.block.compressor.info.decomp") : StatCollector.translateToLocal("dc.block.compressor.info.comp")));
        l.addAll(super.getInfo());
        return l;
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
    public void openInventory() {
    }
    
    @Override
    public void closeInventory() {
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack var2) {
        switch (slot) {
            case slotInput:
                return true;
            case slotOutput:
                return false;
            case slotPower:
                return var2.getItem() instanceof ItemCrystal;
        }
        return false;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public boolean isInverted() {
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
        if (!worldObj.isRemote) isSync = false;
        
    }
    
    public void setInverted(boolean isInverted2) {
        if (!wip) this.isInverted = isInverted2;
        if (!worldObj.isRemote) isSync = false;
    }

    public ItemStack getTempbuffer() {
        return tempbuffer;
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(var1 == 0) return new int[] {slotOutput};
		if (var1 == 1) return new int[] {slotInput};
		return null;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		if (var3 == 0) return false;
		if (var3 == 1 && var1 ==slotInput) return isItemValidForSlot(slotInput, var2);
		return false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		if(var3 == 1) return false;
		if(var3 == 0) return var1 == slotOutput;
		return false;
	}
}
