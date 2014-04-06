package mak.dc.blocks;

import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGodBottler extends BlockDeadCraft {

	
	public BlockGodBottler() {
		super(Material.iron);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGodBottler();
	}
}
