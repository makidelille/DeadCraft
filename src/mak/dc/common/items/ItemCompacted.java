package mak.dc.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class ItemCompacted extends Item {
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemStack fakeStack = new ItemStack(getItem(stack), getSize(stack), getDmg(stack));
        if (getTag(stack) != null) fakeStack.setTagCompound(getTag(stack));
        if (fakeStack.getItem() instanceof ItemCompacted) {
            fakeStack = getBaseStack(stack);
            list.add(StatCollector.translateToLocal("dc.compacted.info.item") + " : " + EnumChatFormatting.DARK_PURPLE + fakeStack.getDisplayName());
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(StatCollector.translateToLocal("dc.compacted.info.size") + " : " + EnumChatFormatting.DARK_GREEN + getTotalSize(stack));
                list.add(StatCollector.translateToLocal("dc.compacted.info.compLvl") + " : " + getCompressionLevel(stack));
            } else {
                list.add("" + EnumChatFormatting.ITALIC + EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
            }
        } else {
            if(fakeStack.getItem() == null) return;
            String display = fakeStack.getDisplayName();
            String itemName = StatCollector.translateToLocal(fakeStack.getItem().getUnlocalizedName() + ".name");
            list.add(StatCollector.translateToLocal("dc.compacted.info.item") + " : " + EnumChatFormatting.DARK_PURPLE + itemName);
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (!itemName.equals(display)) list.add(StatCollector.translateToLocal("dc.compacted.info.displayName") + " : " + EnumChatFormatting.ITALIC + EnumChatFormatting.DARK_AQUA + display);
                list.add(StatCollector.translateToLocal("dc.compacted.info.size") + " : " + EnumChatFormatting.DARK_GREEN  + fakeStack.stackSize);
                list.add(StatCollector.translateToLocal("dc.compacted.info.dmg") + " : " + fakeStack.getItemDamage());
            } else {
                list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
            }
        }
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
     * @return the uncrompressedStack
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
    
    private static int getCompressionLevel(ItemStack stack) {
        int lvl = 0;
        ItemStack unc = uncompactStack(stack);
        while (unc.getItem() instanceof ItemCompacted) {
            unc = uncompactStack(unc);
            lvl++;
        }
        return lvl;
    }
    
    private static int getTotalSize(ItemStack stack) {
        int size = 1;
        ItemStack unc = uncompactStack(stack);
        while (unc.getItem() instanceof ItemCompacted) {
            size *= unc.stackSize;
            unc = uncompactStack(unc);
        }
        size *= unc.stackSize;
        return size;
    }
    
    private static ItemStack getBaseStack(ItemStack stack) {
        ItemStack unc = uncompactStack(stack);
        while (unc.getItem() instanceof ItemCompacted) {
            unc = uncompactStack(unc);
        }
        return unc;
    }
}
