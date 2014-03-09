package mak.dc.client.gui.container;

import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.util.IChatComponent;

public class ContainerDeadCraft extends Container {

    private TileEntityDeadCraft te;

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
    public boolean canInteractWith (EntityPlayer entityplayer) {
        if(!te.isUserAllowed(entityplayer.getCommandSenderName())) System.out.println("test"); //TODO entityplayer.addChatMessage("the block is locked");
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
