package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotGodBottler;
import mak.dc.common.tileEntities.TileEntityGodBottler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGodBottler extends ContainerDeadCraft {
    
    private TileEntityGodBottler te;
    private InventoryPlayer invPlayer;
    
    public ContainerGodBottler(InventoryPlayer inventory, TileEntityGodBottler te2) {
        super(inventory, te2, true);
        te = te2;
        invPlayer = inventory;
        
        addSlotToContainer(new SlotGodBottler(te, 0, 17, 62));
        addSlotToContainer(new SlotGodBottler(te, 1, 65, 6));
        addSlotToContainer(new SlotGodBottler(te, 2, 99, 63));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotGodBottler(te, 3 + i * 3 + j, 115 + j * 18, 6 + i * 18));
            }
        }
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(invPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
        
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, te.getWorkedTime());
        crafter.sendProgressBarUpdate(this, 1, te.getPower());
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object player : crafters) {
            ((ICrafting) player).sendProgressBarUpdate(this, 0, te.getWorkedTime());
            ((ICrafting) player).sendProgressBarUpdate(this, 1, te.getPower());
        }
    }
    
    private int getSlotvalidForStack(ItemStack stack) {
        for (int i = 0; i < te.getSizeInventory(); i++) {
            if (te.isItemValidForSlot(i, stack)) return i;
        }
        return -1;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack re = null;
        Slot slot = getSlot(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            re = stack.copy();
            if (i < te.getSizeInventory() - 1) {
                if (!mergeItemStack(stack, 9, 44, false)) return null;
            } else {
                int slotNb = getSlotvalidForStack(stack);
                if (slotNb != -1) {
                    switch (slotNb) {
                        case 0:
                            if (!mergeItemStack(stack, slotNb, slotNb + 1, false)) return null;
                            break;
                        case 1:
                            if (!mergeItemStack(stack, slotNb, slotNb + 1, false)) return null;
                            break;
                        case 3:
                            if (!mergeItemStack(stack, slotNb, slotNb + 6, false)) return null;
                            break;
                    }
                }
            }
            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return re;
    }
    
    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        switch (id) {
            case 0:
                te.setWorkedTime(data);
                break;
            case 1:
                te.setPower(data);
                break;
        }
    }
    
}
