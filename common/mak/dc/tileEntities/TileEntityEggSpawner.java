package mak.dc.tileEntities;

import mak.dc.items.DeadCraftItems;
import mak.dc.items.ItemCrystal;
import mak.dc.lib.IBTInfos;
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
import cpw.mods.fml.common.FMLLog;

public class TileEntityEggSpawner extends TileEntityDeadCraft implements IInventory,ISidedInventory {
		
	private static final byte deadcraftId = 1;

	public static final int MAXPOWER = 100_000;

	public static final int CHARGESPEED = 1_000;
	public static final int POWERUSAGE = 5;

	private static final int[] slots = {0,1,2,3,4,5,6,7};
	
	public static int _maxBuildTime = 18_000;
	
	
	private ItemStack[] invContent;

	private int buildTime ;

	private byte started;
	private boolean created;
	private int eggInStock;

	private byte wait;
	private byte redState = 0;
	private byte mode = 1;

	private int power;


	public TileEntityEggSpawner() {
		super(true);
		invContent = new ItemStack[8];
		setStarted((byte) 0);
		created = false;
		buildTime = 0;
		eggInStock = 0;	
	}
	
	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(this.getStackInSlot(6) != null)
				charge();
			if(hasStarted() && getProgress() < 100) {
				if(this.power - POWERUSAGE > 0) {
					this.decharge();
					buildTime++;
				}
			}if(hasStarted() && getProgress() >= 100) {
				setBuildTime(0);
				setStarted((byte) 0);
				created = true;
				
				if ( isRepeatOn() )
					receiveInterfaceEvent(0);
				
			}
			if(created) {
				eggInStock++;
				created = false;
			}
			
			if(wait <= 0)
				updateRedstone((byte) 0);
			else
				wait--;
			
