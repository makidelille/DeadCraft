package mak.dc.common.blocks;

import mak.dc.DeadCraft;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherStarWood extends BlockLog {

	private IIcon iconTop;
	private IIcon iconSide;

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		iconTop = iconRegister
				.registerIcon(Textures.NETHERWOOD_LOG_TOP_TEXT_LOC);
		iconSide = iconRegister
				.registerIcon(Textures.NETHERWOOD_LOG_SIDE_TEXT_LOC);
	}
	
	
	@Override
	protected IIcon getSideIcon(int var1) {
		return iconSide;
	}

	@Override
	protected IIcon getTopIcon(int p_150161_1_) {
		return iconTop;
	}

}
