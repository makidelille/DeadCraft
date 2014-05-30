package mak.dc.util;

import java.util.HashMap;

import mak.dc.items.DeadCraftItems;
import mak.dc.items.ItemCrystal;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PowerManager {

	private HashMap<Item, Integer> powerProductionAmount = new HashMap<Item, Integer>();

	public PowerManager getInstance() {
		return this;
	}
	
	public void registerAsFuel(ItemStack stack, int powerAmount) {
		if(!powerProductionAmount.containsKey(stack.getItem())) {
			powerProductionAmount.put(stack.getItem(), powerAmount);
		}
	}
	
	public boolean isFuel(ItemStack stack) {
		return powerProductionAmount.containsKey(stack.getItem()) || stack.getItem() instanceof ItemCrystal;
	}
	
	public int getPowerProduce(ItemStack stack) {
		if(isFuel(stack) && !(stack.getItem() instanceof ItemCrystal)) return powerProductionAmount.get(stack.getItem());
		else if(stack.getItem() instanceof ItemCrystal) return ItemCrystal.getCharge(stack);
		else return 0;
	}
	
	public void initialise(){
		registerAsFuel(new ItemStack(Items.redstone), 100);
		registerAsFuel(new ItemStack(Blocks.redstone_block), 990);
		registerAsFuel(new ItemStack(Items.nether_star), 50_000);
		registerAsFuel(new ItemStack(Blocks.dragon_egg), 150_000);
	}
	
}
