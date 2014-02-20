package mak.dc.tileEntities;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDeadCraft extends TileEntity {


    protected String owner;
    protected ArrayList allowed = new ArrayList(); 
    protected boolean locked;
    protected boolean isManagable = true;


    public TileEntityDeadCraft() {
        this.locked = false;
        this.owner = "null";

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
        allowed = users;
    }


    public ArrayList getAllowedUser () {
        return allowed;
    }


    public boolean isUserAllowed(String name) {
        if(!isManagable) return false;
        else if(!this.locked || isUserCreator(name)) return true;
        else if(allowed.contains(name.toLowerCase())) return true;
        else if(this.isLocked()) return false;
        else return false;
        
    }
    
    public boolean isUserCreator(String name) {
        return name.equalsIgnoreCase(owner);
    }

    public void invertlock() {
        if(!isManagable) return ;
        setLocked(!locked);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean par) {
        if(!isManagable) return ;
        this.locked = par;
    }
    
    public void setUnManagable() {
        this.isManagable = false;
    }



    @Override
    public void writeToNBT (NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        
        nbtTagCompound.setString("owner", this.owner);
                if(allowed.size() != 0) {        
                    int nbersAll = allowed.size();
                    NBTTagCompound tagAllowed = new NBTTagCompound();
                    for (int i = 0; i < nbersAll; i++ ) {
                        tagAllowed.setString("allowed " +i , (String) allowed.get(i));
                    }
                    tagAllowed.setInteger("nbAlllowed", nbersAll);
                    nbtTagCompound.setTag("allowed", tagAllowed);
                }
        nbtTagCompound.setBoolean("locked", locked);
    }

    @Override
    public void readFromNBT (NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        
        this.owner = nbtTagCompound.getString("owner");
                int nbersAll = nbtTagCompound.getInteger("nbAlllowed");
                NBTTagCompound tagAllowed = nbtTagCompound.getCompoundTag("allowed");
                for (int i = 0; i < nbersAll; i++ ) {
                    allowed.add(tagAllowed.getString("allowed "  +i));
                }
        this.locked = nbtTagCompound.getBoolean("locked");
    }






}
