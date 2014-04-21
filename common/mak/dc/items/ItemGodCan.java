package mak.dc.items;

import java.util.List;

import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGodCan extends ItemFood{
	
	
	public ItemGodCan(){ //TODO finish
		super(1, false);
		this.setUnlocalizedName(IBTInfos.ITEM_GODCAN_UNLOCALIZED_NAME);
		this.setMaxStackSize(1);
		this.setHasSubtypes(false);
		this.setMaxDamage(1200);
		this.setFull3D();
		this.setAlwaysEdible();
		
	}
	
	
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		// TODO add infos
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
	public ItemStack onEaten(ItemStack is, World world,	EntityPlayer player) {
		if(!world.isRemote) {
			is.setItemDamage(0);
			NBTTagCompound tag = is.getTagCompound();
			if(tag == null) tag = new NBTTagCompound();
			tag.setString("last user", player.getCommandSenderName());
			int[] ids = {1,2,3,4};
			tag.setIntArray("effects ids", ids );
			is.setTagCompound(tag);
			if(player.isSneaking()) {
				removeEffects(world, player, ids);
				is.setItemDamage(getMaxDamage());
			}
		}
		
		return is;
	}
	
	@Override
	public void onUpdate(ItemStack is, World world,	Entity ent, int par1, boolean par2) {
		if(!world.isRemote) {
			int time = is.getItemDamage();
			NBTTagCompound tag = is.getTagCompound();
			if(tag == null) return;
			int[] ids = tag.getIntArray("effects ids");
			if(time < this.getMaxDamage() -1 ) {
				tag.setBoolean("isActive", true);
//				this.applyEffects(world, ent, ids );
				is.damageItem(1, (EntityLivingBase) ent);
			}else if(time == this.getMaxDamage()){
				this.removeEffects(world, ent, ids);
				is.setItemDamage(this.getMaxDamage());
				tag.setBoolean("isActive",false);
			}
			is.setTagCompound(tag);
		}
	}
		
	@Override
	public void onCreated(ItemStack is, World world,EntityPlayer player) {
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		if(!player.worldObj.isRemote) {
			NBTTagCompound tag = item.getTagCompound();
			if(tag != null && tag.getBoolean("isActive"))
				return false;
		}
		
		return true;
	}
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) { //TODO almost finish
		if(!entityItem.worldObj.isRemote) {
			System.out.println("test");
			NBTTagCompound tag = entityItem.getEntityData();
			if(tag!=null){
				if(tag.getBoolean("isActive")) {
					String lastUser = tag.getString("last user");
					EntityPlayer player = entityItem.worldObj.getPlayerEntityByName(lastUser);
					removeEffects(entityItem.worldObj, player, tag.getIntArray("effects ids"));
					player.setDead();
					entityItem.setDead();
					System.out.println("test2");
				}
			}
		}
		return false;
	}
	
	
	
	
	private void applyEffects(World world, Entity ent, int[] effectIds) {
		if(!world.isRemote) {
			for (int i=0; i < effectIds.length; i++) {
				int id = effectIds[i];
				EntityPlayer player = (EntityPlayer) ent;
				switch(id) {
				case 1 :					
					if(player.capabilities.allowFlying) break;
					player.capabilities.allowFlying = true;
					player.sendPlayerAbilities();
					break;
				case 2 :
					player.addPotionEffect(new PotionEffect(1, 20, 5));
					player.addPotionEffect(new PotionEffect(8, 20, 5));
					break;
				case 3:
					player.addPotionEffect(new PotionEffect(11,20,5));
					player.addPotionEffect(new PotionEffect(12,20,5));
					break;
				case 4:
					player.addPotionEffect(new PotionEffect(3,20,1000));
					player.addPotionEffect(new PotionEffect(5,20,100));
					break;
				}
			}
		}
		
	}
	
	private void removeEffects(World world, Entity ent, int[] effectIds) {
		if(!world.isRemote) {
			for (int i=0; i < effectIds.length; i++) {
				int id = effectIds[i];
				EntityPlayerMP player = (EntityPlayerMP) ent;
				switch(id) {
				case 1 : 
					if(!player.capabilities.allowFlying) break;
					PlayerCapabilities newCap = player.capabilities;
					newCap.allowFlying = false;
					player.playerNetServerHandler.processPlayerAbilities(new C13PacketPlayerAbilities(newCap));
					player.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S39PacketPlayerAbilities(newCap));
					break;
				case 2 :
					player.removePotionEffect(1);
					player.removePotionEffect(8);
					break;
				case 3:
					player.removePotionEffect(11);
					player.removePotionEffect(12);
					break;
				case 4:
					player.removePotionEffect(3);
					player.removePotionEffect(5);
					break;
				
				
				
				}
			}
		}
	}
	
	
	
	

}
