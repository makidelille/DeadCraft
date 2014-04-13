package mak.dc.blocks;

import mak.dc.lib.IBTInfos;
import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockGodBottler extends BlockDeadCraft {

	
	public BlockGodBottler() {
		super(Material.iron);
		this.setBlockName(IBTInfos.BLOCK_BOTTLER_UNLOCALIZED_NAME);
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGodBottler((var2 == 1));
	}
	
	@Override
	public boolean canBePlaced(World world,int x, int y, int z) {
		return (world.isAirBlock(x, y, z) && world.isAirBlock(x, y+1, z));
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack is) {
		if(!world.isRemote) {
			System.out.println("test");
			if(world.getBlockMetadata(x, y, z) == 0) {
				world.setBlock(x, y+1, z, this,1 ,3);
			}
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
			if(te instanceof TileEntityGodBottler) {
				te.setTop();
			}
			te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			if(te instanceof TileEntityGodBottler) {
				byte face = (byte) (MathHelper.floor_double((double)(ent.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
				System.out.println(face);
	                //0 = +Z
	                //1 = -X
	                //2 = -Z
	                //3 = +X
				 
				te.setFacing(face);
				System.out.println(te.getFacing());
			}
		}
	}
	
//	@Override
//	public void onBlockAdded(World world, int x,int y, int z) {
//		if(!world.isRemote) {
//			if(world.getBlockMetadata(x, y, z) == 0) {
//				world.setBlock(x, y+1, z, this,1 ,3); //TODO
//			}
//			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
//			if(te instanceof TileEntityGodBottler) te.setTop();
//		}
//	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			te.setFacing((byte) (MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3));
		}
		
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x,int y, int z, Block block) {
		if(!(block instanceof BlockGodBottler)) return;
		if((world.getBlock(x, y+1, z) instanceof BlockAir && world.getBlockMetadata(x, y, z) == 0)) {
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
