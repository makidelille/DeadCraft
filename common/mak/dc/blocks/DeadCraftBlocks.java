package mak.dc.blocks;

import java.util.ArrayList;

import mak.dc.items.itemBlock.ItemBlockEggSpawner;
import mak.dc.items.itemBlock.ItemBlockGodBottler;
import mak.dc.lib.IBTInfos;
import mak.dc.tileEntities.TileEntityEggSpawner;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class DeadCraftBlocks {
	
	public static BlockDeadCraft eggSpawner;
	public static BlockDeadCraft enderPearlBlock;
	public static BlockDeadCraft godBottler;
	public static ArrayList itemBlocks;

	public static void init() {
		
		itemBlocks = new ArrayList<>();
		
		BlockDeadCraft.init();
		
		eggSpawner = new BlockEggSpawner();
		enderPearlBlock = new BlockEnderPearlBlock();
		godBottler = new BlockGodBottler();
		
		eggSpawner.setid(BlockDeadCraft.getNextId());
		godBottler.setid(BlockDeadCraft.getNextId());
		
		
		GameRegistry.registerBlock(eggSpawner, ItemBlockEggSpawner.class, IBTInfos.BLOCK_EGGSPAWNER_KEY);
		GameRegistry.registerBlock(enderPearlBlock, IBTInfos.BLOCK_ENDERPEARLBLOCK_KEY);
		GameRegistry.registerBlock(godBottler, ItemBlockGodBottler.class, IBTInfos.BLOCK_BOTTLER_KEY);
		
		itemBlocks.add(eggSpawner.getId(),new ItemBlockEggSpawner(eggSpawner));
		itemBlocks.add(godBottler.getId(),new ItemBlockGodBottler(godBottler));

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
