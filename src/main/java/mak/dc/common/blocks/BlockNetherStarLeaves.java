package mak.dc.common.blocks;

import java.util.Random;

import mak.dc.DeadCraft;
import mak.dc.common.items.DeadCraftItems;
import net.minecraft.block.BlockLeaves;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockNetherStarLeaves extends BlockLeaves {

	public BlockNetherStarLeaves() {
		super();
		this.setCreativeTab(DeadCraft.tabDeadCraft);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random ran) {
		if(!world.isRemote) {
			int meta = world.getBlockMetadata(x, y, z);
			if(ran.nextInt(100) <= 50 && meta < 10){
				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 1|2);
			}else if(ran.nextInt(100) <= 25 && meta >= 10 && meta < 15) {
				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 1|2);
			}
		}
	}
	
	@Override
	public Item getItemDropped(int meta, Random ran, int fortune) {
		if(meta >= 10 && ran.nextInt(100) <= 90 - (meta-10) * 10){
			return DeadCraftItems.netherStarNugget;
		}else{
			return Items.apple;
		}
	}
	
	
	
	//TODO add the grows of the nuggets
	
	@Override
	public IIcon getIcon(int var1, int var2) {
		//TODO texture
		return null;
	}

	@Override
	public String[] func_150125_e() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
