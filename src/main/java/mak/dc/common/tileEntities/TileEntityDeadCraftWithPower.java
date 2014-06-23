package mak.dc.common.tileEntities;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.common.util.IPowerReceiver;
import mak.dc.common.util.IPowerSender;
import mak.dc.network.pipeline.packets.DeadCraftPowerSourcesPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public abstract class TileEntityDeadCraftWithPower extends TileEntityDeadCraft implements IPowerReceiver {
    
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
    public void askPower(int amount) {
        if (powerSources != null && !powerSources.isEmpty()) {
            for (IPowerSender powerSource : powerSources) {
                if (powerSources == null) {
                    continue;
                }
                powerSource.onAskedPower(this, amount);
            }
        }
        
    }
    
    private void checkSources() {
        for (int i = 0; i < powerSources.size(); i++) {
            TileEntity source = (TileEntity) powerSources.get(i);
            if (source == null) {
                powerSources.remove(i);
                isSync = false;
                continue;
            }
            TileEntity te = worldObj.getTileEntity(source.xCoord, source.yCoord, source.zCoord);
            if (source != te) {
                powerSources.remove(i);
                isSync = false;
                continue;
            }
            
        }
        sourceChanged = false;
        
    }
    
    @Override
    public List<String> getInfo() {
        ArrayList<String> re = new ArrayList<String>();
        re.add(StatCollector.translateToLocal("dc.power") + " : " + power + "/" + getMaxPower());
        if (powerSources != null && !powerSources.isEmpty()) {
            re.add(StatCollector.translateToLocal("dc.block.power.info.connectionNb") + " : " + powerSources.size());
        }
        return re;
    }
    
    public abstract int getMaxChargeSpeed();
    
    public abstract int getMaxPower();
    
    public int getCharge(){
        return this.power;
    }
    
    @Override
    public List<IPowerSender> getPowerSource() {
        return powerSources;
        
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        power = nbtTagCompound.getInteger("power");
        coords = (NBTTagCompound) nbtTagCompound.getTag("coord");
    }
    
    @Override
    public void recievePower(int amount) {
        if (power + amount <= getMaxPower()) {
            power += amount;
            hasReceive = true;
        }
        if (amount == 0) {
            hasReceive = false;
        }
        
    }
    
    @Override
    public void resetPowerSource() {
        powerSources = new ArrayList<IPowerSender>();
    }
    
    @Override
    public void setPowerSource(int[] blockCoord) {
        int x = blockCoord[0];
        int y = blockCoord[1];
        int z = blockCoord[2];
        
        TileEntity te = worldObj.getTileEntity(x, y, z);
        if (te == null) return;
        if (te instanceof IPowerSender && !powerSources.contains(te)) {
            powerSources.add((IPowerSender) te);
        }
        isSync = false;
        
    }
    
    @Override
    public void setSourceChange() {
        sourceChanged = true;
    }
    
    public void setCharge(int charge) {
        this.power = charge;
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
    
    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (sourceChanged) {
                checkSources();
            }
            if (!isSync) {
                sync();
            }
            if (power <= getMaxPower()) {
                int dif = getMaxPower() - power <= getMaxChargeSpeed() ? getMaxPower() - power : getMaxChargeSpeed();
                askPower(dif);
            }
            if (coords != null && (powerSources == null || powerSources.isEmpty())) {
                int size = coords.getInteger("size");
                for (int i = 0; i < size; i++) {
                    int[] coord = coords.getIntArray("" + i);
                    setPowerSource(coord);
                }
                coords = null;
            }
            hasReceive = false;
        }
        
    }
    
    @Override
    public void updatePowerSource(int[] blockCoord) {
        int x = blockCoord[0];
        int y = blockCoord[1];
        int z = blockCoord[2];
        
        TileEntity te = worldObj.getTileEntity(x, y, z);
        if (te == null || !(te instanceof IPowerSender)) return;
        if (powerSources.contains(te)) {
            powerSources.remove(te);
        } else {
            powerSources.add((IPowerSender) te);
        }
        isSync = false;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("power", power);
        if (!powerSources.isEmpty()) {
            NBTTagCompound coords = new NBTTagCompound();
            coords.setInteger("size", powerSources.size());
            for (int i = 0; i < powerSources.size(); i++) {
                IPowerSender powerSource = powerSources.get(i);
                int[] coord = { ((TileEntity) powerSource).xCoord, ((TileEntity) powerSource).yCoord, ((TileEntity) powerSource).zCoord };
                coords.setIntArray("" + i, coord);
            }
            nbtTagCompound.setTag("coord", coords);
        }
    }
    
}
