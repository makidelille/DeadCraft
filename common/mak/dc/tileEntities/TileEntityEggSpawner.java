package mak.dc.tileEntities;

import mak.dc.client.sounds.Sound;
import mak.dc.items.ItemLifeCrystal;
import mak.dc.lib.Lib;
import mak.dc.lib.TileEntitiesInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLLog;

public class TileEntityEggSpawner extends TileEntity implements IInventory {
		
	/**handle by config */
	private static boolean DEBUG = Lib.DEBUG;
	
	public static int _maxBuildTime;
	
	
	private ItemStack[] invContent;

	private int buildTime ;

	private byte started;
	private boolean created;
	private int eggInStock;

	private byte wait;
	private byte redState = 0;
	private byte mode = 1;


	public TileEntityEggSpawner() {
		invContent = new ItemStack[9];
		setStarted((byte) 0);
		created = false;
		buildTime = 0;
		eggInStock = 0;	
	}
	
	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(hasStarted() && getProgress() < 100) {
				buildTime += Math.max((int)getLifeMultiplier(),1);
				tickLifeCrystal();
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
			
			onInventoryChanged();
			spawnEgg();
			
		
		}
		

	}
	
	
	private void tickLifeCrystal() {
		int maxDmg = ItemLifeCrystal._maxValue;
		
		if(!worldObj.isRemote && !DEBUG) {
			if(Math.random() > 0.6) {
				ItemStack crystal = invContent[6];
				if(crystal != null && crystal.getItemDamage() != maxDmg - 1) {
					crystal.setItemDamage(crystal.getItemDamage() + 1);
				}else{
					crystal = invContent[7];
					if(crystal != null && crystal.getItemDamage() != maxDmg - 1) {
						crystal.setItemDamage(crystal.getItemDamage() + 1);
					}
				}
			}
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

	
	public int getLifeMultiplier() {
		int re = 0;
		for (int i = 6 ; i < 8 ; i++) {
				if(getStackInSlot(i) != null) {
					ItemStack stack = getStackInSlot(i);
					if (stack.getItemDamage() <= stack.getMaxDamage()) {
						int dmg = stack.getMaxDamage() - stack.getItemDamage();
						int coeff = (int)(((double)dmg) / stack.getMaxDamage() * 50);
						re += coeff;
					}
				}
			}
		return re;
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
		onInventoryChanged();
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
	        onInventoryChanged();
		
	}
		
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot == 0 || slot == 2)
			return itemstack.getItem() == Item.netherStar ;
		else if(slot == 1)
			return itemstack.getItem() == Item.skull;
		else if(slot == 3 || slot == 5)
			return itemstack.itemID == Block.obsidian.blockID;
		else if (slot == 4)
			return itemstack.itemID == Block.blockDiamond.blockID;
		else if(slot ==  6 || slot == 7)
			return itemstack.getItem() instanceof ItemLifeCrystal;
		else		
			return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getInvName() {
		return TileEntitiesInfo.EGGSPAWNER_TILE_NAME;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(xCoord, yCoord, zCoord) <= 25;
	}
	
	
	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
	
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
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		NBTTagList items = compound.getTagList("Items");
		
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound)items.tagAt(i);
			int slot = item.getByte("Slot");
			
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}
		
		
		buildTime = compound.getInteger("buildtime");
		created = compound.getBoolean("created");
		setStarted(compound.getByte("started"));
		eggInStock = compound.getInteger("eggInStock");
		redState = compound.getByte("redState");
		mode = compound.getByte("mode");

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


	private void decrStackCreation() {
			for (int slot = 0; slot < 6; slot++) {
				getStackInSlot(slot).stackSize--;
				if(getStackInSlot(slot).stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			onInventoryChanged();
						
				
		}
			
	}

	public void setEggInStock(int data) {
		eggInStock = (byte) data;
		
	}
	
	private boolean spawnEgg() {
		
		
		if(!worldObj.isRemote) {
			if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && eggInStock >= 1) {
				worldObj.setBlock(xCoord, yCoord + 1, zCoord, Block.dragonEgg.blockID);
				eggInStock--;
				updateRedstone((byte) 1);
				wait = 5;
				Sound.EGG_SPAWN.play(xCoord +0.5D, yCoord +0.5D, zCoord + 0.5D, 3.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
//				worldObj.playSound(xCoord, yCoord, zCoord, "", 3.0F, orldObj.rand.nextFloat() * 0.1F + 0.9F, true);
				return true;
			}	
		}
		return false;
	}
	

	private boolean isInventoryComplete() {
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

	public boolean isRepeatOn(int i) {
		return mode == i;
	}

	public boolean isRepeatOn() {
		return isRepeatOn(0);
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


}
