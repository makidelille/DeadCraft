package mak.dc.items.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import java.util.Map.Entry;

import cpw.mods.fml.common.FMLLog;
import mak.dc.util.CanEffect;
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


	private int curentId;
	
	public CanCraftingManager getInstance() {
		return this;
	}
		
	public void addRecipe(ItemStack[] ingredients, PotionEffect[] effects) {
		this.recipesList.put(ingredients, Integer.valueOf(this.nextId()));
		this.EffectsList.put(Integer.valueOf(this.nextId()), new CanEffect(this.nextId(), effects));
		
		FMLLog.getLogger().info("new can recipe made of " +  ingredients.toString() + " at id : " + this.nextId());
		this.curentId++;
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
			if(tag.hasKey("effect ids")) ids = (NBTTagList) tag.getTag("effect ids");
			else ids = new NBTTagList();
			NBTTagCompound newid = new NBTTagCompound();
			newid.setInteger("id", effect.effectId);;
			ids.appendTag(newid);
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
	

	public void initialise() { //dunno if i put smthg here ?
		this.curentId = 0;
		hasInit = true;
	}

	/** 
	 * register recipe here
	*/
	public void postInitialise() { 
		if(!hasInit) return;
		this.addRecipe(new ItemStack[] {new ItemStack(Items.diamond)},new PotionEffect[]{new PotionEffect(1, 2)});
		this.addRecipe(new ItemStack[] {new ItemStack(Items.diamond_axe)},new PotionEffect[]{new PotionEffect(1, 2)});
		this.addRecipe(new ItemStack[] {new ItemStack(Items.diamond), new ItemStack(Items.diamond_axe)},new PotionEffect[]{new PotionEffect(1, 2)});
		
	}

	public CanEffect getCraftedEffects(int idMatchingrecipe) {
		return this.EffectsList.get(idMatchingrecipe);
	}

	private int nextId() {
		return curentId+1;
	}
	
	
	
}
