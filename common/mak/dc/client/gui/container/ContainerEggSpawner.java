package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotEggSpawner;
import mak.dc.common.tileEntities.TileEntityEggSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerEggSpawner extends ContainerDeadCraft {
    
    private TileEntityEggSpawner te;
    private InventoryPlayer inv;
    
    public ContainerEggSpawner(InventoryPlayer invPlayer, TileEntityEggSpawner te) {
        super(invPlayer, te, true);
        this.te = te;
        inv = invPlayer;
        
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                addSlotToContainer(new SlotEggSpawner(te, x + y * 3, 65 + 18 * x, 18 + y * 18));
            }
        }
        
        addSlotToContainer(new SlotEggSpawner(te, 6, 29, 72));
        
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(invPlayer, x, 11 + 18 * x, 165));
        }
        
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 11 + 18 * x, 107 + y * 18));
            }
        }
        
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting player) {
        super.addCraftingToCrafters(player);
        
        player.sendProgressBarUpdate(this, 0, te.getBuildTime());
        player.sendProgressBarUpdate(this, 1, te.getEggInStock());
        player.sendProgressBarUpdate(this, 2, te.getStarted());
        player.sendProgressBarUpdate(this, 3, te.getRedstoneState());
        player.sendProgressBarUpdate(this, 4, te.getMode());
        player.sendProgressBarUpdate(this, 5, te.getPower());
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        
        for (Object player : crafters) {
            ((ICrafting) player).sendProgressBarUpdate(this, 0, te.getBuildTime());
            ((ICrafting) player).sendProgressBarUpdate(this, 1, te.getEggInStock());
            ((ICrafting) player).sendProgressBarUpdate(this, 2, te.getStarted());
            ((ICrafting) player).sendProgressBarUpdate(this, 3, te.getRedstoneState());
            ((ICrafting) player).sendProgressBarUpdate(this, 4, te.getMode());
            ((ICrafting) player).sendProgressBarUpdate(this, 5, te.getPower());
            
        }
    }
    
    @Override
    public TileEntityEggSpawner getTileEntity() {
        return te;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        
        Slot slot = getSlot(i);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            
            if (i < te.getSizeInventory() - 1) {
                if (!mergeItemStack(stack, 7, 43, false)) return null;
            } else {
                int pSlot = ValidForASlot(stack);
                
                switch (pSlot) {
                    case 0:
                        return null;
                    case 1:
                        if (!mergeItemStack(stack, pSlot - 1, pSlot, false) && !mergeItemStack(stack, pSlot + 1, pSlot + 2, false)) return null;
                        break;
                    case 4:
                        if (!mergeItemStack(stack, pSlot - 1, pSlot, false) && !mergeItemStack(stack, pSlot + 1, pSlot + 2, false)) return null;
                        break;
                    case 2:
                        if (!mergeItemStack(stack, pSlot - 1, pSlot, false)) return null;
                        break;
                    case 5:
                        if (!mergeItemStack(stack, pSlot - 1, pSlot, false)) return null;
                        break;
                    case 7:
                        if (!mergeItemStack(stack, pSlot - 1, pSlot + 1, false)) return null;
                        break;
                }
                
            }
            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0:
                te.setBuildTime(data);
                break;
            case 1:
                te.setEggInStock(data);
                break;
            case 2:
                te.setStarted((byte) data);
                break;
            case 3:
                te.setRedstoneState((byte) data);
                break;
            case 4:
                te.setMode((byte) data);
                break;
            case 5:
                te.setPower(data);
                break;
        }
    }
    
    private int ValidForASlot(ItemStack stack) {
        for (int i = 0; i < te.getSizeInventory(); i++) {
            if (te.isItemValidForSlot(i, stack)) return i + 1;
        }
        for (int j = 0; j < inv.getSizeInventory(); j++) {
            if (inv.isItemValidForSlot(j, stack)) return -(j + 1);
        }
        
        return 0;
    }
    
}
