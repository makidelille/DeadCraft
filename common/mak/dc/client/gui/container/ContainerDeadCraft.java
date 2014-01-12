package mak.dc.client.gui.container;

import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerDeadCraft extends Container {

    private TileEntityDeadCraft te;
    
    
    public ContainerDeadCraft (InventoryPlayer invPlayer, TileEntityDeadCraft te) {
        this.te = te;
        
    }
    
    
    @Override
    public boolean canInteractWith (EntityPlayer entityplayer) {
        return te == null ? true : te.isUserAllowed(entityplayer.username) && entityplayer.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) <= 64;
    }
    

}
