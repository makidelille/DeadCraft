package mak.dc.common.entity;

import mak.dc.common.blocks.BlockDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityItemDeadCraft extends EntityItem {
    
    private NBTTagCompound data;
    private BlockDeadCraft block;
    private ItemStack is;
    
    public EntityItemDeadCraft(World world, int x, int y, int z, ItemStack is) {
        super(world, x, y, z, is);
        this.is = is;
        block = (BlockDeadCraft) world.getBlock(x, y, z);
        setData();
        
    }
    
    private ItemStack getItemStack() {
        ItemStack is = new ItemStack(Block.getBlockById(data.getInteger("block id")), data.getInteger("size"));
        is.setTagCompound(getTagForItemStack());
        return is;
    }
    
    private NBTTagCompound getTagForItemStack() {
        NBTTagCompound newTag = data;
        newTag.removeTag("block id");
        newTag.removeTag("size");
        return null;
    }
    
    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        super.onCollideWithPlayer(player);
        if (!player.worldObj.isRemote) {
            InventoryPlayer invP = player.inventory;
            int slot = invP.getFirstEmptyStack();
            if (invP.isItemValidForSlot(slot, is)) {
                invP.setInventorySlotContents(slot, is);
            } else return;
        }
    }
    
    public void setData() {
        data = is.getTagCompound();
        data.setInteger("block id", Block.getIdFromBlock(block));
        data.setInteger("size", is.stackSize);
    }
    
}
