package mak.dc.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {
	
	public static CreativeTabs tabDeadCraft;

	public static void init() {
		tabDeadCraft = new CreativeTabs("DeadCraft") {
			@Override
            public ItemStack getIconItemStack() {
				return new ItemStack(ItemInit.deadWand, 1, 0);
		}};
		postInit();
		
	}
	
	protected static void postInit() {
		ItemInit.deadWand.setCreativeTab(tabDeadCraft);
		ItemInit.lifeCrystal.setCreativeTab(tabDeadCraft);
		ItemInit.mindController.setCreativeTab(tabDeadCraft);
		ItemInit.controller.setCreativeTab(tabDeadCraft);

		
		BlockInit.eggSpawner.setCreativeTab(tabDeadCraft);
		BlockInit.enderPearlBlock.setCreativeTab(tabDeadCraft);
	}
}
