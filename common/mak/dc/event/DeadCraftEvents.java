package mak.dc.event;


import java.util.List;

import mak.dc.items.ItemGodCan;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DeadCraftEvents {
	
	@SubscribeEvent
	public void onPlayerOpenGui(PlayerOpenContainerEvent e) {
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
	public void onPlayerToss(ItemTossEvent e) {
		if(e.entityItem.getEntityItem().getItem() instanceof ItemGodCan) {
			ItemStack is = e.entityItem.getEntityItem();
			if(!is.hasTagCompound()) return;			
			NBTTagCompound tag = is.getTagCompound();
			boolean flag = tag.getBoolean("isActive");
			ItemGodCan.pauseEffects(e.player.worldObj, is, e.player);
		}
	}
	
	@SubscribeEvent
	public void onplayerjoined(EntityJoinWorldEvent e){
		if (!(e.entity instanceof EntityPlayer)) return;
		World world = e.world;
		if(world != null) {
			List tes = world.loadedTileEntityList;
			for (int i = 0; i<tes.size(); i++) {
				if(tes.get(i) instanceof TileEntityDeadCraft) {
					((TileEntityDeadCraft)tes.get(i)).syncWithplayer((EntityPlayerMP) e.entity);
				}
			}
		}
	}
	
}
