package mak.dc.common.tileEntities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDeadCraft extends TileEntity {
    
    private static final byte deadcraftId = 0;
    
    protected String owner;
    protected ArrayList<String> allowed;
    protected boolean locked;
    
    public TileEntityDeadCraft(boolean isManagable) {
        locked = false;
        owner = "null";
        allowed = new ArrayList();
    }
    
    public void addAllowedUser(String name) {
        if (!allowed.contains(name.toLowerCase())) {
            allowed.add(name.toLowerCase());
        }
    }
    
    public ArrayList getAllowedUser() {
        return allowed;
    }
    
    public byte getID() {
        return deadcraftId;
    }
    
    public List<String> getInfo() {
        return null;
    }
    
    public String getowner() {
        return owner;
    }
    
    public boolean hasInventory() {
        return this instanceof IInventory;
    }
    
    public void invertlock() {
        setLocked(!locked);
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public boolean isUseableByPlayer(EntityPlayer player) {
        return isUserAllowed(player.getCommandSenderName()) && player.getDistanceSq(xCoord, yCoord, zCoord) <= 36;
    }
    
    public boolean isUserAllowed(String name) {
        if (!locked || isUserCreator(name)) return true;
        else if (allowed.contains(name.toLowerCase())) return true;
        else if (isLocked()) return false;
        else return false;
        
    }
    
    public boolean isUserCreator(String name) {
        return name.equalsIgnoreCase(owner);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        
        owner = nbtTagCompound.getString("owner");
        int nbersAll = nbtTagCompound.getInteger("nbAllowed");
        for (int i = 0; i < nbersAll; i++) {
            allowed.add(nbtTagCompound.getString("allowed [" + i + "]"));
        }
        locked = nbtTagCompound.getBoolean("locked");
        
    }
    
    public void readNBTData(NBTTagCompound tagCompound) {
        owner = tagCompound.getString("owner");
        int nbersAll = tagCompound.getInteger("nbAllowed");
        for (int i = 0; i < nbersAll; i++) {
            allowed.add(tagCompound.getString("allowed [" + i + "]"));
        }
        locked = tagCompound.getBoolean("locked");
    }
    
    public void removeAllowedUser(String name) {
        if (allowed.contains(name.toLowerCase())) {
            allowed.remove(name.toLowerCase());
        }
    }
    
    public void setAllowedUser(ArrayList users) {
        if (users != null) {
            allowed = users;
        } else {
            allowed = new ArrayList<String>();
        }
    }
    
    public void setLocked(boolean par) {
        locked = par;
    }
    
    public void setOwner(String username) {
        owner = username;
    }
    
    public void syncWithplayer(EntityPlayer player) {
    }
    
    public NBTTagCompound writeNBTData() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("owner", owner);
        int nbersAll = allowed.isEmpty() ? 0 : allowed.size();
        if (allowed.size() > 0) {
            for (int i = 0; i < nbersAll; i++) {
                nbtTagCompound.setString("allowed [" + i + "]", allowed.get(i).toString());
            }
            nbtTagCompound.setInteger("nbAllowed", nbersAll);
        }
        nbtTagCompound.setBoolean("locked", locked);
        return nbtTagCompound;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        
        nbtTagCompound.setString("owner", owner);
        int nbersAll = allowed.size();
        if (allowed.size() != 0) {
            for (int i = 0; i < nbersAll; i++) {
                nbtTagCompound.setString("allowed [" + i + "]", allowed.get(i).toString());
            }
            nbtTagCompound.setInteger("nbAllowed", nbersAll);
        }
        nbtTagCompound.setBoolean("locked", locked);
        
    }
    
}
