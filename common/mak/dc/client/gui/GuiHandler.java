package mak.dc.client.gui;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.container.ContainerEggSpawner;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(DeadCraft.instance, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = null;
        switch(ID) {
            case 0 : 
                te = world.getTileEntity(x, y, z);
                if(te != null && te instanceof TileEntityDeadCraft) 
                    return new ContainerDeadCraft(player.inventory , (TileEntityDeadCraft) te, false);
                break;
            case 1 :
                te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityEggSpawner)
                    return new ContainerEggSpawner(player.inventory, (TileEntityEggSpawner) te);
                break;

        }

        return null;
    }

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0 : 
                TileEntityDeadCraft te0 = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
                if(te0 != null ) 
                    return new GuiDeadCraftBlockMain(player.inventory , te0,ID);
                break;
            case 1:
                TileEntityEggSpawner te1 = (TileEntityEggSpawner) world.getTileEntity(x, y, z);
                if (te1 != null )
                    return new GuiEggSpawner(player.inventory,  te1,ID);
                break;

        }

        return null;
    }

}
