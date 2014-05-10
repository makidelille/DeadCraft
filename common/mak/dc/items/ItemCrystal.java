package mak.dc.items;

import java.util.List;

import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrystal extends Item {


    /** handle by config */

    public static final int CRYSTALCOST = 100;
    public static final int MAXCHARGE = 2_500;


    public ItemCrystal () {
        super();
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
    }
    
    @Override
    public void getSubItems(Item item, CreativeTabs tab ,List l) {
    	ItemStack is = new ItemStack(item, 1, 0);
    	NBTTagCompound tag = new NBTTagCompound();
    	
    	tag.setBoolean("creativeSpawn", false);
    	tag.setInteger("charge", 0);
    	is.setTagCompound(tag);
    	l.add(is);
    	
    	is = new ItemStack(item, 1, 0);
    	tag = new NBTTagCompound();
    	tag.setBoolean("creativeSpawn", false);
    	tag.setInteger("charge", MAXCHARGE); 
    	is.setTagCompound(tag);
    	l.add(is);
    	
    	is = new ItemStack(item, 1, 0);
    	tag = new NBTTagCompound();
    	tag.setBoolean("creativeSpawn", true);
    	tag.setInteger("charge", MAXCHARGE);
    	is.setTagCompound(tag);
    	l.add(is);
    	
    }
    
    
    @SideOnly (Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister registerIcon) {
    	itemIcon = registerIcon.registerIcon(Textures.CRYSTAL_TEXT_LOC);
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List l, boolean par4) {
    	if(is.hasTagCompound() && is.getTagCompound().hasKey("charge")){
    		if(is.getTagCompound().getBoolean("creativeSpawn")) {
    			l.add("is Creative Spawned");
    			l.add("charge : " + EnumChatFormatting.YELLOW + "infinite"+EnumChatFormatting.RESET);
    		}
    		else l.add("charge : " + EnumChatFormatting.YELLOW + is.getTagCompound().getInteger("charge") + "/" +  MAXCHARGE + EnumChatFormatting.RESET);
    	}
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
    	return stack.hasTagCompound() && !stack.getTagCompound().getBoolean("creativeSpawn");
    }
    
	  @Override
	public double getDurabilityForDisplay(ItemStack stack) {
		 NBTTagCompound tag = stack.getTagCompound();
		 if(tag == null) return 0;
		 return 1d - (double) tag.getInteger("charge") / MAXCHARGE;
	  }	
    
    
    
    public static boolean isFullyCharged (ItemStack is) {
    	if(!is.hasTagCompound()) return false;
    	NBTTagCompound tag = is.getTagCompound();
    	return tag.getInteger("charge") == MAXCHARGE;
    }
    
    public static boolean isEmpty (ItemStack is) {
    	if(!is.hasTagCompound()) return false;
    	NBTTagCompound tag = is.getTagCompound();
    	return tag.getInteger("charge") == 0;
    }
    
    /**
     * 
     * @param stack
     * @param chargeAmount
     * @return how much of the charge amount given is left after charge of the crystal
     */
    public static int chargeItem (ItemStack stack, int chargeAmount) {
    	if(!stack.hasTagCompound()) return chargeAmount;
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag.getBoolean("creativeSpawn")) {
    		tag.setInteger("charge", MAXCHARGE);
    		stack.setTagCompound(tag);    	
    		return 0;
    	}
    	int curentCharge = tag.getInteger("charge");
    	if(curentCharge >= MAXCHARGE) return chargeAmount;
    	if(curentCharge  + chargeAmount > MAXCHARGE) {
    		tag.setInteger("charge", MAXCHARGE);
    		stack.setTagCompound(tag);    		
    		return curentCharge + chargeAmount - MAXCHARGE;
    	}else{
    		tag.setInteger("charge", curentCharge + chargeAmount);
    		stack.setTagCompound(tag);    
    		return 0;
    	}
    }

    /**
     * 
     * @param stack
     * @param dischargeAmount
     * @return how much of the discharge amount given is left after discharge of the crystal
     */
    public static int dischargeItem (ItemStack stack, int dischargeAmount) {
    	if(!stack.hasTagCompound()) return dischargeAmount;
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag.getBoolean("creativeSpawn")) {
    		tag.setInteger("charge", MAXCHARGE);
    		stack.setTagCompound(tag);    	
    		return 0;
    	}
    	int curentCharge = tag.getInteger("charge");
    	if(curentCharge <= 0) return dischargeAmount;
    	if(curentCharge  - dischargeAmount < 0) {
    		tag.setInteger("charge", 0);
    		stack.setTagCompound(tag);    		
    		return dischargeAmount - curentCharge;
    	}else{
    		tag.setInteger("charge", curentCharge - dischargeAmount);
    		stack.setTagCompound(tag);    		
    		return 0;
    	}
    }

}
