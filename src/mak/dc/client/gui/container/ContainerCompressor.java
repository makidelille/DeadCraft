package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotCompressor;
import mak.dc.client.gui.container.slot.SlotPower;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.tileEntities.TileEntityDeadCraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCompressor extends ContainerDeadCraft{

    private TileEntityCompressor te;
    
    public ContainerCompressor(InventoryPlayer inv, TileEntityCompressor te) {
        super(inv, te, false);
        this.addSlotToContainer(new SlotCompressor(te,te.slotInput, 53,34));
        this.addSlotToContainer(new SlotCompressor(te,te.slotOutput, 118, 34 ));
        this.addSlotToContainer(new SlotPower(te, te.slotPower, 8, 61));
        this.te = te;
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, te.getProgress());
        crafter.sendProgressBarUpdate(this, 1, te.getCharge());
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object player : crafters) {
            ((ICrafting) player).sendProgressBarUpdate(this, 0, te.getProgress());
            ((ICrafting) player).sendProgressBarUpdate(this, 1, te.getCharge());
        }
    }
    
    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        switch (id) {
            case 0:
                te.setProgress(data);
                break;
            case 1:
                te.setCharge(data);
                break;
        }
    }
    
    
}
