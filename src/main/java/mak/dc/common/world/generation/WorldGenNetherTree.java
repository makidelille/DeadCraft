package mak.dc.common.world.generation;

import java.util.Random;

import mak.dc.common.blocks.DeadCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenNetherTree extends WorldGenerator {

	private Block log;
	private Block leave;
	private int minHeight;
	private boolean doVinesGrow;

	public WorldGenNetherTree(){
		this(true,3,DeadCraftBlocks.netherStarLogBlock, DeadCraftBlocks.netherStarLeaveBlock, false);
	}
	
	public WorldGenNetherTree(boolean notify, int minHeight, Block log,
			Block leave, boolean vines) {
		super(notify);
		this.minHeight = minHeight;
		this.log = log;
		this.leave = leave;
		this.doVinesGrow = vines;
	}

	@Override
	public boolean generate(World world, Random ran, int x, int y, int z) {
		int height = ran.nextInt(3) + minHeight; // set the height of the tree
		// check where it's placed
		// is the y coord right : Y € [1;255-height]
		if (y >= 1 && y + height + 1 <= 256) { // the +1if for the leaves on the
												// top
			Block blockToPlace;
			byte size;
			boolean canPlace = true;
			// we check for evry y of the tree
			for (int curY = 0; curY <= height + 1; curY++) {
				size = 1;
				//radius
				if (curY == 0 || curY == height+1) {
					size = 0;
				} else if (curY == height) {
					size = 1;
				} else if (curY == height -1 ){
					size = 2;
				} else if (curY == height -2){
					size = 3;
				}

				for (int curX = x - size; curX <= x + size; curX++) {
					for (int curZ = z - size; curZ <= z + size; curZ++) {
						// we check if we can place block here
						
						
					}
				}

			}

			if (!canPlace)
				return false;
			// we place the blocks here
			
			
			return true;
		}
		return false;
	}

}
