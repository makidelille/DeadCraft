package mak.dc.common.items;

import mak.dc.DeadCraft;
import mak.dc.common.blocks.DeadCraftBlocks;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.IBTInfos;
import net.minecraft.block.Block;
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
	public static Item compacted;
	
	public static void init() {

		deadWand = new ItemDeadWand()
				.setUnlocalizedName(IBTInfos.ITEM_DEADWAND_KEY).setCreativeTab(DeadCraft.tabDeadCraft);
		crystal = new ItemCrystal()
				.setUnlocalizedName(IBTInfos.ITEM_CRYSTAL_KEY).setCreativeTab(DeadCraft.tabDeadCraft);
		mindController = new ItemMindController()
				.setUnlocalizedName(IBTInfos.ITEM_MINDCONTROLLER_KEY).setCreativeTab(DeadCraft.tabDeadCraft);
		wrench = new ItemWrench().setUnlocalizedName(IBTInfos.ITEM_WRENCH_KEY).setCreativeTab(DeadCraft.tabDeadCraft);
		godCan = new ItemGodCan().setUnlocalizedName(IBTInfos.ITEM_GODCAN_KEY).setCreativeTab(DeadCraft.tabDeadCraft);
		compacted = new ItemCompacted()
				.setUnlocalizedName(IBTInfos.ITEM_COMPACTED_KEY).setCreativeTab(DeadCraft.tabDeadCraft);
		GameRegistry.registerItem(deadWand, IBTInfos.ITEM_DEADWAND_KEY,
				Lib.MOD_ID);
		GameRegistry.registerItem(crystal, IBTInfos.ITEM_CRYSTAL_KEY,
				Lib.MOD_ID);
		GameRegistry.registerItem(mindController,
				IBTInfos.ITEM_MINDCONTROLLER_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(wrench, IBTInfos.ITEM_WRENCH_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(godCan, IBTInfos.ITEM_GODCAN_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(compacted, IBTInfos.ITEM_COMPACTED_KEY,
				Lib.MOD_ID);
	}

	public static void registerCraftRecipe() {
		ItemStack is;
		NBTTagCompound tag;
		/**
		 * craft of the crystal
		 */
		is = new ItemStack(crystal, 1, 0);
		tag = new NBTTagCompound();
		tag.setBoolean("creativeSpawn", false);
		tag.setInteger("charge", 0);
		is.setTagCompound(tag);
		CraftingManager.getInstance().addRecipe(
				is,
				new Object[] { " R ", "RNR", " R ", Character.valueOf('R'),
						Items.redstone, Character.valueOf('N'),
						Items.nether_star });

		is = new ItemStack(crystal, 1, 1);
		tag = new NBTTagCompound();
		tag.setBoolean("creativeSpawn", false);
		tag.setInteger("charge", 0);
		is.setTagCompound(tag);
		CraftingManager.getInstance().addRecipe(
				is,
				new Object[] { " C ", "CDC", " C ", Character.valueOf('C'),
						new ItemStack(crystal, 1, 0), Character.valueOf('D'),
						Items.diamond });

		is = new ItemStack(crystal, 1, 2);
		tag = new NBTTagCompound();
		tag.setBoolean("creativeSpawn", false);
		tag.setInteger("charge", 0);
		is.setTagCompound(tag);
		CraftingManager.getInstance().addRecipe(
				is,
				new Object[] { "RCR", "CDC", "RCR", Character.valueOf('R'),
						Blocks.redstone_block, Character.valueOf('D'),
						Blocks.diamond_block, Character.valueOf('C'),
						new ItemStack(crystal, 1, 1) });

		/**
		 * craft of wrench
		 */
		is = new ItemStack(wrench);
		CraftingManager.getInstance().addRecipe(
				is,
				new Object[] { " T ", " TT", "B  ", Character.valueOf('T'),
						Blocks.redstone_torch, Character.valueOf('B'),
						Items.blaze_rod });
		/**
		 * craft of godcan
		 */
		is = new ItemStack(godCan);
		CraftingManager.getInstance()
				.addRecipe(
						is,
						new Object[] { "I I", "I I", " B ",
								Character.valueOf('I'), Items.iron_ingot,
								Character.valueOf('B'), Items.bucket });
		/**
		 * craft of Mindcontroller
		 */
		is = new ItemStack(mindController);
		tag = new NBTTagCompound();
		tag.setBoolean("creative", false);
		tag.setInteger("charge", 0);
		is.setTagCompound(tag);
		CraftingManager.getInstance()
				.addRecipe(
						is,
						new Object[] { " T ", "ICI", "II ",
								Character.valueOf('I'), Items.iron_ingot,
								Character.valueOf('T'), Blocks.redstone_torch,
								Character.valueOf('C'), crystal });
		/**
		 * craft of Deadwand
		 */
		is = new ItemStack(deadWand);
		tag = new NBTTagCompound();
		tag.setInteger("charge", 0);
		is.setTagCompound(tag);
		CraftingManager.getInstance().addRecipe(
				is,
				new Object[] { " RN", " GR", "D  ", Character.valueOf('G'),
						Items.blaze_rod, Character.valueOf('R'),
						Items.redstone, Character.valueOf('N'),
						Items.nether_star, Character.valueOf('D'),
						Items.diamond });

	}

}
