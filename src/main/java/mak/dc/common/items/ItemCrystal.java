package mak.dc.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mak.dc.common.util.Config.ConfigLib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrystal extends Item {
    
    /** handle by config */
    
    public static final int CRYSTALCOST = ConfigLib.CRYS_CRYSTALCOST;
    public static final int[] MAXCHARGE = { ConfigLib.CRYS_DEFMAXCHARGE, 4*ConfigLib.CRYS_DEFMAXCHARGE, 16*ConfigLib.CRYS_DEFMAXCHARGE };
    public static final String[] tiers = { "1", "2", "3" };
    
    /**
     * @param stack
     * @param chargeAmount
     * @return how much of the charge amount given is left after charge of the
     *         crystal
     */
    public static int chargeItem(ItemStack stack, int chargeAmount) {
        if (!stack.hasTagCompound()) return chargeAmount;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getBoolean("creativeSpawn")) {
            tag.setInteger("charge", MAXCHARGE[stack.getItemDamage()]);
            stack.setTagCompound(tag);
            return 0;
        }
        int curentCharge = tag.getInteger("charge");
        if (curentCharge >= MAXCHARGE[stack.getItemDamage()]) return chargeAmount;
        if (curentCharge + chargeAmount > MAXCHARGE[stack.getItemDamage()]) {
            tag.setInteger("charge", MAXCHARGE[stack.getItemDamage()]);
            stack.setTagCompound(tag);
            return curentCharge + chargeAmount - MAXCHARGE[stack.getItemDamage()];
        } else {
            tag.setInteger("charge", curentCharge + chargeAmount);
            stack.setTagCompound(tag);
            return 0;
        }
    }
    
    /**
     * @param stack
     * @param dischargeAmount
     * @return how much of the discharge amount given is left after discharge of
     *         the crystal
     */
    public static int dischargeItem(ItemStack stack, int dischargeAmount) {
        if (!stack.hasTagCompound()) return dischargeAmount;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getBoolean("creativeSpawn")) {
            tag.setInteger("charge", MAXCHARGE[stack.getItemDamage()]);
            stack.setTagCompound(tag);
            return 0;
        }
        int curentCharge = tag.getInteger("charge");
        if (curentCharge <= 0) return dischargeAmount;
        if (curentCharge - dischargeAmount < 0) {
            tag.setInteger("charge", 0);
            stack.setTagCompound(tag);
            return dischargeAmount - curentCharge;
        } else {
            tag.setInteger("charge", curentCharge - dischargeAmount);
            stack.setTagCompound(tag);
            return 0;
        }
    }
    
    public static int getCharge(ItemStack is) {
        if (!is.hasTagCompound()) return 0;
        NBTTagCompound tag = is.getTagCompound();
        return tag.getInteger("charge");
    }
    
    public static boolean isEmpty(ItemStack is) {
        if (is == null || !(is.getItem() instanceof ItemCrystal)) return false;
        if (!is.hasTagCompound()) return false;
        NBTTagCompound tag = is.getTagCompound();
        return tag.getInteger("charge") == 0;
    }
    
    public static boolean isFullyCharged(ItemStack is) {
        if (!is.hasTagCompound()) return false;
        NBTTagCompound tag = is.getTagCompound();
        return tag.getInteger("charge") == MAXCHARGE[is.getItemDamage()];
    }
    
    public static boolean isCreative(ItemStack is) {
    	if(!is.hasTagCompound()) return false;
    	return is.getTagCompound().hasKey("creativeSpawn") && is.getTagCompound().getBoolean("creativeSpawn");
    }
    
    private IIcon[] icons = new IIcon[3];
    
    public ItemCrystal() {
        super();
        setHasSubtypes(true);
        setMaxStackSize(1);
    }
    
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List l, boolean par4) {
        if (is.hasTagCompound() && is.getTagCompound().hasKey("charge")) {
            if (is.getTagCompound().getBoolean("creativeSpawn")) {
                l.add(StatCollector.translateToLocal("dc.charge") + " : " + EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.infinite") + EnumChatFormatting.RESET);
            } else {
                l.add(StatCollector.translateToLocal("dc.charge") + " : " + EnumChatFormatting.YELLOW + is.getTagCompound().getInteger("charge") + "/" + MAXCHARGE[is.getItemDamage()] + EnumChatFormatting.RESET);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                l.add(StatCollector.translateToLocal("dc.crystal.info.tier") + " : " + EnumChatFormatting.DARK_GREEN + (is.getItemDamage() + 1));
                if (is.getTagCompound().getBoolean("creativeSpawn"))
                    l.add(StatCollector.translateToLocal("dc.info.isCreative"));
            }else{
                l.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
            }
        }
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return 0;
        return 1d - (double) tag.getInteger("charge") / MAXCHARGE[stack.getItemDamage()];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int par1) {
        return icons[par1];
    }
    
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {
        for (int i = 0; i < tiers.length; i++) {
            ItemStack is = new ItemStack(item, 1, i);
            NBTTagCompound tag = new NBTTagCompound();
            
            tag.setBoolean("creativeSpawn", false);
            tag.setInteger("charge", 0);
            is.setTagCompound(tag);
            l.add(is);
            
            is = new ItemStack(item, 1, i);
            tag = new NBTTagCompound();
            tag.setBoolean("creativeSpawn", false);
            tag.setInteger("charge", MAXCHARGE[i]);
            is.setTagCompound(tag);
            l.add(is);
            
            is = new ItemStack(item, 1, i);
            tag = new NBTTagCompound();
            tag.setBoolean("creativeSpawn", true);
            tag.setInteger("charge", MAXCHARGE[i]);
            is.setTagCompound(tag);
            l.add(is);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister registerIcon) {
        for (int i = 0; i < tiers.length; i++) {
            icons[i] = registerIcon.registerIcon(Textures.CRYSTAL_TEXT_LOC + ".tier" + i);
        }
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound() && !stack.getTagCompound().getBoolean("creativeSpawn");
    }

	public static int getMaxCharge(ItemStack crys) {
		return MAXCHARGE[crys.getItemDamage()];
	}
    
}
