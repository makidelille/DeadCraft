package mak.dc.blocks;

import mak.dc.lib.BlockInfo;
import mak.dc.lib.TileEntitiesInfo;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class DeadCraftBlocks {
	
	public static Block eggSpawner;
	public static Block enderPearlBlock;

	public static void init() {
		
		eggSpawner = new BlockEggSpawner();
		enderPearlBlock = new BlockEnderPearlBlock();
		
		GameRegistry.registerBlock(eggSpawner, BlockInfo.EGGSPAWNER_KEY);
		GameRegistry.registerBlock(enderPearlBlock, BlockInfo.ENDERPEARLBLOCK_KEY);
	}
	
	
	
	
	public static void initTileEntity() {
		GameRegistry.registerTileEntity(TileEntityEggSpawner.class, TileEntitiesInfo.EGGSPANWER_TILE_KEY);
	}
	
	
	
	public static void registerCraftRecipe() {
		/**Shapeless Crafting*/
		
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian, 1), new Object[] {
			Items.lava_bucket,Items.water_bucket
		});
		GameRegistry.addShapelessRecipe(new ItemStack(enderPearlBlock, 1,0), new Object[] {Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Blocks.stone});
		
		
		/**Shaped Crafting*/
		
		GameRegistry.addRecipe(new ItemStack(eggSpawner, 1), new Object[] {
										"EXE","XAX","EXE",
											Character.valueOf('E'), Blocks.obsidian,
											Character.valueOf('A'),Items.nether_star,
											Character.valueOf('X'), enderPearlBlock
		});
		
	}

}
