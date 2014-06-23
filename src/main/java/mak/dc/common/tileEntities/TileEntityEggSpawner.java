package mak.dc.common.tileEntities;

import java.util.ArrayList;
import java.util.List;

import mak.dc.common.items.DeadCraftItems;
import mak.dc.common.items.ItemCrystal;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.FMLLog;

public class TileEntityEggSpawner extends TileEntityDeadCraftWithPower implements IInventory, ISidedInventory {
    
    private static final byte deadcraftId = 1;
    
    public static final int MAXPOWER = 100000;
    
    public static final int CHARGESPEED = 50;
    public static final int POWERUSAGE = 5;
    
    private static final int[] slots = { 0, 1, 2, 3, 4, 5, 6, 7 };
    
    public static int _maxBuildTime = 18000;
    
    private ItemStack[] invContent;
    
    private int buildTime;
    
    private byte started;
    private boolean created;
    private int eggInStock;
    
    private byte wait;
    private byte redState = 0;
    private byte mode = 1;
    
    public TileEntityEggSpawner() {
        super();
        invContent = new ItemStack[8];
        setStarted((byte) 0);
        created = false;
        buildTime = 0;
        eggInStock = 0;
    }
    
    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3) {
        return false;
    }
    
    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3) {
        return isItemValidForSlot(var1, var2);
    }
    
    private void charge() {
        if (power + CHARGESPEED > MAXPOWER) return;
        else if (invContent[6] != null) {
            ItemStack crystal = invContent[6];
            if (crystal.getItem() instanceof ItemCrystal) {
                power += CHARGESPEED - ItemCrystal.dischargeItem(crystal, CHARGESPEED);
            }
        }
    }
    
    @Override
    public void closeInventory() {
    }
    
    private void decharge() {
        power -= POWERUSAGE;
    }
    
    public void decrStackCreation() {
        for (int slot = 0; slot < 6; slot++) {
            getStackInSlot(slot).stackSize--;
            if (getStackInSlot(slot).stackSize == 0) {
                setInventorySlotContents(slot, null);
            }
        }
        
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
    public int[] getAccessibleSlotsFromSide(int var1) {
        if (var1 == 0 || var1 == 1) return new int[0];
        return slots;
    }
    
    public int getBuildTime() {
        return buildTime;
    }
    
    public int getEggInStock() {
        return eggInStock;
    }
    
    @Override
    public List<String> getInfo() {
        ArrayList<String> re = (ArrayList<String>) super.getInfo();
        re.add(StatCollector.translateToLocal("dc.progress") + " : " + getProgress() + "%");
        return re;
    }
    
    public ItemStack[] getInventory() {
        return invContent;
    }
    
    @Override
    public String getInventoryName() {
        return blockType.getLocalizedName();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public float getLifeBar(int i) {
        if (getStackInSlot(6 + i) != null) {
            ItemStack stack = getStackInSlot(6 + i);
            return stack.getItemDamage() == 0 ? 10 : (stack.getMaxDamage() - stack.getItemDamage()) / 1000;
        }
        return 0;
    }
    
    @Override
    public int getMaxChargeSpeed() {
        return CHARGESPEED;
    }
    
    @Override
    public int getMaxPower() {
        return MAXPOWER;
    }
    
    public byte getMode() {
        return mode;
    }
   
    public int getProgress() {
        return 100 * buildTime / _maxBuildTime;
    }
    
    public byte getRedstoneState() {
        return redState;
    }
    
    @Override
    public int getSizeInventory() {
        return invContent.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
        return invContent[i];
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        ItemStack itemstack = getStackInSlot(i);
        setInventorySlotContents(i, null);
        return itemstack;
    }
    
    public byte getStarted() {
        return started;
    }
    
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean hasStarted() {
        return getStarted() == 1;
    }
    
    public boolean isInventoryComplete() {
        if (!worldObj.isRemote) {
            boolean re = true;
            for (byte i = 0; i < 6; i++) {
                if (getStackInSlot(i) == null) {
                    re = false;
                    setStarted((byte) 0);
                }
            }
            return re;
        }
        return false;
        
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot == 0 || slot == 2) return itemstack.getItem() == Items.nether_star;
        else if (slot == 1) return itemstack.getItem() == Items.skull;
        else if (slot == 3 || slot == 5) return Item.getIdFromItem(itemstack.getItem()) == Block.getIdFromBlock(Blocks.obsidian);
        else if (slot == 4) return Item.getIdFromItem(itemstack.getItem()) == Block.getIdFromBlock(Blocks.diamond_block);
        else if (slot == 6) return itemstack.getItem() == DeadCraftItems.crystal;
        else return false;
    }
    
    public boolean isRepeatOn() {
        return mode == 0;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord, yCoord, zCoord) <= 25 && isUserAllowed(player.getCommandSenderName());
    }
    
    @Override
    public void openInventory() {
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
            }
        }
        
        buildTime = compound.getInteger("buildtime");
        created = compound.getBoolean("created");
        started = compound.getByte("started");
        eggInStock = compound.getInteger("eggInStock");
        redState = compound.getByte("redState");
        mode = compound.getByte("mode");
        
    }
    
    public void receiveInterfaceEvent(int buttonId) {
        
        if (!worldObj.isRemote) {
            switch (buttonId) {
                case 0:
                    if (hasStarted()) {
                        FMLLog.warning("Dragon Egg Sapwner already strated");
                    }
                    if (!hasStarted()) {
                        if (isInventoryComplete() && getProgress() == 0) {
                            decrStackCreation();
                            setStarted((byte) 1);
                        } else if (getProgress() > 0) {
                            setStarted((byte) 1);
                        }
                    }
                    break;
                case 1:
                    if (getRedstoneState() != 0) {
                        setRedstoneState((byte) 0);
                    }
                    break;
                case 2:
                    if (getRedstoneState() != 1) {
                        setRedstoneState((byte) 1);
                    }
                    break;
                case 3:
                    if (getRedstoneState() != 2) {
                        setRedstoneState((byte) 2);
                    }
                    break;
                case 4:
                    if (getMode() != 1) {
                        setMode((byte) 1);
                    }
                    break;
                case 5:
                    if (getMode() != 0) {
                        setMode((byte) 0);
                    }
                    break;
                case 6:
                    setStarted((byte) 0);
            }
        }
    }
    
    public void setBuildTime(int i) {
        buildTime = i;
        
    }
    
    public void setEggInStock(int data) {
        eggInStock = (byte) data;
        
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        invContent[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
    }
    
    public void setMode(byte i) {
        mode = i;
    }
    
    public void setPower(int data) {
        power = data;
        
    }
    
    public void setRedstoneState(byte i) {
        redState = i;
    }
    
    public void setStarted(byte started) {
        this.started = started;
    }
    
    private boolean spawnEgg() {
        
        if (!worldObj.isRemote) {
            if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && eggInStock >= 1) {
                worldObj.setBlock(xCoord, yCoord + 1, zCoord, Blocks.dragon_egg);
                eggInStock--;
                updateRedstone((byte) 1);
                wait = 5;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            if (!hasReceive && getStackInSlot(6) != null) {
                charge();
            }
            if (hasStarted() && getProgress() < 100) {
                if (power - POWERUSAGE > 0) {
                    decharge();
                    buildTime++;
                }
            }
            if (hasStarted() && getProgress() >= 100) {
                setBuildTime(0);
                setStarted((byte) 0);
                created = true;
                
                if (isRepeatOn()) {
                    receiveInterfaceEvent(0);
                }
                
            }
            if (created) {
                eggInStock++;
                created = false;
            }
            
            if (wait <= 0) {
                updateRedstone((byte) 0);
            } else {
                wait--;
            }
            
            spawnEgg();
            
        }
        
    }
    
    private void updateRedstone(byte state) {
        switch (redState) {
            case 0:
                if (state == 0) {
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
                } else if (state == 1) {
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
                }
                break;
            case 1:
                if (state == 0) {
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
                } else if (state == 1) {
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
                }
                break;
            case 2:
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
                break;
        }
        
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
        compound.setTag("Items", items);
        
        compound.setInteger("buildtime", buildTime);
        compound.setBoolean("created", created);
        compound.setByte("started", getStarted());
        compound.setInteger("eggInStock", eggInStock);
        compound.setByte("redState", redState);
        compound.setByte("mode", mode);
    }    
}
