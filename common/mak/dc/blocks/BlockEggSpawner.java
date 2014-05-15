package mak.dc.blocks;

import java.util.Random;

import mak.dc.DeadCraft;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEggSpawner extends BlockDeadCraft {
	
	public BlockEggSpawner() {
		super(Material.iron);
		this.setResistance(10F);
		this.setStepSound(soundTypeMetal);
		this.setHardness(3.5F);
		}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityEggSpawner();
	}
	
	@Override
	public boolean onBlockActivated(World world,int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ ) {
	    if(!super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ))
	        if(!world.isRemote && player.getCurrentEquippedItem() == null)
	            FMLNetworkHandler.openGui(player, DeadCraft.instance, 1, world, x, y, z);
		return true;		
	}
		

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if(!world.isRemote) {
			TileEntityEggSpawner te = (TileEntityEggSpawner) world.getTileEntity(x, y, z);
			if (te != null && te instanceof IInventory) {
				
				if(te.getEggInStock() > 0) {
					EntityItem eggDropped = new EntityItem(world, x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(), new ItemStack(Blocks.dragon_egg, 0,te.getEggInStock()));
					eggDropped.motionX = (-0.5F + world.rand.nextFloat()) * 0.05F;
					eggDropped.motionY = (4 + world.rand.nextFloat()) * 0.05F;
					eggDropped.motionZ = (-0.5F + world.rand.nextFloat()) * 0.05F;
					world.spawnEntityInWorld(eggDropped);
				}
			}

		
			super.breakBlock(world, x, y, z, block, meta);
		}
	}
	@Override
	public boolean canProvidePower() {
	        return true;
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return world.getBlockMetadata(x, y, z);
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister registerIcon) {
		blockIcon = registerIcon.registerIcon(Textures.EGGSPAWNER_TEXT_LOC);
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	 public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		spawnFx(world,x, y, z);
		
	}
	
	@SideOnly(Side.CLIENT)
	public void spawnFx(World world, int x, int y, int z) {
		
		
		
		Random rand = world.rand;
		
		for (int i = 0; i < 5; i++) {
		
		float xCoord = (float) (x + 0.5);
		float yCoord = y + rand.nextFloat();
		float zCoord = (float) (z +0.5);
		
		float vecX = 2 * (-0.5F + rand.nextFloat());
		float vecY = -0.5F + 2 * rand.nextFloat();
		float vecZ = 2* (-0.5F +  rand.nextFloat());

		world.spawnParticle("portal", xCoord, yCoord, zCoord, vecX, vecY, vecZ);
		}
		
	}
	
	
	
	
	

}
