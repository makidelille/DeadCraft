package mak.dc.common.items;

import java.util.List;

import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemCompacted extends Item {
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add("item : " + getItem(stack).getUnlocalizedName());
        list.add("size : " + getSize(stack));
        list.add("dmg : " + getDmg(stack));
        if(getTag(stack) != null) list.add("has custom data");
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(Textures.COMPACTED_TEXT_LOC);
    }
    
    public static Item getItem(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return null;
        int id = tag.getInteger("stackId");
        if (id == Item.getIdFromItem(DeadCraftItems.compacted)) return null;
        return Item.getItemById(id);
    }
    
    public static int getSize(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return 0;
        ItemStack stack = new ItemStack(getItem(is));
        return tag.getInteger("stackSize");
    }
    
    public static int getDmg(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return 0;
        return tag.getInteger("stackDamage");
    }
    
    public static NBTTagCompound getTag(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return null;
        return (NBTTagCompound) tag.getTag("stackTag");
        
    }
    
    public static ItemStack compactStackInto(ItemStack stack) {
        if(stack == null) return null;
        ItemStack result = new ItemStack(DeadCraftItems.compacted);
        NBTTagCompound tag = new NBTTagCompound();
        if (stack.getItem() instanceof ItemCompacted && stack.getTagCompound() != null) {
            int size = stack.getTagCompound().getInteger("stackSize");
            size *= stack.stackSize;
            tag.setInteger("stackSize", size);
            tag.setInteger("stackId", stack.getTagCompound().getInteger("stackId"));
            tag.setInteger("stackDamage", stack.getTagCompound().getInteger("stackDamage"));
            if(tag.hasKey("stackTag")) tag.setTag("stackTag", stack.getTagCompound().getTag("stackTag"));
        } else {
            tag.setInteger("stackId", Item.getIdFromItem(stack.getItem()));
            tag.setInteger("stackSize", stack.stackSize);
            tag.setInteger("stackDamage", stack.getItemDamage());
            if(stack.hasTagCompound()) tag.setTag("stackTag", stack.getTagCompound());
        }
        result.setTagCompound(tag);
        return result;
        
    }
    /**
     * @param compressed stack
     * @return array of stack, first is the uncompressed, second the left overs
     */
    public static ItemStack[] uncompactStack(ItemStack stack) {
        if(!(stack.getItem() instanceof ItemCompacted)) return null;
        Item it = getItem(stack);
        if (it == null || stack.getTagCompound() == null) return null;
        ItemStack[] re = new ItemStack[2];
        int size = getMaxSize(stack);
        NBTTagCompound tag = stack.getTagCompound();
        ItemStack temp = new ItemStack(it, size, getDmg(stack));
        if(getTag(stack) != null) temp.setTagCompound(getTag(stack));
        re[0] = temp;
        int newSize = getSize(stack) - size;
        tag.setInteger("stackSize", newSize);
        stack.setTagCompound(tag);
        re[1] = stack;
        if(newSize <= 0) re[1] = null;
        return re;
    }

    private static int getMaxSize(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return 0;
        ItemStack stack = new ItemStack(getItem(is));
        return tag.getInteger("stackSize") > stack.getMaxStackSize() ? stack.getMaxStackSize() : tag.getInteger("stackSize");
    }
}
