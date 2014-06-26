package mak.dc.common.items;

import java.util.List;

import mak.dc.common.util.Config.ConfigLib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemDeadWand extends Item {
    
    public static final int MAXCHARGE = ConfigLib.WAND_MAXCHARGE;
    
    public static int chargeItem(ItemStack stack, int chargeToAdd) {
        if (!stack.hasTagCompound()) return chargeToAdd;
        NBTTagCompound tag = stack.getTagCompound();
        int curentCharge = tag.getInteger("charge");
        if (curentCharge >= MAXCHARGE) return chargeToAdd;
        if (curentCharge + chargeToAdd > MAXCHARGE) {
            tag.setInteger("charge", MAXCHARGE);
            stack.setTagCompound(tag);
            return curentCharge + chargeToAdd - MAXCHARGE;
        } else {
            tag.setInteger("charge", curentCharge + chargeToAdd);
            stack.setTagCompound(tag);
            return 0;
        }
        
    }
    
    public static int dischargeItem(ItemStack stack, int chargeToRem) {
        if (!stack.hasTagCompound()) return chargeToRem;
        NBTTagCompound tag = stack.getTagCompound();
        int curentCharge = tag.getInteger("charge");
        if (curentCharge <= 0) return chargeToRem;
        if (curentCharge - chargeToRem < 0) {
            tag.setInteger("charge", 0);
            stack.setTagCompound(tag);
            return chargeToRem - curentCharge;
        } else {
            tag.setInteger("charge", curentCharge - chargeToRem);
            stack.setTagCompound(tag);
            return 0;
        }
    }
    
    public static int getCharge(ItemStack stack) {
        if (!stack.hasTagCompound()) return 0;
        NBTTagCompound tag = stack.getTagCompound();
        return tag.getInteger("charge");
    }
    
    private static ItemStack getCrystal(EntityPlayer player) {
        ItemStack re = null;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() instanceof ItemCrystal) {
                if (!ItemCrystal.isFullyCharged(is)) return is;
                re = is;
            }
        }
        return re;
    }
    
    public ItemDeadWand() {
        super();
        setMaxStackSize(1);
        setHasSubtypes(false);
        
    }
    
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List l, boolean par4) {
        if (is.hasTagCompound() && is.getTagCompound().hasKey("charge")) {
            l.add(StatCollector.translateToLocal("dc.charge") + " : " + EnumChatFormatting.YELLOW + is.getTagCompound().getInteger("charge") + "/" + MAXCHARGE);
        }
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return 0;
        return 1d - (double) tag.getInteger("charge") / MAXCHARGE;
    }
    
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {
        ItemStack is = new ItemStack(item, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("charge", 0);
        is.setTagCompound(tag);
        l.add(is);
        
        is = new ItemStack(item, 1, 0);
        tag = new NBTTagCompound();
        tag.setInteger("charge", MAXCHARGE);
        is.setTagCompound(tag);
        l.add(is);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            int charge = getCharge(stack);
            ItemStack cryIs = getCrystal(player);
            if (charge > 0 && cryIs != null) {
                int chargeleft = ItemCrystal.chargeItem(cryIs, charge);
                dischargeItem(stack, charge - chargeleft);
            }
        }
        return stack;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (!player.worldObj.isRemote && entity.isEntityAlive()) {
            if (!stack.hasTagCompound()) return false;
            
            NBTTagCompound tag = stack.getTagCompound();
            float targetPv = ((EntityLivingBase) entity).getHealth();
            boolean isCreative = player.capabilities.isCreativeMode;
            int charge = tag.getInteger("charge");
            
            if (charge >= MAXCHARGE) return true;
            else {
                
                entity.attackEntityFrom(new DamageSource("magic"), targetPv);
                player.worldObj.playSoundEffect(entity.posX + 0.5D, entity.posY + 0.5D, entity.posZ + 0.5D, "random.burp", 1.0F, 5f);
                
                if (!isCreative) {
                    player.attackEntityFrom(new DamageSource("magic"), targetPv * 2f / 3f);
                    
                    player.addPotionEffect(new PotionEffect(9, (int) targetPv * 20, 5, false));
                    player.addPotionEffect(new PotionEffect(17, (int) targetPv * 20, 5, false));
                }
                
                ItemStack crystalIs = getCrystal(player);
                int chargeToAdd = (int) (1f / 3f * targetPv);
                if (crystalIs != null) {
                    int chargeleft = ItemCrystal.chargeItem(crystalIs, (int) (2f / 3f * targetPv));
                    chargeToAdd += chargeleft;
                }
                chargeItem(stack, chargeToAdd);
                
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Textures.DEADWAND_TEXT_LOC);
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound();
    }
    
}
