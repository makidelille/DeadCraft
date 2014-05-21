package mak.dc.client.gui.container;

import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ContainerDeadCraft extends Container {

    private TileEntityDeadCraft te;

    public ContainerDeadCraft(InventoryPlayer inv, TileEntityDeadCraft te){
    	this(inv,te,true);
    }
    
    public ContainerDeadCraft (InventoryPlayer invPlayer, TileEntityDeadCraft te , boolean custom) {
        this.te = te;
        if(!custom) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new Slot(invPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
                }
            }

            for (int i = 0; i < 9; ++i) {
                this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
            }
        }

    }
 
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
    	ItemStack re = null;
    	Slot slot = getSlot(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			re = stack.copy();
			if( i >= 27 ){
				if (!mergeItemStack(stack, 0,27, false)) {
						return null;
					}
			}else if (i < 27){
				if(!mergeItemStack(stack, 27, 36, false))
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
    public boolean canInteractWith (EntityPlayer entityplayer) {
        if(!te.isUserAllowed(entityplayer.getCommandSenderName())) entityplayer.addChatComponentMessage( new ChatComponentText("the block is locked"));
        return te == null ? true : te.isUserAllowed(entityplayer.getCommandSenderName())
                && entityplayer.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) <= 64;
    }
    
    @Override
    public void detectAndSendChanges () {
        super.detectAndSendChanges();
    }
    
    @Override
    public void addCraftingToCrafters (ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
   
    }
    
     public TileEntityDeadCraft getTileEntity() {
        return te;
    }

}
