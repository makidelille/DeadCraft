package mak.dc.client.gui.container;

import mak.dc.common.inventory.InventoryItemWithPower;
import mak.dc.common.items.ItemCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemWithPower extends Container {

	private class SlotCrystal extends Slot {

		public SlotCrystal(IInventory par1iInventory, int par2, int par3,
				int par4) {
			super(par1iInventory, par2, par3, par4);
		}

		@Override
		public boolean isItemValid(ItemStack par1ItemStack) {
			return par1ItemStack.getItem() instanceof ItemCrystal;
		}

	}

	private InventoryItemWithPower stackInv;
	private InventoryPlayer playerInv;

	//TODO add shift clicks
	
	public ContainerItemWithPower(EntityPlayer player,
			InventoryItemWithPower inv) {
		this.playerInv = player.inventory;
		this.stackInv = inv;

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInv, j + (i + 1) * 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}

		addSlotToContainer(new SlotCrystal(inv, 0, 80, 32));

	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return stackInv.isUseableByPlayer(var1);
	}

	@Override
	public ItemStack slotClick(int slot, int button, int flag,
			EntityPlayer player) {
		// this will prevent the player from interacting with the item that
		// opened the inventory:
		if (slot >= 0 && getSlot(slot) != null
				&& getSlot(slot).getStack() == player.getHeldItem()) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}

}
