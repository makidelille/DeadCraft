package mak.dc.init;

import mak.dc.blocks.BlockEggSpawner;
import mak.dc.blocks.BlockEnderPearlBlock;
import mak.dc.lib.BlockInfo;
import mak.dc.lib.TileEntitiesInfo;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockInit {

	public static Block eggSpawner;
	public static Block enderPearlBlock;

	public static void init() {
		
		eggSpawner = new BlockEggSpawner(BlockInfo.EGGSPAWNER_ID);
		enderPearlBlock = new BlockEnderPearlBlock(BlockInfo.ENDERPEARLBLOCK_ID);
		
		GameRegistry.registerBlock(eggSpawner, BlockInfo.EGGSPAWNER_KEY);
		GameRegistry.registerBlock(enderPearlBlock, BlockInfo.ENDERPEARLBLOCK_KEY);
	}
	
	
	public static void initTileEntity() {
		GameRegistry.registerTileEntity(TileEntityEggSpawner.class, TileEntitiesInfo.EGGSPANWER_TILE_KEY);
	}
	
	public static void registerCraftRecipe() {
		/**Shapeless Crafting*/
		
		GameRegistry.addShapelessRecipe(new ItemStack(Block.obsidian, 1), new Object[] {
			Item.bucketLava,Item.bucketWater
		});
		GameRegistry.addShapelessRecipe(new ItemStack(enderPearlBlock, 1,0), new Object[] {Item.enderPearl,Item.enderPearl,Item.enderPearl,Item.enderPearl,Item.enderPearl,Item.enderPearl,Item.enderPearl,Item.enderPearl,Block.stone});
		
		
		/**Shaped Crafting*/
		
		GameRegistry.addRecipe(new ItemStack(eggSpawner, 1), new Object[] {
										"EXE","XAX","EXE",
											Character.valueOf('E'), Block.obsidian,
											Character.valueOf('A'),Item.netherStar,
											Character.valueOf('X'), enderPearlBlock
		});
		
	}
}
