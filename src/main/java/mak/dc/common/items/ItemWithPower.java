package mak.dc.common.items;

import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.GuiLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public abstract class ItemWithPower extends Item {


	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote && player.isSneaking()){
			FMLNetworkHandler.openGui(player, Lib.MOD_ID, GuiLib.ID_INV_POWERITEM, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		return stack;
	}
	
	
	public static int getCharge(ItemStack item){
		if(!hasCrystal(item)) return 0;
		ItemStack cry = getCrystal(item);
		return ItemCrystal.getCharge(cry);
	}
	
	
	public static void setCrystal(ItemStack item, ItemStack crys){
		if(hasCrystal(item) || !(crys.getItem() instanceof ItemCrystal)) return;
		NBTTagCompound cryTag = crys.writeToNBT(new NBTTagCompound());
		item.getTagCompound().setTag("crystal", cryTag);
	}
	
	public static ItemStack getCrystal(ItemStack item){
		if(hasCrystal(item)){
			return ItemStack.loadItemStackFromNBT((NBTTagCompound) item.getTagCompound().getTag("crystal"));
		}
		return null;
	}
	
	private static boolean hasCrystal(ItemStack item){
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null) {
			item.setTagCompound(new NBTTagCompound());
			return false;
		}
		return tag.hasKey("crystal") && tag.getTag("crystal") != null;
	}


	public static int getInvSize(ItemStack stack) {
		return 1;
	}

}