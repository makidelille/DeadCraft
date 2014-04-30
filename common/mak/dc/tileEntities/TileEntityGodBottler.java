package mak.dc.tileEntities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import mak.dc.DeadCraft;
import mak.dc.items.ItemGodCan;
import mak.dc.items.ItemLifeCrystal;
import mak.dc.items.crafting.CanCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityGodBottler extends TileEntityDeadCraft implements IInventory{
	
	private static final byte deadcraftId = 2;
	
	/**
	 * manager used for recipes
	 */
	private static final CanCraftingManager canCraftingManager = DeadCraft.canCraftingManager;
	
	/**
	 * Animation speed (client Side)
	 */
	public static final int ANIMATIONTIME = 20;
	
	/**
	 * time to build (Server Side) && the max power usage buffer
	 */
	public static final int BUILDTIME = 100;
	public static final int MAXPOWER = 5_000;
	
	/**
	 * rate of the charge and of the usage
	 */
	private static final int CHARGESPEED = 50;
	private static final int POWERUSAGE = 1;
	
	

	private TileEntityGodBottler pair;

	private int clientTick = 0;
	private boolean isSync = false;
	
	private int workedTime;
	private int power;

	/**Slot 0 : Energy; 
	 * Slot 1 : input can;
	 * Slot 2 : output can;
	 * Slot 3,8 : input resources;
	 */
	private ItemStack[] inventory ;
	
	private boolean hasIngredientsChanged;
	/**sub construct
	 * return this(false) = this(false,0)
	 */
	public TileEntityGodBottler() {
		this(false);
	}
	
	/**Main constructor 
	 * @param top
	 */
	public TileEntityGodBottler(boolean top) {
		super(true);
		inventory = new ItemStack[9];
		this.clientTick = 0;
		this.workedTime = 0;
	}
	
	
	
	 
	public boolean canUpdate() {
		return true;
	}	
	
	
	
	 
	public void updateEntity() {
		if(pair == null || !(0 < this.blockMetadata && this.blockMetadata <= 4) ) isSync = false;
		if(!isSync) {
			sync();
			this.blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			return;
		}
		if(worldObj.isRemote) {
			if(this.isRSPowered() ) this.setClientTick(this.getClientTick() + 1);
			else if(!this.isRSPowered()) this.setClientTick(this.getClientTick() - 1);			
		}
		if(!worldObj.isRemote) {
			Map<Integer,ItemStack> mapIs = this.getIngredientStacks() ;
			ItemStack[] is = this.getItemStackFromMap(mapIs);
			
			int idMatchingrecipe = canCraftingManager.getMatchingRecipes(is);

			if(this.getStackInSlot(0) != null) {
				this.charge();
			}
					
			if(this.hasEmptyCan() && idMatchingrecipe != -1 && this.hasPower() && this.isRSPowered()) {
				ItemStack can = this.getEmptyCan();
			
				if(workedTime >= BUILDTIME ) 
					if(this.craftCan(can,mapIs)) this.workedTime = 0;			
					else workedTime = BUILDTIME;
				else{
					workedTime++;
					this.decharge();
				}
				
			}else if(!this.hasEmptyCan()) this.workedTime = 0; 
			else if(hasIngredientsChanged) this.workedTime = 0;
		}
	}
	
	private void charge() {
		if(this.power >= this.MAXPOWER ) return;
		else if(this.inventory[0] != null){
			ItemStack crystal = inventory[0];
			System.out.println(crystal.getMaxDamage()  - CHARGESPEED );
			if(crystal.getItem() instanceof ItemLifeCrystal && crystal.getItemDamage() <= crystal.getMaxDamage()  - CHARGESPEED){
				ItemLifeCrystal itemCrystal= (ItemLifeCrystal)crystal.getItem();
				itemCrystal.dischargeItem(crystal, CHARGESPEED);
				this.power += CHARGESPEED;
			}
		}
		
	}
	
	private void decharge() {
		this.power -= POWERUSAGE;
	}

	private boolean  craftCan(ItemStack can, Map<Integer, ItemStack> mapIs) {
		if(this.getStackInSlot(2) == null) {
			ItemStack[] iss = this.getItemStackFromMap(mapIs);
			ItemStack newIs = canCraftingManager.craftCan(can, iss);
			
			this.setInventorySlotContents(2, newIs);
			this.setInventorySlotContents(1, null);
			
			Iterator iterator = mapIs.entrySet().iterator();
	        Entry entry;
	        do {
	            entry = (Entry)iterator.next();
	            ItemStack is = (ItemStack) entry.getValue();
	            is.stackSize--;
	            if(is.stackSize > 0) this.setInventorySlotContents((int)entry.getKey(),is);
	            else this.setInventorySlotContents((int)entry.getKey(), null);
	        }while (iterator.hasNext());
			
	        return true;
		}else return false;
	}


	private void sync() {
		if(pair == null) {
			pair = (TileEntityGodBottler) worldObj.getTileEntity(xCoord, yCoord + (isTop() ? -1 : 1), zCoord);
			if(pair == null) return;
		}
		if(this.isTop()) clientSetup(pair);
		pair.allowed = this.allowed;
		pair.owner = this.owner;
		pair.locked = this.locked;

		this.blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		isSync = true;
	}


	public ItemStack decrStackSize(int slot, int nb) {
		if(this.isTop()) return pair.decrStackSize(slot, nb);
		else{
			ItemStack itemstack = getStackInSlot(slot);
			
			if (itemstack != null) {
				if (itemstack.stackSize <= nb) {
					setInventorySlotContents(slot, null);
				}else{
					itemstack = itemstack.splitStack(nb);
				}}
			return itemstack;
		}
	}	
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);	
		this.clientTick = nbtTagCompound.getInteger("cTick");
		this.blockMetadata = nbtTagCompound.getInteger("meta");
		if(this.isTop()) readPairData(nbtTagCompound);
		else {
			NBTTagList items = (NBTTagList) nbtTagCompound.getTag("Items");
			for (int i = 0; i < items.tagCount(); i++) {
				NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
				int slot = item.getByte("Slot");
				
				if (slot >= 0 && slot < getSizeInventory()) {
					setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
				}
			}
			this.power = nbtTagCompound.getInteger("power");
			this.workedTime = nbtTagCompound.getInteger("workTime");
		}
		this.isSync = false;
	}
	
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("cTick", clientTick);
		nbtTagCompound.setInteger("meta", this.blockMetadata);
		if(this.isTop()) writePairData(pair, nbtTagCompound);
		else {
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
			nbtTagCompound.setInteger("power", this.power);
			nbtTagCompound.setInteger("workTime", workedTime);
		}
		this.isSync = false;
	}
	
	private void readPairData(NBTTagCompound tag) {
		this.owner = tag.getString("owner");
		int nbersAll = tag.getInteger("nbAllowed");
		for (int i = 0; i < nbersAll; i++ ) {
			allowed.add(tag.getString("allowed [" +i+ "]"));
		}
		this.locked = tag.getBoolean("locked");
		
	}
	
	private void writePairData(TileEntityGodBottler pair, NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setString("owner", pair.owner);
		int nbersAll = pair.allowed.size();
		if(pair.allowed.size() != 0) {       
			for (int i = 0; i < nbersAll; i++ ) 
				nbtTagCompound.setString("allowed [" +i+ "]" , pair.allowed.get(i).toString());
			nbtTagCompound.setInteger("nbAllowed", nbersAll);
		}
		nbtTagCompound.setBoolean("locked", pair.locked);
		
	}
	
	public void clientSetup(TileEntityGodBottler te) {
		this.clientTick = 0;
	}
	
	public void setup(TileEntityGodBottler te) {
		this.pair = te;		
		this.allowed = te.allowed;
		this.owner = te.owner;
		this.locked = te.locked;
		this.isSync = false;
	}
	
	/**
	 * here are all the boolean is and has 
	 */
	
	
	 
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		if(isTop()) return pair.isItemValidForSlot(var1, var2);
		else {
			switch (var1) {
			case 0: return var2.getItem() instanceof ItemLifeCrystal;
			case 1:	return var2.getItem() instanceof ItemGodCan;
			case 2: return false;
			default : return true;
			}
		}
	}
	public boolean isRSPowered() {
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord + (isTop() ? -1 : 1), zCoord);
	}
	public boolean isTop() {
		return this.blockMetadata == 4;
	}
	 
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return isTop() ? pair.isUseableByPlayer(var1) : super.isUseableByPlayer(var1);
	}
	
	
	
	
	
	
	
	 
	public boolean hasCustomInventoryName() {
		return false;
	}
	public boolean hasEmptyCan() {
		return this.inventory[1] != null && this.inventory[1].stackSize == 1 && (this.inventory[1].getItem() instanceof ItemGodCan);
	}
	
	public boolean hasPower() {
		return isTop() ? pair.hasPower() : this.power > 0;
	}
	public boolean hasRessources() {
		for(int i = 2; i < inventory.length; i++) {
			if(inventory[i] != null && inventory[i].stackSize > 0)
				return true;
		}
		return false;
	}
	public boolean hasStarted() {
		if(pair == null) return false;
		return isTop() ? pair.hasStarted() : this.workedTime > 0;
	}
	
	

	/**
	 * here are the getters
	 */
	
	
	public int getDirection() {
		if(pair == null) return 0;
		return isTop() ? pair.getDirection() : this.blockMetadata;
	}
	private ItemStack getEmptyCan() {
		return inventory[1];
	}
	private Map<Integer,ItemStack> getIngredientStacks() {
		Map<Integer,ItemStack> is = new HashMap();
		for(int i = 2; i < inventory.length; i++) {
			if(inventory[i] != null && inventory[i].stackSize > 0)
				is.put(i, getStackInSlot(i));
		}
		return is;
	}
	public String getInventoryName() {
		return "GodBottler";
	}
	public int getInventoryStackLimit() {
		return 64;
	}
	private ItemStack[] getItemStackFromMap(Map<Integer, ItemStack> ing) { 
		ItemStack[] re = new ItemStack[ing.size()];
		int i=0;
		Iterator iterator = ing.entrySet().iterator();
		Entry entry;
		while (iterator.hasNext()) {
			entry = (Entry)iterator.next();
			re[i] = (ItemStack) entry.getValue();
		}
		return re;
	}
	public TileEntityGodBottler getPair() {
		return this.pair;
	}
	public int getPower() {
		return isTop() ? pair.getPower() : this.power;
	}
	public int getSizeInventory() {
		return inventory.length;
	}
	public ItemStack getStackInSlot(int var1) {
		return isTop() ? pair.getStackInSlot(var1) : this.inventory[var1];
	}
	public ItemStack getStackInSlotOnClosing(int var1) {
		return isTop() ? pair.getStackInSlotOnClosing(var1) : this.inventory[var1];
	}
	public int getWorkedTime() {
		return isTop() ? pair.getWorkedTime() : this.workedTime;
	}
	
	/**
	 * here are the setters
	 */
	
	
	public void setDirection(int i) {
		this.blockMetadata = i;
		this.isSync = false;
	}
	 
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if (isTop()) {
			pair.setInventorySlotContents(var1, var2);
			return;
		}else{
			inventory[var1] = var2;
			if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
				var2.stackSize = getInventoryStackLimit();
			if(var1 > 2 && var1 < this.getSizeInventory()) this.hasIngredientsChanged = true;
			else this.hasIngredientsChanged = false;
		}	
	}
	public void setPair(TileEntityGodBottler te) {
		this.pair = te;
	}
	public void setPower(int par) {
		this.power = par;
	}
	public void setTop() {
		this.blockMetadata = 4;
		this.isSync = false;
	}
	public void setWorkedTime(int data) {
		this.workedTime = data;
	}
	
	
	
	 
	public void openInventory() {}
	
	 
	public void closeInventory() {}
	
	
	
	
	
	/**
	 *  Client used functions
	 */
	public int getClientTick() {
		return clientTick;
	}
	public void setClientTick(int tick) {
		if(tick < ANIMATIONTIME && tick > 0) this.clientTick = tick;
		else if(tick >= ANIMATIONTIME) this.clientTick = ANIMATIONTIME;
		else if(tick <= 0) this.clientTick = 0;
	}
	
	/** for display only (client Side)
	 * @return should render the can or not
	 */
	public boolean CLIENThasCan() {
		return this.inventory[1] != null || this.inventory[2] != null;
	}



	
	
}
