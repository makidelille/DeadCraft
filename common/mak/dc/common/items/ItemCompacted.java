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
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        iconRegister.registerIcon(Textures.COMPACTED_TEXT_LOC);
    }
    
    private static Item getItem(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return null;
        int id = tag.getInteger("stackId");
        if (id == Item.getIdFromItem(DeadCraftItems.compacted)) return null;
        return Item.getItemById(id);
    }
    
    private static int getSize(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return 0;
        return tag.getInteger("stackSize");
    }
    
    private static int getDmg(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return 0;
        return tag.getInteger("stackDamage");
    }
    
    private static NBTTagCompound getTag(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        if (tag == null) return null;
        return (NBTTagCompound) tag.getTag("stackTag");
        
    }
    
    public static ItemStack compactStackInto(ItemStack stack) {
        ItemStack result = new ItemStack(DeadCraftItems.compacted);
        if (stack.getItem() instanceof ItemCompacted) {
            // TODO handle it
            return result;
        } else {
            NBTTagCompound tag = result.getTagCompound();
            if (tag == null) tag = new NBTTagCompound();
            tag.setInteger("stackId", Item.getIdFromItem(stack.getItem()));
            tag.setInteger("stackSize", stack.stackSize);
            tag.setInteger("StackDamage", stack.getItemDamage());
            tag.setTag("stackTag", stack.getTagCompound());
            result.setTagCompound(tag);
            return result;
        }
    }
    
    public static ItemStack uncompactStack(ItemStack stack) {
        Item it = getItem(stack);
        if (it == null) return null;
        ItemStack re = new ItemStack(it, getSize(stack), getDmg(stack));
        re.setTagCompound(getTag(stack));
        return re;
    }
}
