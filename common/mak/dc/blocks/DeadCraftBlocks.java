package mak.dc.blocks;

import mak.dc.items.itemBlock.ItemBlockDeadCraft;
import mak.dc.lib.IBTInfos;
import mak.dc.tileEntities.TileEntityEggSpawner;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class DeadCraftBlocks {
	
	public static Block eggSpawner;
	public static Block enderPearlBlock;
	public static Block godBottler;

	public static void init() {
		
		eggSpawner = new BlockEggSpawner();
		enderPearlBlock = new BlockEnderPearlBlock();
		godBottler = new BlockGodBottler();
		
		
		GameRegistry.registerBlock(eggSpawner, ItemBlockDeadCraft.class, IBTInfos.BLOCK_EGGSPAWNER_KEY);
		GameRegistry.registerBlock(enderPearlBlock, IBTInfos.BLOCK_ENDERPEARLBLOCK_KEY);
		GameRegistry.registerBlock(godBottler, ItemBlockDeadCraft.class, IBTInfos.BLOCK_BOTTLER_KEY);

	}
	
	
	
	
	public static void initTileEntity() {
		GameRegistry.registerTileEntity(TileEntityEggSpawner.class, IBTInfos.TILE_EGGSPANWER_KEY);
		GameRegistry.registerTileEntity(TileEntityGodBottler.class, IBTInfos.TILE_BOTTLER_KEY);
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
