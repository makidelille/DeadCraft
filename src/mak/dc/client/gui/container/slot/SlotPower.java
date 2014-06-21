package mak.dc.client.gui.container.slot;

import mak.dc.common.items.ItemCrystal;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPower extends Slot{

    public SlotPower(IInventory par1iInventory, int id, int x, int y) {
        super(par1iInventory, id, x, y);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemCrystal;
    }
    
}
