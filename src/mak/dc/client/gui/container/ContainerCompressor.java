package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotPower;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.tileEntities.TileEntityDeadCraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCompressor extends ContainerDeadCraft{

    public ContainerCompressor(InventoryPlayer inv, TileEntityCompressor te) {
        super(inv, te, false);
        this.addSlotToContainer(new Slot((IInventory) te,te.slotInput, 53,34));
        this.addSlotToContainer(new Slot(te,te.slotOutput, 118, 34 ));
        this.addSlotToContainer(new SlotPower(te, te.slotPower, 8, 61));
    }
    
}
