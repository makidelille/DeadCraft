package mak.dc.items.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import java.util.Map.Entry;

import cpw.mods.fml.common.FMLLog;
import mak.dc.canEffects.CanEffect;
import mak.dc.canEffects.CanEffectFly;
import mak.dc.canEffects.CanEffectPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;

public class CanCraftingManager {

	
	
	private Map<ItemStack[],Integer> recipesList = new HashMap();
	private Map<Integer,CanEffect> EffectsList = new HashMap();
	private static boolean hasInit;
	
	public CanCraftingManager getInstance() {
		return this;
	}
		
	public void addRecipe(ItemStack[] ingredients, CanEffect effect) {
		this.recipesList.put(ingredients, Integer.valueOf(effect.effectId));
		this.EffectsList.put(Integer.valueOf(effect.effectId), effect);
	}
		

	public boolean matchRecipes(ItemStack[] ingredients, ItemStack[] recipe) {
		if(recipe.length != ingredients.length) return false;
		for (int i=0; i < ingredients.length; i++) {
			if(ingredients[i] == null) return false;
			if(!ingredients[i].getItem().equals(recipe[i].getItem()) || ingredients[i].getItemDamage() != recipe[i].getItemDamage()) return false;
		}
		return true;
	}
	
	public int getMatchingRecipes(ItemStack[] ingredients) {
		Iterator iterator = this.recipesList.entrySet().iterator();
        Entry entry;
        do {
            if (!iterator.hasNext()) return -1;
            entry = (Entry)iterator.next();
        }while (!matchRecipes(ingredients, (ItemStack[]) entry.getKey()));
        return (int) entry.getValue();
		
	}
	
	public CanEffect getCanEffectResultId(ItemStack[] ingredients) {
		int id = getMatchingRecipes(ingredients);
		return (CanEffect) (id == -1 ? null : EffectsList.get(id));
	}
	
	public ItemStack craftCan(ItemStack can, ItemStack[] ingredients) {
		ItemStack re = can.copy();
		CanEffect effect = getCanEffectResultId(ingredients);
		if(effect == null) return re;
		else {
			NBTTagCompound tag = can.getTagCompound();
			if(tag == null) tag = new NBTTagCompound();
			NBTTagList ids;
			if(tag.hasKey("effect_ids")) ids = (NBTTagList) tag.getTag("effect_ids");
			else ids = new NBTTagList();
			NBTTagCompound newid = new NBTTagCompound();
			newid.setInteger("id", effect.effectId);;
			ids.appendTag(newid);
			tag.setTag("effect_ids", ids);
			tag.setBoolean("isActive", false);
			tag.setInteger("tick", 0);
			re.setTagCompound(tag);
		}
		return re;
	}
	
	public int getNextAvailableId() {
		int i = 0;
		while (this.recipesList.values().contains(i))
			i++;
		return i;
	}
	

	public void initialise() {
		hasInit = true;
	}

	/** 
	 * register recipe here
	*/
	public void postInitialise() { 
		if(!hasInit) return;
		
		this.addRecipe(new ItemStack[] {new ItemStack(Items.diamond)}, new CanEffectFly(getNextAvailableId()));
		this.addRecipe(new ItemStack[] {new ItemStack(Blocks.diamond_block)},new CanEffectFly(getNextAvailableId()).setDuration(90));
		
		this.addRecipe(new ItemStack[] { new ItemStack(Blocks.dirt)	}, new CanEffectPotion(getNextAvailableId(), 20, "potion_speed", new PotionEffect[] {new PotionEffect(1, 1, 5),new PotionEffect(8, 1, 5)}));
		this.addRecipe(new ItemStack[] { new ItemStack(Items.apple)	}, new CanEffectPotion(getNextAvailableId(), 20, "potion_resistance", new PotionEffect[] {new PotionEffect(11,1,5) ,new PotionEffect(12,1,5)}));
		this.addRecipe(new ItemStack[] { new ItemStack(Blocks.acacia_stairs) }, new CanEffectPotion(getNextAvailableId(), 20, "potion_haste", new PotionEffect[] {new PotionEffect(3,1,1000) ,new PotionEffect(3,1,1000)}));

		//TODO addRecipes
		
	}

	public CanEffect getCraftedEffects(int idMatchingrecipe) {
		return this.EffectsList.get(idMatchingrecipe);
	}


	public CanEffect getCanEffect(int effectId) {
		return EffectsList.get(effectId);		
	}
	
	
	
}
