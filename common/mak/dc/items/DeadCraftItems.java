package mak.dc.items;

import mak.dc.blocks.DeadCraftBlocks;
import mak.dc.lib.ItemInfo;
import mak.dc.lib.Lib;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class DeadCraftItems {

	public static Item deadWand;
	public static Item lifeCrystal;
	public static Item mindController;
	public static Item controller; //TODO finish
	public static Item superChargedNetherStar; //TODO craft usage
	public static CreativeTabs tabDeadCraft;
	
	public static void init() {
		
		deadWand = new ItemDeadWand();
		lifeCrystal = new ItemLifeCrystal();
		mindController = new ItemMindController();
		controller = new ItemController();
		
		GameRegistry.registerItem(deadWand, ItemInfo.DEADWAND_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(lifeCrystal, ItemInfo.LIFECRYSTAL_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(mindController, ItemInfo.MINDCONTROLLER_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(controller, ItemInfo.CONTROLLER_KEY, Lib.MOD_ID);
	}
	
	public static void postInit() {
		tabDeadCraft = new CreativeTabs("DeadCraft") {

			@Override
			public Item getTabIconItem() {
				return deadWand;
			}};
			
		deadWand.setCreativeTab(tabDeadCraft);
		lifeCrystal.setCreativeTab(tabDeadCraft);
		mindController.setCreativeTab(tabDeadCraft);
		controller.setCreativeTab(tabDeadCraft);

		
		DeadCraftBlocks.eggSpawner.setCreativeTab(tabDeadCraft);
		DeadCraftBlocks.enderPearlBlock.setCreativeTab(tabDeadCraft);
	}

	public static void registerCraftRecipe() {
		// TODO Auto-generated method stub
		
	}

}
