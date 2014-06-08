package mak.dc.tileEntities;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.network.packet.DeadCraftPowerSourcesPacket;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public abstract class TileEntityDeadCraftWithPower extends TileEntityDeadCraft implements IPowerReceiver{

	protected ArrayList<IPowerSender> powerSources;
	protected int power;
	protected boolean hasReceive;
	private NBTTagCompound coords;
	private boolean isSync;
	private boolean sourceChanged;




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
			if(sourceChanged) checkSources();
			if(!isSync) sync();
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
			this.hasReceive = false;
		}

	}

	public void setPowerSource(int[] blockCoord) {
		int x = blockCoord[0];
		int y = blockCoord[1];
		int z = blockCoord[2];
		
		TileEntity te = worldObj.getTileEntity(x, y, z);		
		if(te == null) return;
		if(te instanceof IPowerSender && !this.powerSources.contains(te)) this.powerSources.add((IPowerSender) te);
		isSync = false;
		
	}


	private void checkSources() {
		for(int i = 0; i< this.powerSources.size(); i++) {
			TileEntity source  = (TileEntity) this.powerSources.get(i);
			if(source == null) {
				this.powerSources.remove(i);
				isSync= false;
				continue;
			}
			TileEntity te = worldObj.getTileEntity(source.xCoord, source.yCoord, source.zCoord);
			if(source != te) {
				this.powerSources.remove(i);
				isSync= false;
				continue;
			}
			
		}
		sourceChanged = false;
		
	}


	private void sync() {
		DeadCraft.packetPipeline.sendToDimension(new DeadCraftPowerSourcesPacket(this), worldObj.getWorldInfo().getVanillaDimension());
		isSync = true;
	}
	
	@Override
	public void syncWithplayer(EntityPlayer player) {
		DeadCraft.packetPipeline.sendTo(new DeadCraftPowerSourcesPacket(this), (EntityPlayerMP) player);
		super.syncWithplayer(player);
	}


	protected abstract int getMaxChargeSpeed();
	protected abstract int getMaxPower();

	@Override
	public void resetPowerSource() {
		this.powerSources = new ArrayList<IPowerSender>();		
	}
	
	@Override
	public void updatePowerSource(int[] blockCoord) {
		int x = blockCoord[0];
		int y = blockCoord[1];
		int z = blockCoord[2];
		
		TileEntity te = worldObj.getTileEntity(x, y, z);		
		if(te == null || !(te instanceof IPowerSender)) return;
		if(powerSources.contains(te)) powerSources.remove(te);
		else powerSources.add((IPowerSender) te);
		isSync = false;
	}
	
	@Override
	public List<IPowerSender> getPowerSource() {
		return this.powerSources;
		
	}

	@Override
	public void recievePower(int amount) {
		if(power + amount <= getMaxPower()) {
			this.power += amount;
			this.hasReceive = true;
		}
		if(amount == 0) this.hasReceive = false;		
		
	}

	@Override
	public void askPower(int amount) {
		if(powerSources != null && !powerSources.isEmpty() ) {
			for(IPowerSender powerSource : powerSources) {
				if(this.powerSources == null) continue;
				powerSource.onAskedPower(this, amount);
			}
		}
		
	}
	
	
	@Override
	public List<String> getInfo() {
		ArrayList<String> re = new ArrayList<>();
		re.add(StatCollector.translateToLocal("dc.power")+ " : " + this.power + "/" +  this.getMaxPower());
		if(powerSources != null && !powerSources.isEmpty()) {
			re.add(StatCollector.translateToLocal("dc.block.power.info.connectionNb") + " : " + powerSources.size());
		}
		return re;
	}
	
	@Override
	public void setSourceChange() {
		this.sourceChanged  = true;
	}

}
