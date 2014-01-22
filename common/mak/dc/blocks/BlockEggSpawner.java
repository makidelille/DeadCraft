package mak.dc.blocks;

//TODO create blockDC et ajouter lock avec nveaux items

import java.util.Random;

import mak.dc.DeadCraft;
import mak.dc.items.ItemController;
import mak.dc.lib.BlockInfo;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEggSpawner extends BlockDeadCraft {

	
	public BlockEggSpawner(int id) {
		super(id, Material.iron);
		this.setResistance(10F);
		this.setStepSound(soundMetalFootstep);
		this.setHardness(3.5F);
		this.setUnlocalizedName(BlockInfo.EGGSPAWNER_UNLOCALIZED_NAME);
		}

	@Override
	public TileEntity createNewTileEntity(World world) {
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
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		if(!world.isRemote) {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof IInventory) {
				IInventory inventory = (IInventory)te;
				
				for (int i = 0; i < inventory.getSizeInventory(); i++) {
					ItemStack stack = inventory.getStackInSlotOnClosing(i);
					
					if (stack != null) {
						float spawnX = x + world.rand.nextFloat();
						float spawnY = y + world.rand.nextFloat();
						float spawnZ = z + world.rand.nextFloat();
						
						EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
						
						float mult = 0.05F;
						
						droppedItem.motionX = (-0.5F + world.rand.nextFloat()) * mult;
						droppedItem.motionY = (4 + world.rand.nextFloat()) * mult;
						droppedItem.motionZ = (-0.5F + world.rand.nextFloat()) * mult;
						
						world.spawnEntityInWorld(droppedItem);
					}}
			}

		
			super.breakBlock(world, x, y, z, id, meta);
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
	public void registerIcons(IconRegister registerIcon) {
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
		float yCoord = (float) (y + rand.nextFloat());
		float zCoord = (float) (z +0.5);
		
		float vecX = (float) 2 * (-0.5F + rand.nextFloat());
		float vecY = (float) (-0.5F + 2 * rand.nextFloat());
		float vecZ = (float) 2* (-0.5F +  rand.nextFloat());

		world.spawnParticle("portal", xCoord, yCoord, zCoord, vecX, vecY, vecZ);
		}
		
	}
	
	
	
	
	

}
