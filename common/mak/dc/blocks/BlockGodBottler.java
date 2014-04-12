package mak.dc.blocks;

import java.util.List;

import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGodBottler extends BlockDeadCraft {

	
	public BlockGodBottler() {
		super(Material.iron);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGodBottler(var2 == 1);
	}
	
	@Override
	public boolean canBePlaced(World world,int x, int y, int z) {
		return (world.isAirBlock(x, y, z) && world.isAirBlock(x, y+1, z));
	}
		
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAcces,int x, int y, int z) {
		this.setBlockBounds(0, 0, 0, 1F, 1F, 1F);	}
	
	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0f, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void addCollisionBoxesToList(World world, int x,	int y, int z, AxisAlignedBB mask, List list, Entity colidingEntity) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, colidingEntity);
	}
	
	@Override
	public int damageDropped(int p_149692_1_) {
		return 0;
	}
	
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, int x,int y, int z) {
		if(!world.isRemote) {
			if(world.getBlockMetadata(x, y, z) == 0) {
				world.setBlock(x, y+1, z, this,1 ,3); //TODO
			}
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
			if(te instanceof TileEntityGodBottler) te.setTop();
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x,int y, int z, Block block) {
		if(!(block instanceof BlockGodBottler)) return;
		if((world.getBlock(x, y+1, z) instanceof BlockAir)) {
			this.breakBlock(world, x, y, z, this, world.getBlockMetadata(x, y, z));
			return;
		}
		if((world.getBlock(x, y-1, z) instanceof BlockAir) && world.getBlockMetadata(x, y, z) == 1) {
			world.setBlockToAir(x, y, z);
			return;
		}
		if (!(world.getBlock(x, y+1, z) instanceof BlockGodBottler)) return;
		TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
		
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
