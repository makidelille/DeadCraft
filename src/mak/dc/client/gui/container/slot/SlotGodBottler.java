package mak.dc.client.gui.container.slot;

import mak.dc.common.items.ItemCrystal;
import mak.dc.common.items.ItemGodCan;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGodBottler extends Slot {
    
    private byte i;
    
    public SlotGodBottler(IInventory par1iInventory, int par2, int par3, int par4) {
        super(par1iInventory, par2, par3, par4);
        i = (byte) par2;
    }
    
    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        switch (i) {
            case 1:
                return par1ItemStack.getItem() instanceof ItemGodCan;
            case 2:
                return false;
            default:
                return true;
        }
    }
    
}
