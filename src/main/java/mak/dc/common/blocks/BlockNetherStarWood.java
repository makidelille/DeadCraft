package mak.dc.common.blocks;

import mak.dc.DeadCraft;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockNetherStarWood extends Block {

	private IIcon iconTop;
	private IIcon iconSide;

	protected BlockNetherStarWood() {
		super(Material.wood);
		this.setCreativeTab(DeadCraft.tabDeadCraft);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		iconTop = iconRegister.registerIcon(Textures.NETHERWOOD_LOG_SIDE_TEXT_LOC);
		iconSide = iconRegister.registerIcon(Textures.NETHERWOOD_LOG_SIDE_TEXT_LOC);
	}

	@Override
	public int getRenderType() {
		return 31;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block blo, int meta) {
		byte var7 = 4;
		int var8 = var7 + 1;

		if (world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y
				+ var8, z + var8)) {
			for (int var9 = -var7; var9 <= var7; ++var9) {
				for (int var10 = -var7; var10 <= var7; ++var10) {
					for (int var11 = -var7; var11 <= var7; ++var11) {
						Block block = world.getBlock(x + var9, y + var10, z
								+ var11);

						if (block != null) {
							block.beginLeavesDecay(world, x + var9, y + var10,
									z + var11);
						}
					}
				}
			}
		}
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4,
			int par5, float par6, float par7, float par8, int par9) {
		int var10 = par9 & 3;
		byte var11 = 0;

		switch (par5) {
		case 0:
		case 1:
			var11 = 0;
			break;
		case 2:
		case 3:
			var11 = 8;
			break;
		case 4:
		case 5:
			var11 = 4;
		}

		return var10 | var11;
	}

	public int damageDropped(int par1) {
		return 0;
	}

}
