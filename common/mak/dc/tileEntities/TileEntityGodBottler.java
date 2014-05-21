package mak.dc.tileEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mak.dc.DeadCraft;
import mak.dc.items.ItemCrystal;
import mak.dc.items.ItemGodCan;
import mak.dc.items.crafting.CanCraftingManager;
import mak.dc.lib.IBTInfos;
import mak.dc.network.packet.DeadCraftGodBottlerPacket;
import mak.dc.util.IPowerSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class TileEntityGodBottler extends TileEntityDeadCraftWithPower implements IInventory, ISidedInventory{
	
	private static final byte deadcraftId = 2;
	private static final int[] slot_top = {1};
	private static final int[] slot_bottom = {0,2};
	private static final int[] slot_side = {3,4,5,6,7,8};
	private static final int[] slot_back = {0};

	/**
	 * manager used for recipes
	 */
	private static final CanCraftingManager canCraftingManager = DeadCraft.canCraftingManager;
	
	/**
	 * Animation speed (client Side)
	 */
	public static final int ANIMATIONTIME = 20;
	
	/**
	 * time to build (Server Side) && the size of power buffer
	 */
	public static final int BUILDTIME = 100;
	public static final int MAXPOWER = 5_000;
	
	/**
	 * rate of the charge and of the decharge
	 */
	private static final int MAXCHARGESPEED = 10;
	private static final int POWERUSAGE = 10;
	
	
	private IPowerSender powerSource;
	private TileEntityGodBottler pair;

	private int clientTick = 0;
	private boolean isSync = false;
//	private boolean hasReceive;
	
	private int workedTime;
//	private int power;

	/**Slot 0 : Energy; 
	 * Slot 1 : input can;
	 * Slot 2 : output can;
	 * Slot 3,8 : input resources;
	 */
	private ItemStack[] inventory ;
	
	private boolean hasIngredientsChanged;
	
	private ArrayList<EnumBuildError> buildErrors = new ArrayList<EnumBuildError>();
	
	
	public TileEntityGodBottler() {
		super();
		inventory = new ItemStack[9];
		this.clientTick = 0;
		this.workedTime = 0;
	}
		 
	public boolean canUpdate() {
		return true;
	}	
	
	
	
	 
	public void updateEntity() {
		super.updateEntity();
		if(pair == null || !(0 < this.blockMetadata && this.blockMetadata <= 4) ) isSync = false;
		if(!isSync) {
			sync();
			this.blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		}
		if(pair == null) {
			isSync = false;
			return;
		}
		if(worldObj.isRemote) {
			if(this.isRSPowered() ) this.setClientTick(this.getClientTick() + 1);
			else if(!this.isRSPowered()) this.setClientTick(this.getClientTick() - 1);			
		}
		if(!worldObj.isRemote) {			
			if(this.isRSPowered()) {
				this.idleDecharge();
				if(buildErrors.contains(EnumBuildError.NOREDSTONE)) {
					buildErrors.remove(EnumBuildError.NOREDSTONE);
					isSync = false;
				}
			}else{
				if(workedTime != 0){
					this.workedTime = 0;
					isSync = false;
				}
				if(!buildErrors.contains(EnumBuildError.NOREDSTONE)) {
					buildErrors.add(EnumBuildError.NOREDSTONE);
					isSync = false;
				}
			}		
			if(!hasReceive && this.getStackInSlot(0) != null) {
				this.charge();
			}
			
			if(this.hasEmptyCan()) {
				if(buildErrors.contains(EnumBuildError.NOEMMPTYCAN)){
					buildErrors.remove(EnumBuildError.NOEMMPTYCAN);
					isSync = false;
				}
			}else {
				if(!buildErrors.contains(EnumBuildError.NOEMMPTYCAN)){
					buildErrors.add(EnumBuildError.NOEMMPTYCAN);
					isSync = false;
				}
			}
			
			int idMatchingrecipe =this.getMatchingRecipeId();
			
			if(idMatchingrecipe == -1) {
				if(!buildErrors.contains(EnumBuildError.NORECIPE)) {
					buildErrors.add(EnumBuildError.NORECIPE);
					isSync = false;
				}
			}else{
				if(buildErrors.contains(EnumBuildError.NORECIPE)) {
					buildErrors.remove(EnumBuildError.NORECIPE);
					isSync = false;
				}
			}
			if(!hasPower()) {
				if(!buildErrors.contains(EnumBuildError.NOPOWER)){
					buildErrors.add(EnumBuildError.NOPOWER);
					isSync = false;
				}
			}else{
				if(buildErrors.contains(EnumBuildError.NOPOWER)){
					buildErrors.remove(EnumBuildError.NOPOWER);
					isSync = false;
				}
			}
			if(!this.hasCraftingSpace()){
				if(!buildErrors.contains(EnumBuildError.NOCRAFTSPACE)) {
					buildErrors.add(EnumBuildError.NOCRAFTSPACE);
					isSync = false;
				}
				if(this.workedTime != 0){
					this.workedTime = 0;
					isSync = false;
				}
			}else{
				if(buildErrors.contains(EnumBuildError.NOCRAFTSPACE)) {
					buildErrors.remove(EnumBuildError.NOCRAFTSPACE);
					isSync = false;
				}
			}
			
			if(this.hasEmptyCan() && idMatchingrecipe != -1 && this.hasPower() && this.isRSPowered() && this.hasCraftingSpace()) {			
				if(buildErrors.contains(EnumBuildError.NOCRAFTSPACE)) {
					buildErrors.remove(EnumBuildError.NOCRAFTSPACE);
					isSync =false;
				}
				ItemStack can = this.getEmptyCan();
				if(!ItemGodCan.hasId(can, idMatchingrecipe)) {
					if(buildErrors.contains(EnumBuildError.SAMEID)) {
						buildErrors.remove(EnumBuildError.SAMEID);
						isSync = false;
					}
					if(workedTime >= BUILDTIME ) 
						if(this.craftCan(can)) this.workedTime = 0;			
						else workedTime = BUILDTIME;
					else{
						if(workedTime == 1) isSync =false;
						workedTime++;
						this.decharge();
					}
				}else{
					if(!buildErrors.contains(EnumBuildError.SAMEID)) {
						buildErrors.add(EnumBuildError.SAMEID);
						isSync = false;
					}
				}
				
			}else if(!this.hasEmptyCan()) {
				if(this.workedTime != 0) {
					this.workedTime = 0; 
					isSync = false;
				}
			}
			else if(hasIngredientsChanged){
				if(this.workedTime != 0) {
					this.workedTime = 0; 
					isSync = false;
				}
			}
		}
	}
	

	private void charge() {
		if(this.power >= this.MAXPOWER ) return;
		else if(this.inventory[0] != null){
			ItemStack crystal = inventory[0];
			if(crystal.getItem() instanceof ItemCrystal){
				int toCharge = MAXCHARGESPEED;
				if(power + MAXCHARGESPEED > MAXPOWER) toCharge = MAXPOWER - power;
				this.power += toCharge - ItemCrystal.dischargeItem(crystal, toCharge);
			}
		}
		
	}
	
	private void decharge() {
		this.power -= POWERUSAGE;
	}
	private void idleDecharge() {
		if(power - 1>=0)
			this.power--;
		else this.power = 0;
	}

	private boolean  craftCan(ItemStack can) {
		if(hasCraftingSpace()) {
			Map<Integer,ItemStack> mapIs = this.getIngredientStacks() ;

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
			isSync = false;
	        return true;
		}else{ 
			return false;
		}
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
		

		DeadCraft.packetPipeline.sendToDimension(new DeadCraftGodBottlerPacket(this), this.worldObj.getWorldInfo().getVanillaDimension());
		isSync = true;
	}
	
	@Override
	public void syncWithplayer(EntityPlayerMP player) {
		super.syncWithplayer(player);
		DeadCraft.packetPipeline.sendTo(new DeadCraftGodBottlerPacket(this), player);
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
			case 0: return var2.getItem() instanceof ItemCrystal;
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
		return (isTop() ? pair.isUseableByPlayer(var1) : super.isUseableByPlayer(var1)) && pair != null;
	}
	
	
	
	
	
	
	public boolean hasCraftingSpace() {
		return this.getStackInSlot(2) == null;
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
	public ArrayList<EnumBuildError> getBuildErrors()  {
		return this.buildErrors;
	}
	
	public int getDirection() {
		if(pair == null) return 0;
		return isTop() ? pair.getDirection() : this.blockMetadata;
	}
	public ItemStack getEmptyCan() {
		return inventory[1];
	}
	private Map<Integer,ItemStack> getIngredientStacks() {
		Map<Integer,ItemStack> is = new HashMap();
		for(int i = 3; i < inventory.length; i++) {
			if(inventory[i] != null && inventory[i].stackSize > 0)
				is.put(i, getStackInSlot(i));
		}
		return is;
	}
	public String getInventoryName() {
		return IBTInfos.TILE_BOTTLER_KEY;
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
			i++;
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
	
	public int getMatchingRecipeId() {
		Map<Integer,ItemStack> mapIs = this.getIngredientStacks();
		ItemStack[] is = this.getItemStackFromMap(mapIs);
		if(is == null) {
			return -1;
		}
		return canCraftingManager.getMatchingRecipes(is);
	}
	
	/**
	 * here are the setters
	 */
	public void setBuildErrors(ArrayList<EnumBuildError> buildErrorsWithIds) {
		this.buildErrors = buildErrorsWithIds;
	}
	
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
			isSync = false;
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
	
	public enum EnumBuildError {
		
		NOPOWER("noPower",0),
		NOREDSTONE("noRedstone",1),
		NOEMMPTYCAN("noEmptyCan",2),
		NORECIPE("noRecipe",3),
		NOCRAFTSPACE("noSpace",4),
		SAMEID("sameId",5);
		
		private String nid="";
		private int id;
		
		
		
		EnumBuildError(String name, int id){
			this.nid = name;
			this.id = id;
		}
		
		public String toString() {
			return nid;
		}
		
		public static EnumBuildError getBuilErrorWithId(int id) {
			for(EnumBuildError erreur : EnumBuildError.values()) {
				if(erreur.id == id) return erreur;
			}
			return null;
		}
		
		public static int[] getIdsWithBuilError(ArrayList<EnumBuildError> errors) {
			if(errors == null) return null;
			int[] re  = new int[errors.size()];
			for (int i = 0 ; i< errors.size(); i++) {
				re[i] = errors.get(i).id;
			}
			return re;
		}
		
		public static ArrayList<EnumBuildError> getBuildErrorsWithIds(int[] errorsId) {
			ArrayList<EnumBuildError> re = new ArrayList<>();
			for(int i=0; i< errorsId.length ; i++) {
				re.add(getBuilErrorWithId(errorsId[i]));
			}
			return re;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		switch(side) {
		case 0 : return slot_bottom;
		case 1 : return slot_top;
		case 2 : side = 2;
			break;
		case 3 : side = 0;
			break;
		case 4 : side = 1;
			break;
		case 5 : side = 3;
			break;
		}
		if(side == blockMetadata)return slot_back;
		return slot_side;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return this.isItemValidForSlot(var1, var2);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack var2, int side) {
		return side == 0 && slot == 2 || (side == 0 && slot == 0 && ItemCrystal.isEmpty(var2));
	}

	@Override
	protected int getMaxChargeSpeed() {
		return MAXCHARGESPEED;
	}

	@Override
	protected int getMaxPower() {
		return MAXPOWER;
	}
	
	@Override
	public List<String> getInfo() {
		ArrayList<String> re = (ArrayList<String>) (this.isTop() ?pair.getInfo() :  super.getInfo());
		if(this.isTop()) re.add(EnumChatFormatting.GRAY +"" +EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.wrench.isTop"));
		if(!this.isTop())re.add(StatCollector.translateToLocal("dc.wrench.redstone")+ " : " + (this.isRSPowered() ? (EnumChatFormatting.GREEN + StatCollector.translateToLocal("dc.true")) : (EnumChatFormatting.RED + StatCollector.translateToLocal("dc.false")) ));
		return re;
	}

	



	
	
}
