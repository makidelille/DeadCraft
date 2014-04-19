package mak.dc.blocks;

import java.util.Random;

import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockGodBottler extends BlockDeadCraft {

	
	
	public BlockGodBottler() {
		super(Material.iron);
		this.setBlockName(IBTInfos.BLOCK_BOTTLER_UNLOCALIZED_NAME);
		this.setBlockTextureName(Textures.NULL_BI);
		
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
		if(!(world.isRemote))	{
			world.setBlock(x, y+1, z, this,5,3);
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			if(te instanceof TileEntityGodBottler) {
					byte face = (byte) (MathHelper.floor_double((double)(ent.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
					te.setFacing(face);
			}
			TileEntityGodBottler teTop = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
			super.onBlockPlacedBy(world, te.xCoord, te.yCoord, te.zCoord, ent, is);
			teTop.setup(te);
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
		System.out.println("-----");
		if(!world.isRemote) {
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			int face = (MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
			System.out.println(face);
			te.setFacing(face);
			System.out.println(te.getFacing());
		}
		TileEntityGodBottler teCl = (TileEntityGodBottler) world.getTileEntity(x, y, z);
		System.out.println(teCl.getFacing());
		
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y,	int z, Block block, int meta) {
		if(!world.isRemote) {
			TileEntityGodBottler teTop;
			TileEntityGodBottler teBot = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			if(teBot.isTop()) {
				teTop = teBot;
				teBot =  (TileEntityGodBottler) world.getTileEntity(x, y-1, z);
			}
			else teTop = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
			if(teTop != null) {
				this.dropBlockAsItem(world, teTop.xCoord,teTop.yCoord, teTop.zCoord, new ItemStack(block,0,0));
				world.setBlockToAir(teTop.xCoord,teTop.yCoord, teTop.zCoord);
			}
			if(teBot != null) {
				this.dropBlockAsItem(world, teBot.xCoord,teBot.yCoord, teBot.zCoord, new ItemStack(block));
			}
			
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
