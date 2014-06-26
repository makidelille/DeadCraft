package mak.dc.common.tileEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import mak.dc.DeadCraft;
import mak.dc.common.blocks.BlockEnderConverter;
import mak.dc.common.items.ItemCrystal;
import mak.dc.common.util.Config.ConfigLib;
import mak.dc.common.util.IPowerReceiver;
import mak.dc.common.util.IPowerSender;
import mak.dc.common.util.PowerManager;
import mak.dc.network.pipeline.packets.DeadCraftEnderConverterPacket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

public class TileEntityEnderConverter extends TileEntityDeadCraft implements IPowerSender, IInventory {
    
    public static final int MAXPOWER = 10 * ConfigLib.BASE_MAXCHARGE;
    private static final int CHARGERATE = ConfigLib.MAX_CHARGESPEED;
    private static final int MAXTRANSFERTRATE = 10 * CHARGERATE;
    
    private static PowerManager powerManager = DeadCraft.powerManager.getInstance();
    
    private int power;
    private ItemStack inv;
    private boolean isSync;
    
    private int powerInItem;
    private HashMap<IPowerReceiver, Integer> receivers;
    
    public TileEntityEnderConverter() {
        super(true);
        receivers = new HashMap<IPowerReceiver, Integer>();
    }
    
    @Override
    public void closeInventory() {
        isSync = false;
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
    public void delete() {
        Iterator it = receivers.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            ((IPowerReceiver) entry.getKey()).setSourceChange();
        }
        
    }
    
    @Override
    public int[] getCoord() {
        return new int[] { xCoord, yCoord, zCoord };
    }
    
    @Override
    public List<String> getInfo() {
        ArrayList<String> re = new ArrayList();
        re.add(StatCollector.translateToLocal("dc.power") + " : " + power);
        re.add(StatCollector.translateToLocal("dc.block.enderConverter.gui.powerLeft") + " : " + powerInItem);
        re.add(StatCollector.translateToLocal("dc.block.power.info.connectionNb") + " : " + receivers.size());
        return re;
    }
    
    @Override
    public String getInventoryName() {
        return blockType.getLocalizedName();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public int getMaxTransfertRate() {
        return MAXTRANSFERTRATE;
    }
    
    public int getPower() {
        return power;
    }
    
    public int getPowerLeft() {
        return powerInItem;
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
    public ItemStack getStackInSlotOnClosing(int var1) {
        return inv;
    }
    
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return true;
    }
    
    @Override
    public void onAskedPower(IPowerReceiver receiver, int amount) {
        if (!worldObj.isRemote) {
            if (MAXTRANSFERTRATE - amount < 0) {
                amount = MAXTRANSFERTRATE;
            }
            if (receiver != null) {
                receivers.put(receiver, amount);
            }
        }
        
    }
    
    @Override
    public void openInventory() {
        isSync = false;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        power = nbtTagCompound.getInteger("power");
        powerInItem = nbtTagCompound.getInteger("powerItem");
        NBTTagList items = (NBTTagList) nbtTagCompound.getTag("Items");
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getByte("Slot");
            
            if (slot >= 0 && slot < getSizeInventory()) {
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
            }
        }
    }
    
    @Override
    public void sendPower() {
        if (!worldObj.isRemote) {
            Iterator it = receivers.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                int amountAsked = (Integer) entry.getValue();
                IPowerReceiver receiver = (IPowerReceiver) entry.getKey();
                if (power >= amountAsked) {
                    receiver.recievePower(amountAsked);
                    power -= amountAsked;
                    receivers.remove(entry);
                }
            }
        }
        
    }
    
    @Override
    public void setInventorySlotContents(int var1, ItemStack itemstack) {
        inv = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        
    }
    
    public void setPower(int power) {
        this.power = power;
    }
    
    public void setPowerLeft(int data) {
        powerInItem = data;
        
    }
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        BlockEnderConverter.renderPass = pass;
        return pass == 0 || pass == 1 || pass == 2;
    }
    
    public void sync() {
        DeadCraft.packetPipeline.sendToDimension(new DeadCraftEnderConverterPacket(this), worldObj.getWorldInfo().getVanillaDimension());
    }
    
    @Override
    public void syncWithplayer(EntityPlayer player) {
        DeadCraft.packetPipeline.sendTo(new DeadCraftEnderConverterPacket(this), (EntityPlayerMP) player);
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        
        if (!worldObj.isRemote) {
            if (!isSync) {
                sync();
            }
            
            ItemStack fuel = getStackInSlot(0);
            if (fuel != null && powerInItem <= 0) {
                if (powerManager.isFuel(fuel) && power < MAXPOWER - CHARGERATE) {
                    powerInItem = powerManager.getPowerProduce(fuel);
                    if (fuel.getItem() instanceof ItemCrystal) {
                        ItemCrystal.dischargeItem(fuel, powerInItem);
                    } else {
                        fuel.stackSize--;
                    }
                    if (fuel.stackSize <= 0) {
                        setInventorySlotContents(0, null);
                    }
                }
                if (!powerManager.isFuel(fuel)) {
                    worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d, fuel));
                    setInventorySlotContents(0, null);
                }
            }
            if (powerInItem > 0) {
                if (power + CHARGERATE < MAXPOWER && powerInItem - CHARGERATE > 0) {
                    powerInItem -= CHARGERATE;
                    power += CHARGERATE;
                }else if(power + CHARGERATE < MAXPOWER){
                    power += powerInItem;
                    powerInItem = 0;                    
                } else {
                    int space = MAXPOWER - power;
                    powerInItem -= space;
                    power += space;
                }
            }else{
                powerInItem = 0;
            }
            
            if (!receivers.isEmpty()) {
                sendPower();
            }
            
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("power", power);
        nbtTagCompound.setInteger("powerItem", powerInItem);
        
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
        nbtTagCompound.setTag("Items", items);
        
    }
    
}
