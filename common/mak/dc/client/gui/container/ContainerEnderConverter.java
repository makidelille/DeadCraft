package mak.dc.client.gui.container;

import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.tileEntities.TileEntityEnderConverter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

//TODO add shift clicks

public class ContainerEnderConverter extends ContainerDeadCraft{
	private TileEntityEnderConverter te;
	
	public ContainerEnderConverter(InventoryPlayer inv, TileEntityEnderConverter te) {
		super(inv, te, false);
		this.addSlotToContainer(new Slot((IInventory) te, 0, 80,61));
		this.te = te;
	}
	
	@Override
    public void detectAndSendChanges () {
        super.detectAndSendChanges();

		for (Object player : crafters) {
			((ICrafting)player).sendProgressBarUpdate(this, 0, te.getPower());
			((ICrafting)player).sendProgressBarUpdate(this, 1, te.getPowerLeft());
        }
    }
    
    @Override
    public void addCraftingToCrafters (ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
		crafter.sendProgressBarUpdate(this, 0, te.getPower());
		crafter.sendProgressBarUpdate(this, 1, te.getPowerLeft());

    }
    
    @Override
    public void updateProgressBar(int id, int data) {
    	super.updateProgressBar(id, data);
    	switch(id) {
    	case 0: te.setPower(data);
    	break;
    	case 1 : te.setPowerLeft(data);
    	break;
    	}
    }
	
	

}
