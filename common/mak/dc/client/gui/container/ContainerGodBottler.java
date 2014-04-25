package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotGodBottler;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

public class ContainerGodBottler extends ContainerDeadCraft{

	private TileEntityGodBottler te;
	private InventoryPlayer invPlayer;
	
	public ContainerGodBottler(InventoryPlayer inventory,TileEntityGodBottler te2) {
		super(inventory,te2,true);
		this.te = te2;
		this.invPlayer = inventory;

		this.addSlotToContainer(new SlotGodBottler(te,0, 17,62));
		this.addSlotToContainer(new SlotGodBottler(te, 1, 65, 6));
		this.addSlotToContainer(new SlotGodBottler(te, 2, 99, 63));
		for (int i= 0; i < 2; i++)
			for (int j = 0; j < 3 ; j++)
				this.addSlotToContainer(new SlotGodBottler(te, 3+ i *3 + j, 115 + j*18, 6 + i*18));
		
		
		//player slot
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
	public void addCraftingToCrafters(ICrafting crafter) {
		super.addCraftingToCrafters(crafter);
		crafter.sendProgressBarUpdate(this, 0, te.getWorkedTime());
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (Object player : crafters) {
			((ICrafting)player).sendProgressBarUpdate(this, 0, te.getWorkedTime());
		
		}	
	}
	
	@Override
	public void updateProgressBar(int id, int data) {
		super.updateProgressBar(id, data);
		switch(id) {
		case 0 :
			te.setWorkedTime(data);
		}
	}
	
	
}
