package mak.dc.blocks;

import mak.dc.items.DeadCraftItems;
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
	/** tileEntities block */
	public static BlockDeadCraft eggSpawner;
	public static BlockDeadCraft godBottler;
	
	/** w/ tileEntities Blocks */
	public static Block enderPearlBlock;

	public static void init() {
		
		
		BlockDeadCraft.init();
		
		eggSpawner = new BlockEggSpawner().setBlockName(IBTInfos.BLOCK_EGGSPAWNER_KEY);
		enderPearlBlock = new BlockEnderPearlBlock().setBlockName(IBTInfos.BLOCK_ENDERPEARLBLOCK_KEY);
		godBottler = new BlockGodBottler().setBlockName(IBTInfos.BLOCK_BOTTLER_KEY);
		
		eggSpawner.setid(BlockDeadCraft.getNextId());
		godBottler.setid(BlockDeadCraft.getNextId());
		
		
		GameRegistry.registerBlock(eggSpawner, ItemBlockDeadCraft.class, IBTInfos.BLOCK_EGGSPAWNER_KEY);
		GameRegistry.registerBlock(godBottler, ItemBlockDeadCraft.class, IBTInfos.BLOCK_BOTTLER_KEY);
		
		GameRegistry.registerBlock(enderPearlBlock, IBTInfos.BLOCK_ENDERPEARLBLOCK_KEY);
		


	}
	
	
	
	
	public static void initTileEntity() {
		GameRegistry.registerTileEntity(TileEntityEggSpawner.class, IBTInfos.TILE_EGGSPANWER_KEY);
		GameRegistry.registerTileEntity(TileEntityGodBottler.class, IBTInfos.TILE_BOTTLER_KEY);
	}
	
	
	
	public static void registerCraftRecipe() {		
		/**Shapeless Crafting*/
		
		GameRegistry.addShapelessRecipe(new ItemStack(enderPearlBlock, 1,0), new Object[] {Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Items.ender_pearl,Blocks.stone});
		
		
		/**Shaped Crafting*/
		
		GameRegistry.addRecipe(new ItemStack(eggSpawner, 1), new Object[] {
										"EXE","XAX","EXE",
											Character.valueOf('E'), Blocks.obsidian,
											Character.valueOf('A'),Items.nether_star,
											Character.valueOf('X'), enderPearlBlock
		});
		GameRegistry.addRecipe(new ItemStack(godBottler, 1), new Object[] {
			"INI",
			"IDI",
			"PCP",
			Character.valueOf('I'), Items.iron_ingot,
			Character.valueOf('N'), Items.nether_star,
			Character.valueOf('C'), DeadCraftItems.crystal,
			Character.valueOf('P'), Blocks.piston,
			Character.valueOf('D'), Items.diamond
		
		
		});
		
	}

}
