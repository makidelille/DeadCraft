package mak.dc.blocks;

import java.util.Random;

import mak.dc.DeadCraft;
import mak.dc.lib.IBTInfos;
import mak.dc.network.DeadCraftGodBottlerPacket;
import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGodBottler extends BlockDeadCraft {

	
	
	public BlockGodBottler() {
		super(Material.iron);
		this.setBlockName(IBTInfos.BLOCK_BOTTLER_UNLOCALIZED_NAME);
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGodBottler();
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
		world.setBlock(x, y+1, z, this);
		TileEntityGodBottler teBot = (TileEntityGodBottler) world.getTileEntity(x, y, z);
		if(teBot instanceof TileEntityGodBottler) {
				
		}
		TileEntityGodBottler teTop = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
		super.onBlockPlacedBy(world, teBot.xCoord, teBot.yCoord, teBot.zCoord, ent, is);
		teTop.setup(teBot);
		teBot.setPair(teTop);
		
		
		//TODO Client part
		TileEntityGodBottler teBotCl = (TileEntityGodBottler) world.getTileEntity(x, y, z);
		TileEntityGodBottler teTopCl = (TileEntityGodBottler) world.getTileEntity(x, y+1, z);
		if(teBotCl instanceof TileEntityGodBottler && teTopCl instanceof TileEntityGodBottler) {
			byte face = (byte) (MathHelper.floor_double((double)(ent.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
			teBotCl.setDirection(face);
			teTopCl.clientSetup(teBotCl);
			world.markBlockForUpdate(teBotCl.xCoord, teBotCl.yCoord, teBotCl.zCoord);
			world.markBlockForUpdate(teTopCl.xCoord, teTopCl.yCoord, teTopCl.zCoord);
		}
		
		

	}

	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			int face = (MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
			System.out.println(face);
			te.setDirection(face);
			System.out.println(te.getDirection());
		}
		TileEntityGodBottler teCl = (TileEntityGodBottler) world.getTileEntity(x, y, z);
		System.out.println(teCl.getDirection());
		
		return false;
	}
	
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,int meta) {
		if(!world.isRemote) {
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			if(te == null || !(te instanceof TileEntityGodBottler)) {
				return;
			}
			if(te.isTop()) te = (TileEntityGodBottler) world.getTileEntity(x, y-1, z);
			if(te != null && te instanceof TileEntityGodBottler) {
				world.removeTileEntity(te.xCoord,te.yCoord, te.zCoord);
				world.removeTileEntity(te.xCoord,te.yCoord + 1, te.zCoord);
				world.setBlockToAir(te.xCoord,te.yCoord, te.zCoord);
				world.setBlockToAir(te.xCoord,te.yCoord + 1, te.zCoord);
				ItemStack is = new ItemStack(block, 1,0);
		    	is.setTagCompound(te.writeNBTData(new NBTTagCompound()));
		    	is.stackSize = 1;
		    	EntityItem entitem = new EntityItem(world, x, y, z, is);
		    	entitem.setEntityItemStack(is);
		    	world.spawnEntityInWorld(entitem);
			}
		    	
			
			
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,int side) {
		return side != ((TileEntityGodBottler) world.getTileEntity(x, y, z)).getDirection() ;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x,int y, int z, Block block){
			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
			if(te != null) {
			if(world.isBlockIndirectlyGettingPowered(x, y, z)) te.setPowered(true);
			else if(!world.isBlockIndirectlyGettingPowered(x, y, z)) te.setPowered(false);
			DeadCraft.packetPipeline.sendToDimension(new DeadCraftGodBottlerPacket(te), world.getWorldInfo().getVanillaDimension());
		}
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public void randomDisplayTick(World world, int x,int y, int z, Random ran) {
		if(((TileEntityGodBottler) world.getTileEntity(x, y, z)).isHasStarted()) {
			boolean flag = (((TileEntityGodBottler) world.getTileEntity(x, y, z)).isTop());
			for (int i = 0; i < 50; i++) {
				float coef = ran.nextFloat() / 10;
				float scale = ran.nextFloat();
				
				int a = ran.nextInt(90);
				int b = ran.nextInt(360);
				
				float vx = (float) (Math.cos(a /(2 * Math.PI)) * Math.cos(b / (2 * Math.PI)) * coef);
				float vy = (float) (Math.sin(a /(2 * Math.PI)) * coef);
				float vz = (float) (Math.cos(a /(2 * Math.PI)) *  Math.sin(b /(2 * Math.PI)) * coef);
				float dx = 0.5f, dy = flag ?-0.35f : 0.65f, dz = 0.5f;
				
				EntityReddustFX fx = new EntityReddustFX(world, x+dx, y+dy + ran.nextFloat() / 10, z+dz, vx, vy, vz);
				fx.setRBGColorF(0.65f + ran.nextFloat() / 10, 0.65f + ran.nextFloat() /10, 1f);
				fx.setVelocity(vx, vy, vz);
				Minecraft.getMinecraft().effectRenderer.addEffect(fx);		
			}
		}
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		return Blocks.stone.getIcon(0, 0);
	}
		
	@Override
	public int getRenderType() {
		return ClientProxy.renderInventoryTESRId;
	}
	
}
