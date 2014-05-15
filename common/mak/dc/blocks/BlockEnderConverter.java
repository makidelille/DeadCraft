package mak.dc.blocks;

import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityEnderConverter;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnderConverter extends BlockDeadCraft{

	public BlockEnderConverter() {
		super(Material.iron);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityEnderConverter();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.renderInventoryTESRId;
	}

}
