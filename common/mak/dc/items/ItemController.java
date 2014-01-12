package mak.dc.items;

//TODO not finsihed don't forget the textures :D

import java.util.List;

import mak.dc.DeadCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemController extends Item {

    public ItemController (int id) {
        super(id);
    }

    public List<String> getAllowedList (ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        List<String> allowed = null;
        if(tag != null){ 
            int nbersAll = tag.getInteger("nbAlllowed");
            NBTTagCompound tagAllowed = tag.getCompoundTag("allowed");
            for (int i = 0; i < nbersAll; i++ ) {

                allowed.set(i, tagAllowed.getString("allowed "  +i));
            }
        }
        return allowed;
    }
    
    @Override
    public void onUpdate (ItemStack is, World world, Entity ent, int par4, boolean par5) {
        super.onUpdate(is, world, ent, par4, par5);
        if(!world.isRemote) {
            if(!is.hasTagCompound()) {
                NBTTagCompound tag = new NBTTagCompound();
                is.setTagCompound(tag);
            }
        }
        
    }

    public byte getMode (ItemStack is) {
        byte re = -1;
        NBTTagCompound tag = is.getTagCompound();
        if (tag != null) re = tag.getByte("mode");
        return re;
    }

    @Override
    public boolean onItemUseFirst (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            if(player.isSneaking()) {
                NBTTagCompound tag = stack.getTagCompound();
                if(tag!= null) {
                    System.out.println(tag);
                    player.addChatMessage(tag.getString("lastEntry")); //TODO check why it doesn't work
                }else {
                    System.out.println("lol");
                }
            }else {
                if(stack.hasTagCompound()) player.openGui(DeadCraft.instance, 1, player.worldObj, x, y, z);
                else {
                    NBTTagCompound tag = new NBTTagCompound();
                    stack.setTagCompound(tag);
                }
            }
            return true;
        }
        return false;

    }
    
    public String addUser (ItemStack is) {
        String re = "";
        NBTTagCompound tag = is.getTagCompound();
        if(tag != null) re = tag.getString("lastEntry");
        return re;
    }
    
    public void onInterfaceEventRecieve() {
        
    }

}
