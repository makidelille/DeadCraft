package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotGodBottler;
import mak.dc.items.ItemGodCan;
import mak.dc.items.ItemCrystal;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGodBottler extends ContainerDeadCraft{

	private TileEntityGodBottler te;
	private InventoryPlayer invPlayer;
	
	public ContainerGodBottler(InventoryPlayer inventory,TileEntityGodBottler te2) {
		super(inventory,te2,true);
		this.te = te2;
		this.invPlayer = inventory;

		this.addSlotToContainer(new SlotGodBottler(te, 0, 17,62));
		this.addSlotToContainer(new SlotGodBottler(te, 1, 65, 6));
		this.addSlotToContainer(new SlotGodBottler(te, 2, 99, 63));
		for (int i= 0; i < 2; i++)
			for (int j = 0; j < 3 ; j++)
				this.addSlotToContainer(new SlotGodBottler(te, 3+ i *3 + j, 115 + j*18, 6 + i*18));
		
		
		for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
        
        
        
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			
			if (i < te.getSizeInventory() - 1) {
				if (!mergeItemStack(stack, 9, 44, false)) {
					return null;
				}
			}else{
				if(stack.getItem() instanceof ItemCrystal) {
					if(!mergeItemStack(stack, 0, 2, false)) {
						return null;
					}
				}if(stack.getItem() instanceof ItemGodCan ) {
					if(!mergeItemStack(stack, 1, 2, false)) {
						return null;
					}
				}else{
					if(!mergeItemStack(stack, 3, 9, false)){
						return null;
					}
				}
			}
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
		}
		
		
		return null;
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
			((ICrafting)player).sendProgressBarUpdate(this, 0, te.getWorkedTime());
			((ICrafting)player).sendProgressBarUpdate(this, 1, te.getPower());		
		}	
	}
	
	@Override
	public void updateProgressBar(int id, int data) {
		//super.updateProgressBar(id, data);
		switch(id) {
		case 0 :
			te.setWorkedTime(data);
			break;
		case 1:
			te.setPower(data);
			break;
		}
	}
	
	
	
	
}
