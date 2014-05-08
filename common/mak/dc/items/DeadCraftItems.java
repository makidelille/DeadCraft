package mak.dc.items;

import com.ibm.icu.impl.IntTrieBuilder;

import mak.dc.blocks.DeadCraftBlocks;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Lib;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.GameRegistry;

public class DeadCraftItems {

	public static Item deadWand;
	public static Item crystal;
	public static Item mindController;
	public static Item wrench; 
	public static Item godCan;
	public static CreativeTabs tabDeadCraft;
	
	public static void init() {
		
		deadWand = new ItemDeadWand().setUnlocalizedName(IBTInfos.ITEM_DEADWAND_KEY);
		crystal = new ItemCrystal().setUnlocalizedName(IBTInfos.ITEM_CRYSTAL_KEY);
		mindController = new ItemMindController().setUnlocalizedName(IBTInfos.ITEM_MINDCONTROLLER_KEY);
		wrench = new ItemWrench().setUnlocalizedName(IBTInfos.ITEM_WRENCH_KEY);
		godCan = new ItemGodCan().setUnlocalizedName(IBTInfos.ITEM_GODCAN_KEY);
		
		GameRegistry.registerItem(deadWand, IBTInfos.ITEM_DEADWAND_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(crystal, IBTInfos.ITEM_CRYSTAL_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(mindController, IBTInfos.ITEM_MINDCONTROLLER_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(wrench, IBTInfos.ITEM_WRENCH_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(godCan, IBTInfos.ITEM_GODCAN_KEY, Lib.MOD_ID);
	}
	
	public static void postInit() {
		tabDeadCraft = new CreativeTabs("DeadCraft") {

			@Override
			public Item getTabIconItem() {
				return deadWand;
			}};
			
		deadWand.setCreativeTab(tabDeadCraft);
		crystal.setCreativeTab(tabDeadCraft);
		mindController.setCreativeTab(tabDeadCraft);
		wrench.setCreativeTab(tabDeadCraft);
		godCan.setCreativeTab(tabDeadCraft);

		
		DeadCraftBlocks.eggSpawner.setCreativeTab(tabDeadCraft);
		DeadCraftBlocks.enderPearlBlock.setCreativeTab(tabDeadCraft);
		DeadCraftBlocks.godBottler.setCreativeTab(tabDeadCraft);
	}

	public static void registerCraftRecipe() {
		ItemStack is; 
    	NBTTagCompound tag; 
		/**
		 * craft of the crystal
		 */
    	is = new ItemStack(crystal);
    	tag = new NBTTagCompound();
		tag.setBoolean("creativeSpawn", false);
    	tag.setInteger("charge", 0);
    	is.setTagCompound(tag);
		CraftingManager.getInstance().addRecipe(is, new Object[]{
				" R ",
				"RNR",
				" R ",
				Character.valueOf('R'), Items.redstone,
				Character.valueOf('N'), Items.nether_star
		});
		
		/**
		 * craft of wrench
		 */
		is = new ItemStack(wrench);
		CraftingManager.getInstance().addRecipe(is, new Object[]{
				" T ",
				" TT",
				"B  ",
				Character.valueOf('T'), Blocks.redstone_torch,
				Character.valueOf('B'), Items.blaze_rod
		});
		/**
		 * craft of godcan
		 */
		is = new ItemStack(godCan);
		CraftingManager.getInstance().addRecipe(is, new Object[] {
				"III",
				"INI",
				" B ",
				Character.valueOf('I'), Items.iron_ingot,
				Character.valueOf('N'), Items.nether_star,
				Character.valueOf('B'), Items.bucket,
				Character.valueOf('R'), Items.redstone
		});
		/**
		 * craft of mindcontroller
		 */
		is = new ItemStack(mindController);
		CraftingManager.getInstance().addRecipe(is, new Object[] {
				" T ",
				"ICI",
				"II ",
				Character.valueOf('I'), Items.iron_ingot,
				Character.valueOf('T'), Blocks.redstone_torch,
				Character.valueOf('C'), crystal
		});
		/**
		 * craft of deadwand
		 */
		is = new ItemStack(deadWand);
		tag = new NBTTagCompound();
		tag.setInteger("charge", 0);
    	is.setTagCompound(tag);
		CraftingManager.getInstance().addRecipe(is, new Object[] {
			" RN",
			" GR",
			"D  ",
			Character.valueOf('R'), Items.redstone,
			Character.valueOf('N'), Items.nether_star,
			Character.valueOf('D'), Items.diamond			
		});

	}

}
