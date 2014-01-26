package mak.dc.tileEntities;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDeadCraft extends TileEntity {


    protected String owner;
    protected ArrayList allowed = new ArrayList(); 
    protected boolean locked;
    protected boolean needManager = true;


    public TileEntityDeadCraft() {
        this.locked = false;
        this.owner = "null";

    }

    public void setOwner(String username) {
        this.owner = username;
        System.out.println(this.owner);
    }

    public String getowner() {
        return this.owner;
    }

    public void addAllowedUser (String name) {
        allowed.add(name);
    }

    public void removeAllowedUser (String name) {
        allowed.remove(name);
    }

    public void setAllowedUser (ArrayList users) {
        allowed = users;
    }


    public ArrayList getAllowedUser () {
        return allowed;
    }


    public boolean isUserAllowed(String name) {
        if(!needManager) return false;
        if(!this.locked || isUserCreator(name)) return true;
        else if(this.isLocked()) return false;
        else return isUserAllowed(name);
    }


    public boolean isUserCreator(String name) {
        return name.equalsIgnoreCase(owner);
    }

    public void invertlock() {
        if(!needManager) return ;
        setLocked(!locked);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean par) {
        if(!needManager) return ;
        System.out.println(par);
        this.locked = par;
    }
    
    public void setUnManagable() {
        this.needManager = false;
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
