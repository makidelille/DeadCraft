package mak.dc.util;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PowerManager {

	private static HashMap<Item, Integer> powerProductionAmount = new HashMap<Item, Integer>();

	public PowerManager getInstance() {
		return this;
	}
	
	public static void registerAsFuel(ItemStack stack, int powerAmount) {
		if(!powerProductionAmount.containsKey(stack.getItem())) {
			powerProductionAmount.put(stack.getItem(), powerAmount);
		}
	}
	
	public static boolean isFuel(ItemStack stack) {
		return powerProductionAmount.containsKey(stack.getItem());
	}
	
	public static int getPowerProduce(ItemStack stack) {
		if(isFuel(stack)) return powerProductionAmount.get(stack.getItem());
		else return 0;
	}
	
}
