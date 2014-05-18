package mak.dc.tileEntities;

import java.util.ArrayList;
import java.util.List;

import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public abstract class TileEntityDeadCraftWithPower extends TileEntityDeadCraft implements IPowerReceiver{

	protected ArrayList<IPowerSender> powerSources;
	protected int power;
	protected boolean hasReceive;
	private NBTTagCompound coords;




	public TileEntityDeadCraftWithPower() {
		super(true);
		powerSources = new ArrayList<IPowerSender>();
	}

	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.power = nbtTagCompound.getInteger("power");
		this.coords = (NBTTagCompound) nbtTagCompound.getTag("coord");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("power", power);
		if(!powerSources.isEmpty()) {
			NBTTagCompound coords = new NBTTagCompound();
			coords.setInteger("size", powerSources.size());
			for(int i = 0; i< powerSources.size(); i++) {
				IPowerSender powerSource = powerSources.get(i);
				int[] coord = {((TileEntity)powerSource).xCoord,((TileEntity)powerSource).yCoord,((TileEntity)powerSource).zCoord};
				coords.setIntArray("" +  i, coord);
			}
			nbtTagCompound.setTag("coord", coords);
		}
	}


	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(power <= this.getMaxPower()) {
				int dif = this.getMaxPower() - power <= this.getMaxChargeSpeed() ? this.getMaxPower() - power : getMaxChargeSpeed();
				this.askPower(dif);				
			}if(coords != null && (this.powerSources == null || this.powerSources.isEmpty()) ) {
					int size = coords.getInteger("size");
					for(int i = 0; i< size; i++) {
						int[] coord = coords.getIntArray("" + i);
						setPowerSource(coord);
					}
					coords = null;
			}
			
		}

	}

	protected abstract int getMaxChargeSpeed();
	protected abstract int getMaxPower();



	public void setPowerSource(int[] blockCoord) {
		int x = blockCoord[0];
		int y = blockCoord[1];
		int z = blockCoord[2];
		
		
		TileEntity te = worldObj.getTileEntity(x, y, z);
		if(te == null) return;
		if(te instanceof IPowerSender) this.powerSources.add((IPowerSender) te);
				
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
		if(powerSources != null && !powerSources.isEmpty() ) {
			int size = powerSources.size();
			for(int i=0; i< powerSources.size(); i++) {
				IPowerSender powerSource = powerSources.get(i);
				if(this.powerSources == null) continue;
				powerSource.onAskedPower(this, amount/size);
			}
		}
		
	}
	
	
	@Override
	public List<String> getInfo() {
		ArrayList<String> re = new ArrayList<>();
		re.add(StatCollector.translateToLocal("dc.power")+ " : " + this.power + "/" +  this.getMaxPower());
		if(powerSources != null && !powerSources.isEmpty()) {
			re.add("conection number" + " : " + powerSources.size());
		}
		return re;
	}

}
