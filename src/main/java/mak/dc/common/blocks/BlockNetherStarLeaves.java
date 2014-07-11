package mak.dc.common.blocks;

import mak.dc.DeadCraft;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

public class BlockNetherStarLeaves extends BlockLeavesBase {

	public BlockNetherStarLeaves() {
		super(Material.leaves, false);
		this.setCreativeTab(DeadCraft.tabDeadCraft);
	}
	
	@Override
	public IIcon getIcon(int var1, int var2) {
		
		return null;
	}

}
