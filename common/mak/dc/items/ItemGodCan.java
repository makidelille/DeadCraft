package mak.dc.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mak.dc.DeadCraft;
import mak.dc.canEffects.CanEffect;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGodCan extends ItemFood{
		
	public ItemGodCan(){
		super(1, false);
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setAlwaysEdible();
		
	}
	
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List infos, boolean par4) {
		if(stack.hasTagCompound()){
			NBTTagCompound  tag = stack.getTagCompound();
			if(tag.hasKey("effect_ids")){
				NBTTagList ids = (NBTTagList) tag.getTag("effect_ids");
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					for(int i =0; i < ids.tagCount(); i++) {
						infos.add("effects id : " + (ids.getCompoundTagAt(i).getInteger("id")));
					}
					infos.add("Is active : " + tag.getBoolean("isActive"));
					infos.add("Time in Use : " + (tag.getInteger("tick") / 20) +  "s");
				
				}else {
					infos.add(EnumChatFormatting.YELLOW + "-- Press " +EnumChatFormatting.ITALIC + "Shift" +EnumChatFormatting.RESET + "" + EnumChatFormatting.YELLOW +  " for More Infos --" );
					for(int i =0; i < ids.tagCount(); i++) {
						infos.add("effects : " + DeadCraft.canCraftingManager.getCanEffect((ids.getCompoundTagAt(i).getInteger("id"))).getName());
					}
				}
			}
		}

	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Textures.GODCAN_TEXT_LOC);
	}

	
    public boolean showDurabilityBar(ItemStack stack) {
        if(stack.hasTagCompound()) return stack.getTagCompound().hasKey("isActive") ? stack.getTagCompound().getBoolean("isActive") : false;
        return false;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
    	
    	int maxDuration = 0;
    	int tickInUse = 0;
        
    	NBTTagCompound tag = stack.getTagCompound();
    	if(!tag.hasKey("tick") || !tag.hasKey("effect_ids")) return 0;
    	tickInUse = tag.getInteger("tick");
		NBTTagList tagList = (NBTTagList) tag.getTag("effect_ids");
		for (int i=0; i <tagList.tagCount(); i++) {
			NBTTagCompound id =  tagList.getCompoundTagAt(i);
			if(id.hasKey("id")) {
				int effectId = id.getInteger("id");
				CanEffect effect = DeadCraft.canCraftingManager.getCanEffect(effectId);
				if(effect.getDuration() > maxDuration) maxDuration = effect.getDuration();
			}

		}
    	
    	return (double)tickInUse / (double)maxDuration;
    }
    	
	
	@Override
	public IIcon getIconFromDamage(int par1) {
		return this.itemIcon;
	}
	
	@Override
	public ItemStack onEaten(ItemStack is, World world,	EntityPlayer player) {
		if(!world.isRemote) {
			NBTTagCompound tag = is.getTagCompound();
			if(tag != null) {
				if(!tag.hasKey("effect_ids")) return is;
				tag.setBoolean("isActive", true);
			}
		}
		return is;
	}
	

	
	@Override
	public void onUpdate(ItemStack is, World world,	Entity ent, int par4, boolean par5) {
		if(!world.isRemote) {
			NBTTagCompound tag = is.getTagCompound();
			if(tag.getInteger("tick") > 0) tag.setBoolean("isActive", true);
			if(tag != null && tag.getBoolean("isActive")) {
				int tickInUse = tag.getInteger("tick");
				if(!tag.hasKey("effect_ids")) return ;
				NBTTagList tagList = (NBTTagList) tag.getTag("effect_ids");
				if(tagList.tagCount() == 0) {
					tag.setInteger("tick", 0);
					tag.setBoolean("isActive", false);
					return;
				}for (int i=0; i <tagList.tagCount(); i++) {
					NBTTagCompound id =  tagList.getCompoundTagAt(i);
					if(id.hasKey("id")) {
						int effectId = id.getInteger("id");
						CanEffect effect = DeadCraft.canCraftingManager.getCanEffect(effectId);
						if(tickInUse < effect.getDuration()) 
							effect.applyEffect(world, (EntityPlayer) ent);
						else {
							effect.removeEffect(world, (EntityPlayer) ent);
							tagList.removeTag(i);
						}
					}
				}
				tag.setInteger("tick", tickInUse+1);
				is.setTagCompound(tag);

			}
			
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.drink;
	}


	private static void removeEffects(World world, ItemStack is,	EntityPlayer player) {
		if(!world.isRemote) {
			NBTTagCompound tag = is.getTagCompound();
			if(tag != null) {
				if(!tag.hasKey("effect_ids")) return ;
				NBTTagList tagList = (NBTTagList) tag.getTag("effect_ids");
				if(tagList.tagCount() == 0) {
					tag.setInteger("tick", 0);
					tag.setBoolean("isActive", false);
					return;
				}for (int i=0; i <tagList.tagCount(); i++) {
					NBTTagCompound id =  tagList.getCompoundTagAt(i);
					if(id.hasKey("id")) {
						int effectId = id.getInteger("id");
						CanEffect effect = DeadCraft.canCraftingManager.getCanEffect(effectId);
						effect.removeEffect(world,  player);
					}

				}
				is.setTagCompound(tag);

			}
			
		}
		
	}
	
	public static void pauseEffects(World world,ItemStack is,EntityPlayer player) {
		if(!world.isRemote) {
			NBTTagCompound tag = is.getTagCompound();
			if(tag != null && tag.getBoolean("isActive")) {
				tag.setBoolean("isActive", false);
				removeEffects(world, is, player);
			}
			is.setTagCompound(tag);
		}
	}
	
}
