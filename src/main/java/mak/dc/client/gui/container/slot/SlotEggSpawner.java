package mak.dc.client.gui.container.slot;

import mak.dc.common.items.DeadCraftItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotEggSpawner extends Slot {
    
    private byte i;
    
    public SlotEggSpawner(IInventory inv, int i, int x, int y) {
        super(inv, i, x, y);
        this.i = (byte) i;
        
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return isItemValidSlot(stack, i);
    }
    
    private boolean isItemValidSlot(ItemStack stack, int slot) {
        if (slot == 0 || slot == 2) return stack.getItem() == Items.nether_star;
        else if (slot == 1) return stack.getItem() == Items.skull;
        else if (slot == 3 || slot == 5) return Item.getIdFromItem(stack.getItem()) == Block.getIdFromBlock(Blocks.obsidian);
        else if (slot == 4) return Item.getIdFromItem(stack.getItem()) == Block.getIdFromBlock(Blocks.diamond_block);
        else return false;
    }
    
}
