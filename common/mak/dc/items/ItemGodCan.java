package mak.dc.items;

import java.util.ArrayList;
import java.util.List;

import mak.dc.lib.ItemInfo;
import mak.dc.lib.Textures;
import mak.dc.potion.DeadCraftPotion;
import mak.dc.potion.DeadCraftPotionEffect;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGodCan extends ItemPotion{
	
	
	public ItemGodCan(){
		super();
		this.setUnlocalizedName(ItemInfo.GODCAN_UNLOCALIZED_NAME);
		this.setMaxStackSize(16);
		this.setHasSubtypes(true);
		this.setMaxDamage(3);
		this.setNoRepair();
		
	}
	
	
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		// TODO Auto-generated method stub
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Textures.GODCAN_TEXT_LOC);
	}
	
	@Override
	public IIcon getIconFromDamage(int par1) {
		return this.itemIcon;
	}
	
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
		if(!world.isRemote) {
			player.addPotionEffect(new DeadCraftPotionEffect(1, 100, 1, false));
		}
		
		return is;
	}
	
	@Override
	public void onCreated(ItemStack is, World world,EntityPlayer player) {
	}
	
	

}
