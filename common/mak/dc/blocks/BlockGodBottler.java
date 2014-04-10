package mak.dc.blocks;

import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGodBottler extends BlockDeadCraft {

	
	public BlockGodBottler() {
		super(Material.iron);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGodBottler();
	}
	
	@Override
	public boolean canBePlaced(World world,int x, int y, int z) {
		return world.isAirBlock(x, y, z) && world.isAirBlock(x, y+1, z);
	}
		
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAcces,int x, int y, int z) {
		this.setBlockBounds(0, 0, 0, 1, 2, 1);	}
	
	
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, int x,int y, int z) {
		if(!world.isRemote) {
			world.setBlock(x, y+1, z, DeadCraftBlocks.fakeBlock);
		}
	}
	
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.renderGodBottlerTESRId;
	}
}
