package mak.dc.init;

import mak.dc.items.ItemDeadWand;
import mak.dc.items.ItemLifeCrystal;
import mak.dc.items.ItemMindController;
import mak.dc.lib.ItemInfo;
import mak.dc.lib.Lib;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemInit {
	
	public static Item deadWand;
	public static Item lifeCrystal;
	public static Item mindController;
	
	static EnumArmorMaterial DC_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("deadcraft_armor", 20, new int[]{2, 8, 4, 2}, 15);

	
	public static void init() {
		
		
		
		deadWand = new ItemDeadWand(ItemInfo.DEADWAND_ID);
		lifeCrystal = new ItemLifeCrystal(ItemInfo.LIFECRYSTAL_ID);
		mindController = new ItemMindController(ItemInfo.MINDCONTROLLER_ID);
		
		GameRegistry.registerItem(deadWand, ItemInfo.DEADWAND_KEY, Lib.MOD_ID);
		GameRegistry.registerItem(lifeCrystal, ItemInfo.LIFECRYSTAL_KEY, Lib.MOD_ID);
	
	}
	
	public static void registerCraftRecipe() {
		
		
	}
}
