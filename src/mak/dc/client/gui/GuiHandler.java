package mak.dc.client.gui;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerCompressor;
import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.container.ContainerEggSpawner;
import mak.dc.client.gui.container.ContainerEnderConverter;
import mak.dc.client.gui.container.ContainerGodBottler;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.tileEntities.TileEntityDeadCraft;
import mak.dc.common.tileEntities.TileEntityEggSpawner;
import mak.dc.common.tileEntities.TileEntityEnderConverter;
import mak.dc.common.tileEntities.TileEntityGodBottler;
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
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        switch (ID) {
            case 0:
                if (te != null) return new GuiDeadCraftBlockMain(player.inventory, (TileEntityDeadCraft) te, ID);
                break;
            case 1:
                if (te != null) return new GuiEggSpawner(player.inventory, (TileEntityEggSpawner) te, ID);
                break;
            case 2:
                if (te != null && !((TileEntityGodBottler) te).isTop()) return new GuiGodBottler(player.inventory, (TileEntityGodBottler) te, ID);
                break;
            case 3:
                if (te != null) return new GuiEnderConverter(player.inventory, (TileEntityEnderConverter) te, ID);
                break;
            case 4:
                if (te != null) return new GuiCompressor(player.inventory, (TileEntityCompressor) te, ID);
                break;
        
        }
        
        return null;
    }
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = null;
        switch (ID) {
            case 0:
                te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityDeadCraft) return new ContainerDeadCraft(player.inventory, (TileEntityDeadCraft) te, false);
                break;
            case 1:
                te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityEggSpawner) return new ContainerEggSpawner(player.inventory, (TileEntityEggSpawner) te);
                break;
            case 2:
                te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityGodBottler) return new ContainerGodBottler(player.inventory, (TileEntityGodBottler) te);
                break;
            case 3:
                te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityEnderConverter) return new ContainerEnderConverter(player.inventory, (TileEntityEnderConverter) te);
                break;
            case 4:
                te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityCompressor) return new ContainerCompressor(player.inventory, (TileEntityCompressor) te);
                break;
        }
        
        return null;
    }
    
}
