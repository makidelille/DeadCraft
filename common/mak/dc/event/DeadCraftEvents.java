package mak.dc.event;


import mak.dc.items.ItemGodCan;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DeadCraftEvents {

	@SubscribeEvent
	public void OnPlayerOpenGui(PlayerOpenContainerEvent e) {
		InventoryPlayer playerInv = e.entityPlayer.inventory;
		for(int i=0; i < playerInv.getSizeInventory(); i++) {
			if(playerInv.getStackInSlot(i) != null && playerInv.getStackInSlot(i).getItem() instanceof ItemGodCan) {
				ItemStack is = playerInv.getStackInSlot(i);
				if(!is.hasTagCompound())  return;
				NBTTagCompound tag = is.getTagCompound();
				boolean flag = tag.getBoolean("isActive");
				if(flag && !e.entityPlayer.openContainer.equals(e.entityPlayer.inventoryContainer)) ItemGodCan.pauseEffects(e.entityPlayer.worldObj, is, e.entityPlayer);;
			}
		}
	}
	
	@SubscribeEvent
	public void OnPlayerToss(ItemTossEvent e) {
		if(e.entityItem.getEntityItem().getItem() instanceof ItemGodCan) {
			ItemStack is = e.entityItem.getEntityItem();
			if(!is.hasTagCompound()) return;			
			NBTTagCompound tag = is.getTagCompound();
			boolean flag = tag.getBoolean("isActive");
			ItemGodCan.pauseEffects(e.player.worldObj, is, e.player);
		}
	}
}
