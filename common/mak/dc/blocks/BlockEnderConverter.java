package mak.dc.blocks;

import java.util.Random;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import mak.dc.DeadCraft;
import mak.dc.network.packet.DeadCraftEnderConverterPacket;
import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityEnderConverter;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnderConverter extends BlockDeadCraft{

	public static int renderPass;

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
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			if(!super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)){
				if(player != null  && world.getTileEntity(x, y, z) instanceof TileEntityEnderConverter && side != 0 && side != 1) {
					if(!player.isSneaking()) {
						ItemStack is = player.inventory.getCurrentItem();
						TileEntityEnderConverter te = (TileEntityEnderConverter) world.getTileEntity(x, y, z);
						if(te.getStackInSlot(0) == null && is != null){
							te.setInventorySlotContents(0, is);
							player.setCurrentItemOrArmor(0, null);
							DeadCraft.packetPipeline.sendToDimension(new DeadCraftEnderConverterPacket(te), world.getWorldInfo().getVanillaDimension());
							return true;
						}else if (te.getStackInSlot(0) != null && is == null){
							world.spawnEntityInWorld(new EntityItem(world, x +0.5d, y +0.5d, z+0.5d, te.getStackInSlot(0)));
							te.setInventorySlotContents(0, null);
							DeadCraft.packetPipeline.sendToDimension(new DeadCraftEnderConverterPacket(te), world.getWorldInfo().getVanillaDimension());
							return true;
						}
					}else{
						FMLNetworkHandler.openGui(player, DeadCraft.instance, 3, world, x, y, z);
					}
				}
			}
			
		}
		return true;
	}
	
	@Override
	public void randomDisplayTick(World world, int x,int y, int z, Random rand) {
		//TODO particles
}

}
