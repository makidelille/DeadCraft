package mak.dc.client.gui.container;

import mak.dc.common.inventory.InventoryItemWithPower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerItemWithPower extends Container{

	private InventoryItemWithPower stackInv;
	private InventoryPlayer playerInv;
	
	public ContainerItemWithPower(EntityPlayer player, InventoryItemWithPower inv){
		this.playerInv = player.inventory;
		this.stackInv = inv;
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return stackInv.isUseableByPlayer(var1);
	}

}
