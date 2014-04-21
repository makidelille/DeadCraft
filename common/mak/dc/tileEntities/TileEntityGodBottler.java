package mak.dc.tileEntities;

import mak.dc.DeadCraft;
import mak.dc.network.DeadCraftGodBottlerPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.obj.Face;

public class TileEntityGodBottler extends TileEntityDeadCraft implements IInventory{
	
	private static final byte deadcraftId = 2;

	public static final int animationTime = 20;

	private TileEntityGodBottler pair;
	
	private int time;
	private int starsInStock;
	public int direction;
	private boolean hasStarted;
	private boolean isTop;
	private ItemStack[] inventory ;

	private int clientTick = 0;

	private boolean isPowered = false;

	private boolean isSync;
	
	public TileEntityGodBottler() {
		this(false);
	}
	
	
	public TileEntityGodBottler(boolean top, byte facing) {
		super(true);
		this.isTop = top;
		this.direction = facing;
	}
	
	public TileEntityGodBottler(boolean top) {
		this(top, (byte) 0);
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
		}
	}
	
	private void sync() {
		pair.allowed = this.allowed;
		pair.owner = this.owner;
		pair.locked = this.locked;
		
		
		isSync = true;
	}


	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return null;
	}



	@Override
	public String getInventoryName() {
		return isTop ? pair.getInventoryName() : "GodBottler";
	}



	@Override
	public int getInventoryStackLimit() {
		return isTop ? pair.getInventoryStackLimit() : 1;
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
		return isTop? pair.getStackInSlotOnClosing(var1) :this.inventory[var1];
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if (isTop) pair.setInventorySlotContents(var1, var2);
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
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);	
		this.isTop = nbtTagCompound.getBoolean("top");
		this.direction = nbtTagCompound.getInteger("direction");
		if(this.isTop) readPairData(nbtTagCompound);
		DeadCraft.packetPipeline.sendToDimension(new DeadCraftGodBottlerPacket(this), worldObj.getWorldInfo().getVanillaDimension());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("direction", this.direction);
		nbtTagCompound.setBoolean("top", this.isTop);
		if(this.isTop) writePairData(pair, nbtTagCompound);
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

	}
	
	public void clientSetup(TileEntityGodBottler te) {
		this.direction = te.getDirection();
		this.clientTick = (int) animationTime;
		this.setTop();
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
	
	

	public boolean isPowered() {
		return this.isPowered;
	}
	public void setPowered(boolean isPowered2) {
		this.isPowered = isPowered2;		
	}


	public TileEntityGodBottler getPair() {
		return this.pair;
	}


	public boolean isHasStarted() {
		return this.hasStarted;
	}




	
	
}
