package mak.dc.client.gui.container;

import mak.dc.DeadCraft;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.tileEntities.TileEntityEnderConverter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerEnderConverter extends ContainerDeadCraft{
	private TileEntityEnderConverter te;
	
	public ContainerEnderConverter(InventoryPlayer inv, TileEntityEnderConverter te) {
		super(inv, te);
		this.addSlotToContainer(new Slot((IInventory) te, 1, 80,61));
		for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv,i, 8 + i * 18, 142));
        }
		this.te = te;
	}
	
	
	//FIXME dupe bug
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		ItemStack re = null;
    	Slot slot = getSlot(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			re = stack.copy();
			if(DeadCraft.powerManager.isFuel(stack)) {
				if(!mergeItemStack(stack, 0, 1, false))
					return null;
				else {
					if(i >= 27 ){
						if (!mergeItemStack(stack, 1,28, false)) { 
								return null;
							}
					}else if (i < 27 && i> 0){
						if(!mergeItemStack(stack, 28, 37, false))
							return null;
					}
				}
			}
			if(i >= 27 ){
				if (!mergeItemStack(stack, 1,28, false)) { 
						return null;
					}
			}else if (i < 27 && i> 0){
				if(!mergeItemStack(stack, 28, 37, false))
					return null;
			}
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			slot.onPickupFromSlot(par1EntityPlayer, stack);
		}
    	return re;
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
