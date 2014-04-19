package mak.dc.client;

import net.minecraft.tileentity.TileEntity;

public interface IRenderer {
	public void renderInventory(double x, double y, double z);
	public void renderBlock(TileEntity tileEntity, int x, int y, int z);

}
