package mak.dc.client.gui.container.slot;

import mak.dc.items.ItemLifeCrystal;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotEggSpawner extends Slot {

	private byte i;

	
	public SlotEggSpawner(IInventory inv, int i, int x,int y) {
		super(inv, i, x, y);
		this.i = (byte) i;
		
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return isItemValidSlot(stack, i);
	}
	
	private boolean isItemValidSlot(ItemStack stack, int slot){
		if(slot == 0 || slot == 2)
			return stack.getItem() == Item.netherStar ;
		else if(slot == 1)
			return stack.getItem() == Item.skull;
		else if(slot == 3 || slot == 5)
			return stack.itemID == Block.obsidian.blockID;
		else if (slot == 4)
			return stack.itemID == Block.blockDiamond.blockID;
		else if(slot ==  6 || slot == 7)
			return stack.getItem() instanceof ItemLifeCrystal;
		else		
			return false;
	}
	

}
