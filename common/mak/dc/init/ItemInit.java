package mak.dc.init;

import mak.dc.items.ItemController;
import mak.dc.items.ItemDeadWand;
import mak.dc.items.ItemLifeCrystal;
import mak.dc.items.ItemMindController;
import mak.dc.lib.ItemInfo;
import mak.dc.lib.Lib;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemInit {
	
	public static Item deadWand;
	public static Item lifeCrystal;
	public static Item mindController;
	public static Item controller; //TODO
	public static Item superChargedNetherStar; //TODO craft usage
	

	
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
	
	public static void registerCraftRecipe() {
		
		
	}
}
