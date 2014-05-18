package mak.dc.blocks;

import java.util.Random;

import mak.dc.DeadCraft;
import mak.dc.items.ItemWrench;
import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityEnderConverter;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public class BlockEnderConverter extends BlockDeadCraft{

	public static int renderPass;

	public BlockEnderConverter() {
		super(Material.iron);
		this.setBlockTextureName("stone");
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
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			if(!super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)){
				if(player != null  && world.getTileEntity(x, y, z) instanceof TileEntityEnderConverter && side != 0 && side != 1) {
					ItemStack is = player.inventory.getCurrentItem();
					TileEntityEnderConverter te = (TileEntityEnderConverter) world.getTileEntity(x, y, z);
					if(te.getStackInSlot(0) == null && is != null){
						te.setInventorySlotContents(0, is);
						player.setCurrentItemOrArmor(0, null);
						return true;
					}
				}
				if(player != null){
					FMLNetworkHandler.openGui(player, DeadCraft.instance, 3, world, x, y, z);
					return true;
				}
			}
			
		}
		return true;
	}
	@Override
	public void onWrenched(World world, int x, int y, int z,EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack wrench = player.inventory.getCurrentItem();
		if(wrench == null) return; //should never execute
		ItemWrench.saveBlockCoord(wrench, x, y, z);
	}
	
	
	@Override
	public void randomDisplayTick(World world, int x,int y, int z, Random ran) {
		TileEntityEnderConverter te = (TileEntityEnderConverter) world.getTileEntity(x, y, z);
		if(te != null && te.getStackInSlot(0) != null) {
			for (int i = 0; i < 250; i++) {
				double vx;
				double vz;
				double coef = ran.nextDouble();
				if(ran.nextBoolean()) {
					 vx = ran.nextDouble() - 0.5d;
					 vz =  0;
				}else{
					 vx =  0;
					 vz = ran.nextDouble() - 0.5d;
				}
				EntitySmokeFX fx = new EntitySmokeFX(world, x +0.5, y +0.4 , z + 0.5, vx * coef, 0, vz * coef,0.5f);
	
				fx.setRBGColorF(0.8f + ran.nextFloat()/10, 1f, 0.8f + ran.nextFloat()/10);
				Minecraft.getMinecraft().effectRenderer.addEffect(fx);		
			}
		}
		
}

}
