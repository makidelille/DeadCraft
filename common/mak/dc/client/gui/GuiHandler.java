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
        NetworkRegistry.instance().registerGuiHandler(DeadCraft.instance, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = null;
        switch(ID) {
            case 0 : 
                te = world.getBlockTileEntity(x, y, z);
                if(te != null && te instanceof TileEntityDeadCraft) 
                    return new ContainerDeadCraft(player.inventory , (TileEntityDeadCraft) te, false);
                break;
            case 1 :
                te = world.getBlockTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityEggSpawner)
                    return new ContainerEggSpawner(player.inventory, (TileEntityEggSpawner) te);
                break;

        }

        return null;
    }

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = null;
        switch (ID) {
            case 0 : 
                te = world.getBlockTileEntity(x, y, z);
                if(te != null && te instanceof TileEntityDeadCraft) 
                    return new GuiDeadCraftBlockMain(player.inventory , (TileEntityDeadCraft) te,ID);
                break;
            case 1:
                te = world.getBlockTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityEggSpawner)
                    return new GuiEggSpawner(player.inventory, (TileEntityEggSpawner) te,ID);
                break;

        }

        return null;
    }

}
