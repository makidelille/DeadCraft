package mak.dc.items.itemBlock;

import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBlockGodBottler extends ItemBlockDeadCraft {

	public ItemBlockGodBottler(Block block) {
		super(block);
	}
		
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world,int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		super.onItemUse(is, player, world, x, y, z, side, hitX, hitY, hitZ);
//		if(!world.isRemote) {
//			TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
//			if(te instanceof TileEntityGodBottler) {
//				byte face = (byte) (MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
//				te.setFacing(face);
//				world.markBlockForUpdate(x, y, z);
//				TileEntityGodBottler te1 = (TileEntityGodBottler) world.getTileEntity(x, y, z);
//				if(te1 instanceof TileEntityGodBottler) {
//					te1.setPair(te);
//					te1.setTop();
//					world.markBlockForUpdate(x, y+1, z);
//
//					
//			}}
//			return true;
//		}
		return false;
	}
	

}