			spawnEgg();
			
		
		}
		

	}
	private void updateRedstone(byte state) {
		switch(redState) {
		case 0:
			if (state == 0)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			else if (state == 1)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
			break;
		case 1 : 
			if (state == 0)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
			else if (state == 1)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			break;
		case 2 : 
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			break;
		}
		
	}

	public boolean hasStarted() {
		return getStarted() == 1;
	}

	public void setBuildTime(int i) {
		this.buildTime = i;
		
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
	public ItemStack decrStackSize(int i, int count) {
		ItemStack itemstack = getStackInSlot(i);
		
		if (itemstack != null) {
			if (itemstack.stackSize <= count) {
				setInventorySlotContents(i, null);
			}else{
				itemstack = itemstack.splitStack(count);
			}}
		return itemstack;
		
	}
	

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack itemstack = getStackInSlot(i);
		setInventorySlotContents(i, null);
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		invContent[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
	            itemstack.stackSize = getInventoryStackLimit();
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot == 0 || slot == 2)
			return itemstack.getItem() == Items.nether_star ;
		else if(slot == 1)
			return itemstack.getItem() == Items.skull;
		else if(slot == 3 || slot == 5)
			return Item.getIdFromItem(itemstack.getItem()) == Block.getIdFromBlock(Blocks.obsidian);
		else if (slot == 4)
			return Item.getIdFromItem(itemstack.getItem()) == Block.getIdFromBlock(Blocks.diamond_block);
		else if(slot ==  6)
			return itemstack.getItem() == DeadCraftItems.crystal;
		else		
			return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(xCoord, yCoord, zCoord) <= 25 && this.isUserAllowed(player.getCommandSenderName());
	}
	
	
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
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
		compound.setTag("Items", items);
		
		compound.setInteger("buildtime", buildTime);
		compound.setBoolean("created", created);
		compound.setByte("started", getStarted());
		compound.setInteger("eggInStock", eggInStock);
		compound.setByte("redState", redState);
		compound.setByte("mode", mode);
		compound.setInteger("power", power);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		NBTTagList items = (NBTTagList) compound.getTag("Items");
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
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
		power = compound.getInteger("power");

	}


	public float getLifeBar(int i) {
		if (getStackInSlot(6 + i) != null) {
			ItemStack stack = getStackInSlot(6+ i);
			return stack.getItemDamage() == 0 ? 10 : (stack.getMaxDamage() - stack.getItemDamage()) / 1000 ;
		}
		return 0;
	}
	

	public int getBuildTime() {
		return this.buildTime;
	}

	public int getEggInStock() {
		return eggInStock;
	}

	public int getProgress() {
		return 100 * buildTime / _maxBuildTime;
	}

	public void receiveInterfaceEvent(int buttonId) {

		if(!worldObj.isRemote) {
			switch(buttonId) {
			case 0:
				if(hasStarted())
					FMLLog.warning("Dragon Egg Sapwner already strated");
				if(!hasStarted()) {
					if(isInventoryComplete() && getProgress() == 0) {
						decrStackCreation();
						this.setStarted((byte) 1);
					}else if(getProgress() > 0) {
						this.setStarted((byte) 1);
					}
				}
				break;
			case 1 :
				if(getRedstoneState() != 0) {
					setRedstoneState((byte)0);
				}break;
			case 2:
				if(getRedstoneState() != 1) {
					setRedstoneState((byte)1);
				}break;
			case 3:
				if(getRedstoneState() != 2) {
					setRedstoneState((byte)2);
				}break;
			case 4:
				if(getMode() != 1 )
					setMode((byte)1);
				break;
			case 5:
				if(getMode() != 0 )
					setMode((byte)0);
				break;
			case 6 :
				setStarted((byte) 0);
			}
		}
	}


	public void decrStackCreation() {
			for (int slot = 0; slot < 6; slot++) {
				getStackInSlot(slot).stackSize--;
				if(getStackInSlot(slot).stackSize == 0) {
					setInventorySlotContents(slot, null);
				}				
		}
			
	}

	public void setEggInStock(int data) {
		eggInStock = (byte) data;
		
	}
	
	private boolean spawnEgg() {
		
		
		if(!worldObj.isRemote) {
			if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && eggInStock >= 1) {
				worldObj.setBlock(xCoord, yCoord + 1, zCoord, Blocks.dragon_egg);
				eggInStock--;
				updateRedstone((byte) 1);
				wait = 5;
				return true;
			}	
		}
		return false;
	}
	

	public boolean isInventoryComplete() {
		if(!worldObj.isRemote) {
			boolean re = true;
			for (byte i = 0; i < 6; i++) {
				if(getStackInSlot(i) == null) {
					re = false;
					this.setStarted((byte) 0);
				}
			}	
			return re;
			}
		return false;
		
	}
	
	public void setRedstoneState(byte i ) {
		redState = i;
	}
	public byte getRedstoneState() {
		return redState;
	}
	

	public void setMode(byte i) {
		mode = i;
	}

	public byte getMode() {
		return mode;
	}


	public boolean isRepeatOn() {
		return mode == 0;
	}

	public ItemStack[] getInventory() {
		return invContent;
	}

	public byte getStarted() {
		return started;
	}

	public void setStarted(byte started) {
		this.started = started;
	}

	@Override
	public void closeInventory() {}

	@Override
	public String getInventoryName() {
		return blockType.getLocalizedName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	private void charge() {
		if(this.power + CHARGESPEED > this.MAXPOWER ) return;
		else if(this.invContent[6] != null){
			ItemStack crystal = invContent[6];
			if(crystal.getItem() instanceof ItemCrystal){
				this.power += CHARGESPEED - ItemCrystal.dischargeItem(crystal, CHARGESPEED);
			}
		}
	}
	private void decharge() {
		this.power -= POWERUSAGE;
	}
	
	@Override
	public void openInventory() {}

	public int getPower() {
		return this.power;
	}

	public void setPower(int data) {
		this.power = data;
		
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(var1 == 0 || var1 == 1) return new int[0];
		return slots;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return isItemValidForSlot(var1, var2);
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return false;
	}



}
