package mak.dc.common.items;

import java.util.List;

import mak.dc.common.util.Config.ConfigLib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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

public class ItemDeadWand extends ItemWithPower {
    
	//TODO rewrite the code
	
    public static final int MAXCHARGE = ConfigLib.WAND_MAXCHARGE;
    
    public ItemDeadWand() {
        super();
        setMaxStackSize(1);
        setHasSubtypes(false);
        
    }
    
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List l, boolean par4) {
    	super.addInformation(is, player, l, par4);
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (!player.worldObj.isRemote && entity.isEntityAlive()) {
        	use(stack, player.worldObj, entity, player);
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

	@Override
	protected void use(ItemStack stack, World world, Entity ent,EntityPlayer player) {
		if(ent instanceof EntityLiving){
			EntityLiving entlive = (EntityLiving) ent;
			if(discharge(stack, (int)entlive.getHealth())){
				entlive.setDead();
			}
		}
	}

	@Override
	protected EnumPowerUseProp getUseType() {
		return EnumPowerUseProp.ONUSE;
	}
    
}
