package mak.dc.client.gui.container;

import mak.dc.client.gui.container.slot.SlotEggSpawner;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerEggSpawner extends ContainerDeadCraft {

	private TileEntityEggSpawner te;
	
	public ContainerEggSpawner(InventoryPlayer invPlayer, TileEntityEggSpawner te) {
	    super(invPlayer, te, true);
	    this.te = te;
	
		for (int y = 0; y < 2; y++) {
			for(int x = 0 ; x < 3; x++) {
				addSlotToContainer(new SlotEggSpawner(te , x + (y * 3), 65 + (18 * x), 18 + (y * 18)));
			}
		}
		
		for (int x = 0; x < 2; x++)
			addSlotToContainer(new SlotEggSpawner(te, 6 + x, 20 + x * 18, 84));		
		
		
		for (int x = 0; x < 9; x++)
			addSlotToContainer(new Slot(invPlayer, x, 11 + (18 * x), 165));
		
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 9; x++)
				addSlotToContainer(new Slot(invPlayer, x + (y * 9) + 9, 11 + (18 * x), 107 + (y * 18)));
		
		
		
		
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
			ItemStack result = stack.copy();
			
			if (i < te.getSizeInventory() - 1) {
				if (!mergeItemStack(stack, 8, 44, false)) {
					return null;
				}
			}else {
				int pSlot = ValidForASlot(stack);
				
				switch (pSlot) {
				case -1 : 
					return null;
				case 0 : 
					if(!mergeItemStack(stack,pSlot, pSlot +1,false) && !mergeItemStack(stack, pSlot +2 , pSlot +3, false))  {
						return null;	
					}
					break;
				case 3:
					if(!mergeItemStack(stack,pSlot, pSlot +1,false) && !mergeItemStack(stack, pSlot +2 , pSlot +3, false))  {
						return null;	
					}
					break;
				case 1 : 
					if(!mergeItemStack(stack, pSlot , pSlot + 1 , false)) {
						return null;
					}
					break;
				case 4 : 
					if(!mergeItemStack(stack, pSlot , pSlot + 1 , false)) {
						return null;
					}
					break;
				case 6 :
					if (!mergeItemStack(stack, pSlot , pSlot + 2 , false)) {
						return null;
					}
					break;
				
				}
	
				
			}
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			slot.onPickupFromSlot(player, stack);
			
			return result;
		}
		
		return null;
	}
	
	private int ValidForASlot(ItemStack stack) {
		for (int i=0; i< te.getSizeInventory(); i++) {
			if(te.isItemValidForSlot(i, stack)) {
				return i;
				}
		}
		
		return -1;
	}

	@Override
    public void addCraftingToCrafters(ICrafting player) {
		super.addCraftingToCrafters(player);
		
		player.sendProgressBarUpdate(this, 0, te.getBuildTime());
		player.sendProgressBarUpdate(this, 1, te.getEggInStock());
		player.sendProgressBarUpdate(this, 2, te.getStarted());
		player.sendProgressBarUpdate(this, 3, te.getRedstoneState());
		player.sendProgressBarUpdate(this, 4, te.getMode());
		for(int i = 0; i < 6; i++) {
			player.sendSlotContents(this, i, te.getStackInSlot(i));
		}
    }
	
	@Override
    public void detectAndSendChanges() {
		super.detectAndSendChanges();
    	
		for (Object player : crafters) {
			((ICrafting)player).sendProgressBarUpdate(this, 0, te.getBuildTime());
			((ICrafting)player).sendProgressBarUpdate(this, 1, te.getEggInStock());
			((ICrafting)player).sendProgressBarUpdate(this, 2, te.getStarted());
			((ICrafting)player).sendProgressBarUpdate(this, 3, te.getRedstoneState());
			((ICrafting)player).sendProgressBarUpdate(this, 4, te.getMode());
			for(int i = 0; i < 6; i++) {
				((ICrafting)player).sendSlotContents(this, i, te.getStackInSlot(i));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void updateProgressBar(int id, int data) {
    	switch(id) {
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
    	case 4 :
    		te.setMode((byte) data);
    		break;
    	}
    }




}
