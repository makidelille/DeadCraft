package mak.dc.client.gui;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerEggSpawner;
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
			switch(ID) {
			case 0 :
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te != null && te instanceof TileEntityEggSpawner )
					return new ContainerEggSpawner(player.inventory, (TileEntityEggSpawner)te);
				break;
			
			}
			
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case 0 :
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if(te != null && te instanceof TileEntityEggSpawner)
					return new GuiEggSpawner(player.inventory, (TileEntityEggSpawner)te);
				break;
	
		
		}
		
		return null;
	}

}
