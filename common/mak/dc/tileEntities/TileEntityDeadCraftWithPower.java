package mak.dc.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;

public abstract class TileEntityDeadCraftWithPower extends TileEntityDeadCraft implements IPowerReceiver{

	protected IPowerSender powerSource;
	protected int power;
	protected boolean hasReceive;




	public TileEntityDeadCraftWithPower(boolean isManagable) {
		super(isManagable);
	}

	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.power = nbtTagCompound.getInteger("power");
		NBTTagCompound coord = (NBTTagCompound) nbtTagCompound.getTag("coord");
		if(coord != null) {
			int[] coords = {coord.getInteger("x"),coord.getInteger("y"),coord.getInteger("z")};
			this.setPowerSource(coords);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("power", power);
		if(powerSource != null) {
			NBTTagCompound coord = new NBTTagCompound();
			coord.setInteger("x", ((TileEntity)powerSource).xCoord);
			coord.setInteger("y", ((TileEntity)powerSource).yCoord);
			coord.setInteger("z", ((TileEntity)powerSource).zCoord);
			nbtTagCompound.setTag("coord", coord);
		}
	}


	@Override
	public void updateEntity() {
		if(power < this.getMaxPower()) {
			int dif = this.getMaxPower() - power <= this.getMaxChargeSpeed() ? this.getMaxPower() - power : getMaxChargeSpeed();
			this.askPower(dif);				
		}
		//TODO same bug of paring as always :d

	}

	protected abstract int getMaxChargeSpeed();
	protected abstract int getMaxPower();



	public void setPowerSource(int[] blockCoord) {
		int x = blockCoord[0];
		int y = blockCoord[1];
		int z = blockCoord[2];
		
		
		TileEntity te = worldObj.getTileEntity(x, y, z);
		if(te == null) return;
		if(te instanceof IPowerSender) this.powerSource = (IPowerSender) te;
				
	}




	@Override
	public void recievePower(int amount) {
		if(power + amount <= getMaxPower()) {
			this.power += amount;
			this.hasReceive = true;
		}
		
	}




	@Override
	public void askPower(int amount) {
		if(this.powerSource == null) return;
		this.powerSource.onAskedPower(this, amount);
		
	}

}
