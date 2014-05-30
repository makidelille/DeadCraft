package mak.dc.tileEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import mak.dc.DeadCraft;
import mak.dc.blocks.BlockEnderConverter;
import mak.dc.items.ItemCrystal;
import mak.dc.network.packet.DeadCraftEnderConverterPacket;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import mak.dc.util.PowerManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityEnderConverter extends TileEntityDeadCraft implements IPowerSender,IInventory{

	
	public static final int MAXPOWER = 50_000;
	private static final int CHARGERATE = 50;

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
	public void updateEntity() {
		super.updateEntity();
		
		if(!worldObj.isRemote) {
			if(!isSync) sync();
			
			ItemStack fuel = this.getStackInSlot(0);
			if(fuel != null && powerInItem <= 0) {
				if(powerManager.isFuel(fuel) && power < MAXPOWER - CHARGERATE){
					this.powerInItem = powerManager.getPowerProduce(fuel);
					if((fuel.getItem() instanceof ItemCrystal)){
						ItemCrystal.dischargeItem(fuel, powerInItem);						
					}else fuel.stackSize--;
					if(fuel.stackSize <= 0) {
						this.setInventorySlotContents(0, null);
					}
				}
				if(!powerManager.isFuel(fuel)){
					worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord +0.5d, yCoord +0.5d, zCoord+0.5d, fuel));
					setInventorySlotContents(0, null);
				}
			}
			if(powerInItem > 0) {
				if(power + CHARGERATE < MAXPOWER) {
					powerInItem -= CHARGERATE;
					power +=CHARGERATE;			
				}else{
					int space = MAXPOWER - power;
					powerInItem -= space;
					power += space;
				}
			}
					
		if(!receivers.isEmpty()) sendPower();
			
		}
	}
	
	public void sync() {
		DeadCraft.packetPipeline.sendToDimension(new DeadCraftEnderConverterPacket(this), worldObj.getWorldInfo().getVanillaDimension());
	}
	
	@Override
	public void syncWithplayer(EntityPlayerMP player) {
		DeadCraft.packetPipeline.sendTo(new DeadCraftEnderConverterPacket(this), player);
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.power = nbtTagCompound.getInteger("power");
		this.powerInItem = nbtTagCompound.getInteger("powerItem");
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
		nbtTagCompound.setInteger("powerItem", powerInItem);
	
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
	public void sendPower() {
		if(!worldObj.isRemote) {
			Iterator it = receivers.entrySet().iterator();
			while(it.hasNext()) {
				Entry entry = (Entry) it.next();
				int amountAsked = (int) entry.getValue();
				IPowerReceiver receiver = (IPowerReceiver) entry.getKey();
				if(power >= amountAsked) {
					receiver.recievePower(amountAsked);
					this.power -= amountAsked;
					receivers.remove(entry);
				}
			}
		}
		
	}


	@Override
	public void onAskedPower(IPowerReceiver receiver,int amount) {
		if(!worldObj.isRemote) {
			if(power - amount <0)
				amount = power;
			if(receiver != null) this.receivers.put(receiver, amount);
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
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
	            itemstack.stackSize = getInventoryStackLimit();
		}
		
		
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
	public void openInventory() {
		this.isSync = false;
	}

	@Override
	public void closeInventory() {
		this.isSync = false;
	}

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
	
	public int getPowerLeft() {
		return this.powerInItem;
	}

	public void setPowerLeft(int data) {
		this.powerInItem = data;
		
	}
	
	@Override
	public List<String> getInfo() {
		ArrayList<String> re = new ArrayList();
		re.add("power : " + this.power);
		re.add("power left in item :" + this.powerInItem);
		re.add("connections :"  + this.receivers.size());
		return re;
	}

	@Override
	public int[] getCoord() {
		return new int[]{xCoord,yCoord,zCoord};
	}
	
	

	@Override
	public void delete() {
		Iterator it = receivers.entrySet().iterator();
		while(it.hasNext()) {
			Entry entry = (Entry) it.next();
			((IPowerReceiver) entry.getKey()).setSourceChange();
		}
		
	}

}
