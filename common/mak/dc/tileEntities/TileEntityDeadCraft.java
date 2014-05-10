package mak.dc.tileEntities;

import java.util.ArrayList;

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
        this.locked = false;
        this.owner = "null";
        this.allowed = new ArrayList();
        

    }

    public void setOwner(String username) {
        this.owner = username;
    }

    public String getowner() {
        return this.owner;
    }

    public void addAllowedUser (String name) {
        if(!allowed.contains(name.toLowerCase()))
            allowed.add(name.toLowerCase());
    }

    public void removeAllowedUser (String name) {
        if(allowed.contains(name.toLowerCase()))
            allowed.remove(name.toLowerCase());
    }

    public void setAllowedUser (ArrayList users) {
    	if(users != null) allowed = users;
    	else allowed = new ArrayList<String>();
    }


    public ArrayList getAllowedUser () {
        return allowed;
    }


    public boolean isUserAllowed(String name) {
        if(!this.locked || isUserCreator(name)) return true;
        else if(allowed.contains(name.toLowerCase())) return true;
        else if(this.isLocked()) return false;
        else return false;
        
    }
    
    public boolean isUseableByPlayer(EntityPlayer player) {
    	return isUserAllowed(player.getCommandSenderName()) && player.getDistanceSq(xCoord, yCoord, zCoord) <= 36;
    }
    
    public boolean isUserCreator(String name) {
        return name.equalsIgnoreCase(owner);
    }

    public void invertlock() {
        setLocked(!locked);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean par) {
        this.locked = par;
    }
    
     @Override
    public void writeToNBT (NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
                
        nbtTagCompound.setString("owner", this.owner);
        int nbersAll = allowed.size();
                if(allowed.size() != 0) {       
                    for (int i = 0; i < nbersAll; i++ ) 
                        nbtTagCompound.setString("allowed [" +i+ "]" , allowed.get(i).toString());
                    nbtTagCompound.setInteger("nbAllowed", nbersAll);
                }
        nbtTagCompound.setBoolean("locked", locked);
        
    }

    @Override
    public void readFromNBT (NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        
        this.owner = nbtTagCompound.getString("owner");
        int nbersAll = nbtTagCompound.getInteger("nbAllowed");
        for (int i = 0; i < nbersAll; i++ ) {
            allowed.add(nbtTagCompound.getString("allowed [" +i+ "]"));
        }
        this.locked = nbtTagCompound.getBoolean("locked");
        
    }

	
	public byte getID() {
		return this.deadcraftId;
	}

	public NBTTagCompound writeNBTData() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		nbtTagCompound.setString("owner", this.owner);
        int nbersAll = allowed.isEmpty() ?  0 : allowed.size();
                if(allowed.size() > 0) {       
                    for (int i = 0; i < nbersAll; i++ ) 
                        nbtTagCompound.setString("allowed [" +i+ "]" , allowed.get(i).toString());
                    nbtTagCompound.setInteger("nbAllowed", nbersAll);
                }
        nbtTagCompound.setBoolean("locked", locked);
		return nbtTagCompound;
	}

	public void readNBTData(NBTTagCompound tagCompound) {
		this.owner = tagCompound.getString("owner");
        int nbersAll = tagCompound.getInteger("nbAllowed");
        for (int i = 0; i < nbersAll; i++ ) {
            allowed.add(tagCompound.getString("allowed [" +i+ "]"));
        }
        this.locked = tagCompound.getBoolean("locked");		
	}

	public  boolean hasInventory() {
		return this instanceof IInventory;
	}






}
