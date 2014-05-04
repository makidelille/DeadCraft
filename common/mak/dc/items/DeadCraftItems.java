package mak.dc.items;

import mak.dc.blocks.DeadCraftBlocks;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Lib;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class DeadCraftItems {

	public static Item deadWand;
	public static Item lifeCrystal;
	public static Item mindController;
	public static Item controller; 
	public static Item godCan;
	public static CreativeTabs tabDeadCraft;
	
	public static void init() {
		
		deadWand = new ItemDeadWand();
		lifeCrystal = new ItemLifeCrystal();
		mindController = new ItemMindController();
		controller = new ItemWrench();
		godCan = new ItemGodCan();
		
		GameRegistry.registerItem(deadWand, IBTInfos.ITEM_DEADWAND_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(lifeCrystal, IBTInfos.ITEM_LIFECRYSTAL_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(mindController, IBTInfos.ITEM_MINDCONTROLLER_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(controller, IBTInfos.ITEM_CONTROLLER_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(godCan, IBTInfos.ITEM_GODCAN_KEY, Lib.MOD_ID);
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
		godCan.setCreativeTab(tabDeadCraft);

		
		DeadCraftBlocks.eggSpawner.setCreativeTab(tabDeadCraft);
		DeadCraftBlocks.enderPearlBlock.setCreativeTab(tabDeadCraft);
		DeadCraftBlocks.godBottler.setCreativeTab(tabDeadCraft);
	}

	public static void registerCraftRecipe() {
		//TODO add craft recipe	
	}

}
