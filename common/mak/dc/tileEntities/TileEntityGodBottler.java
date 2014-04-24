package mak.dc.tileEntities;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.items.crafting.CanCraftingManager;
import mak.dc.network.DeadCraftGodBottlerPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityGodBottler extends TileEntityDeadCraft implements IInventory{
	
	private static final byte deadcraftId = 2;
	private static final CanCraftingManager canRecipesManager = CanCraftingManager.getInstance();
	
	
	
	/**Aniamtion speed (client Side)
	 */
	public static final int animationTime = 20;
	
	/**time to build (Server Side)
	 */
	public static final int buildTime = 100;
	
	

	private TileEntityGodBottler pair;
	private int pairX,pairY,pairZ;
	
	public int direction;
	private boolean isTop;
	

	private int clientTick = 0;
	private boolean isSync = false;
	
	private int workedTime;
	private int powerAmount;
	private boolean isPowered = false;

	/**Slot 0 : Energy; 
	 * Slot 1 : input can;
	 * Slot 2 : output can;
	 * Slot 3,8 : input resources;
	 */
	private ItemStack[] inventory ;
	/**sub construct
	 * return this(false) = this(false,0)
	 */
	public TileEntityGodBottler() {
		this(false);
	}
	
	/**sub construct
	 * @param isTop ?
	 * @return this(top,0)
	 */
	public TileEntityGodBottler(boolean top) {
		this(top, (byte) 0);
	}
	
	/**Main constructor 
	 * @param top
	 * @param facing
	 */
	public TileEntityGodBottler(boolean top, byte facing) {
		super(true);
		this.isTop = top;
		this.direction = facing;
		inventory = new ItemStack[9];
		this.clientTick = 0;
		this.workedTime = 0;
	}
	
	
	public boolean isTop() {
		return this.isTop;
	}
	
	public void setTop() {
		this.isTop = true;
	}
	
	public void setPair(TileEntityGodBottler te) {
		this.pair = te;
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}	
	
	
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
			if(this.isPowered ) this.setClientTick(this.getClientTick() + 1);
			else if(!this.isPowered) this.setClientTick(this.getClientTick() - 1);
		}
		if(!worldObj.isRemote) {
			if(!isSync) sync();
			ItemStack[] ing = this.getIngredientStacks() ;
			int idMatchingrecipe = canRecipesManager.getMatchingRecipes(ing);
			if(canRecipesManager.getCraftedEffects(idMatchingrecipe) != null) 
				System.out.println(canRecipesManager.getCraftedEffects(idMatchingrecipe).getEffectId());
		}
		workedTime++;
		if(workedTime > buildTime ) workedTime = 0;
	}
	
	private ItemStack[] getIngredientStacks() {
		List<ItemStack> is = new ArrayList();
		for(int i = 2; i < inventory.length; i++) {
			if(inventory[i] != null && inventory[i].stackSize > 0)
				is.add(inventory[i]);
		}
	//	System.out.println(is);
		ItemStack[] re = new ItemStack[is.size()];
		for(int j=0; j < re.length; j++)
			re[j] = (ItemStack) is.get(j);
		return re;
	}

	private void sync() {
		if(pair == null) {
			pair = (TileEntityGodBottler) worldObj.getTileEntity(pairX, pairY, pairZ);
			return;
		}if(this.isTop()) clientSetup(pair);
		pair.allowed = this.allowed;
		pair.owner = this.owner;
		pair.locked = this.locked;
		
		DeadCraft.packetPipeline.sendToDimension(new DeadCraftGodBottlerPacket(this), worldObj.getWorldInfo().getVanillaDimension());
		isSync = true;
	}


	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if(this.isTop()) return pair.decrStackSize(var1, var2);
		return null;
	}



	@Override
	public String getInventoryName() {
		return isTop ? pair.getInventoryName() : "GodBottler";
	}



	@Override
	public int getInventoryStackLimit() {
		return isTop ? pair.getInventoryStackLimit() : 64;
	}



	@Override
	public int getSizeInventory() {
		return isTop ? pair.getSizeInventory() : inventory.length;
	}



	@Override
	public ItemStack getStackInSlot(int var1) {
		return isTop ? pair.getStackInSlot(var1) : inventory[var1];
	}



	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return isTop? pair.getStackInSlotOnClosing(var1) : this.inventory[var1];
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if (isTop) {
			pair.setInventorySlotContents(var1, var2);
			return;
		}else{
			inventory[var1] = var2;
			if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
		            var2.stackSize = getInventoryStackLimit();
		}
		
		
	}



	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}



	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		if (isTop) return pair.isItemValidForSlot(var1, var2);
		return true;
	}



	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		if (isTop) return pair.isUseableByPlayer(var1);
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) { //TODO save inv
		super.readFromNBT(nbtTagCompound);	
		this.isTop = nbtTagCompound.getBoolean("top");
		this.direction = nbtTagCompound.getInteger("direction");
		this.clientTick = nbtTagCompound.getInteger("cTick");
		if(pair == null) {
			pairX = nbtTagCompound.getInteger("pairX");
			pairY = nbtTagCompound.getInteger("pairY");
			pairZ = nbtTagCompound.getInteger("pairZ");
		}
		if(this.isTop) readPairData(nbtTagCompound);
		else {
			NBTTagList items = nbtTagCompound.getTagList("Items", 0);
			
			for (int i = 0; i < items.tagCount(); i++) {
				NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
				int slot = item.getByte("Slot");
				
				if (slot >= 0 && slot < getSizeInventory()) {
					setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) { //TODO save inv
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("direction", this.direction);
		nbtTagCompound.setBoolean("top", this.isTop);
		nbtTagCompound.setInteger("cTick", clientTick);
		if(pair != null){
			nbtTagCompound.setInteger("pairX", pair.xCoord);
			nbtTagCompound.setInteger("pairY", pair.yCoord);
			nbtTagCompound.setInteger("pairZ", pair.zCoord);
		}
		if(this.isTop) 	writePairData(pair, nbtTagCompound);
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
		}
		
	}




	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int i) {
		this.direction = i;
	}

	public int getClientTick() {
		return clientTick;
	}

	public void setClientTick(int tick) {
		if(tick <= animationTime && tick >= 0) this.clientTick = tick;
		else if(tick > animationTime) this.clientTick = animationTime;
		else if(tick < 0) this.clientTick = 0;
	}
	
	public void clientSetup(TileEntityGodBottler te) {
		this.direction = te.getDirection();
		this.clientTick = 0;
		this.setTop();
		//updating client (in theory)
		DeadCraft.packetPipeline.sendToDimension(new DeadCraftGodBottlerPacket(te), worldObj.getWorldInfo().getVanillaDimension());
	}

	public void setup(TileEntityGodBottler te) {
		this.pair = te;		
		this.allowed = te.allowed;
		this.owner = te.owner;
		this.locked = te.locked;
	}
	
	private void readPairData(NBTTagCompound tag) {
		this.owner = tag.getString("owner");
        int nbersAll = tag.getInteger("nbAllowed");
        for (int i = 0; i < nbersAll; i++ ) {
            allowed.add(tag.getString("allowed [" +i+ "]"));
        }
        this.locked = tag.getBoolean("locked");
        
		NBTTagList items = tag.getTagList("Items", 0);
		
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
			int slot = item.getByte("Slot");
			
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}

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
	
	
	public boolean isPowered() {
		return this.isPowered;
	}
	public void setPowered(boolean isPowered2) {
		this.isPowered = isPowered2;		
	}

	public TileEntityGodBottler getPair() {
		return this.pair;
	}

	
	public boolean hasStarted() {
		return this.workedTime > 0;
	}


	public int getWorkedTime() {
		return this.workedTime;
	}

	
	public boolean hasEmptyCan() {
		return this.inventory[1] != null && this.inventory[1].stackSize > 0;
	}
	
	public boolean hasRessources() {
		for(int i = 2; i < inventory.length; i++) {
			if(inventory[i] != null && inventory[i].stackSize > 0)
				return true;
		}
		return false;
	}
	
	
	/** for display only (client Side)
	 * @return should render the can or not
	 */
	@SideOnly(Side.CLIENT)
	public boolean CLIENThasCan() {
		return this.inventory[1] != null || this.inventory[2] != null;
	}




	
	
}
