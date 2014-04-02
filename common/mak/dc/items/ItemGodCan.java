package mak.dc.items;

import java.util.List;

import mak.dc.lib.ItemInfo;
import mak.dc.lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;

public class ItemGodCan extends Item{
	
	
	public ItemGodCan(){ //TODO
		super();
		this.setUnlocalizedName(ItemInfo.GODCAN_UNLOCALIZED_NAME);
		this.setMaxStackSize(1);
		this.setHasSubtypes(false);
		this.setMaxDamage(300);
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
	
//	@Override
//	public int getColorFromDamage(int par1) {
//		return 0;
//	}
//	
//	@Override
//	public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
//		// TODO Auto-generated method stub
//		return super.getIconFromDamageForRenderPass(par1, par2);
//	}
	
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
		if(!world.isRemote) {
			is.setItemDamage(0);
		}
		
		return is;
	}
	
	@Override
	public void onUpdate(ItemStack is, World world,	Entity ent, int par1, boolean par2) {
		if(!world.isRemote) {
			int time = is.getItemDamage();
			int[] ids = {1};
			if(time < this.getMaxDamage() -1 ) {
				this.applyEffects(world, ent, ids );
				is.damageItem(1, (EntityLivingBase) ent);
			}else{
				this.removeEffects(world, ent, ids);
				is.setItemDamage(300);
			}
		}
	}
	
	@Override
	public void onCreated(ItemStack is, World world,EntityPlayer player) {
	}
	
	private void applyEffects(World world, Entity ent, int[] effectIds) {
		if(!world.isRemote) {
			for (int i=0; i < effectIds.length; i++) {
				int id = effectIds[i];
				switch(id) {
				case 1 : 
					EntityPlayer player = (EntityPlayer) ent;
					if(player.capabilities.allowFlying) break;
					player.capabilities.allowFlying = true;
					player.sendPlayerAbilities();
					break;
				
				
				
				}
			}
		}
		
	}
	
	private void removeEffects(World world, Entity ent, int[] effectIds) {
		if(!world.isRemote) {
			for (int i=0; i < effectIds.length; i++) {
				int id = effectIds[i];
				switch(id) {
				case 1 : 
					EntityPlayerMP player = (EntityPlayerMP) ent;
					if(!player.capabilities.allowFlying) break;
					player.capabilities.allowFlying = false;
					player.sendPlayerAbilities();
					player.setGameType(GameType.SURVIVAL); //TODO i'll have to change the way it works
					break;
				
				
				
				}
			}
		}
	}
	
	
	
	

}
