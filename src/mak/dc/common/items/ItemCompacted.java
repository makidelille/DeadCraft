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
        if (getTag(stack) != null) list.add("has custom data");
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(Textures.COMPACTED_TEXT_LOC);
    }
    
    public static Item getItem(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return null;
        int id = tag.getInteger("stackId");
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
        if (stack == null) return null;
        ItemStack result = new ItemStack(DeadCraftItems.compacted);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("stackId", Item.getIdFromItem(stack.getItem()));
        tag.setInteger("stackSize", stack.stackSize);
        tag.setInteger("stackDamage", stack.getItemDamage());
        if (stack.hasTagCompound()) tag.setTag("stackTag", stack.getTagCompound());
        result.setTagCompound(tag);
        return result;
        
    }
    
    /**
     * @param compressed stack
     * @return array of stack, first is the uncompressed, second the left overs
     */
    public static ItemStack uncompactStack(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemCompacted)) return null;
        Item it = getItem(stack);
        if (it == null || stack.getTagCompound() == null) return null;
        int size = getMaxSize(stack);
        NBTTagCompound tag = stack.getTagCompound();
        ItemStack temp = new ItemStack(it, size, getDmg(stack));
        if (getTag(stack) != null) temp.setTagCompound(getTag(stack));
        return temp;
    }
    
    private static int getMaxSize(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return 0;
        ItemStack stack = new ItemStack(getItem(is));
        return tag.getInteger("stackSize") > stack.getMaxStackSize() ? stack.getMaxStackSize() : tag.getInteger("stackSize");
    }
}
