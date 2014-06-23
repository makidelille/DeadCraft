package mak.dc.client.gui.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCompressor extends Slot {

    private IInventory inv;
    private int id;
    
    public SlotCompressor(IInventory par1iInventory, int par2, int par3, int par4) {
        super(par1iInventory, par2, par3, par4);
        this.inv = par1iInventory;
        this.id = par2;
    }
    
    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return inv.isItemValidForSlot(id, par1ItemStack);
    }
    
}
