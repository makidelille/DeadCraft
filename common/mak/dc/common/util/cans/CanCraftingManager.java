package mak.dc.common.util.cans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;

public class CanCraftingManager {
    
    private Map<ItemStack[], Integer> recipesList = new HashMap();
    private Map<Integer, CanEffect> EffectsList = new HashMap();
    private static boolean hasInit;
    
    public void addRecipe(ItemStack[] ingredients, CanEffect effect) {
        recipesList.put(ingredients, Integer.valueOf(effect.effectId));
        EffectsList.put(Integer.valueOf(effect.effectId), effect);
    }
    
    public ItemStack craftCan(ItemStack can, ItemStack[] ingredients) {
        ItemStack re = can.copy();
        CanEffect effect = getCanEffectResultId(ingredients);
        if (effect == null) return re;
        else {
            NBTTagCompound tag = can.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            NBTTagList ids;
            if (tag.hasKey("effect_ids")) {
                ids = (NBTTagList) tag.getTag("effect_ids");
            } else {
                ids = new NBTTagList();
            }
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
    
    public CanEffect getCanEffect(int effectId) {
        return EffectsList.get(effectId);
    }
    
    public CanEffect getCanEffectResultId(ItemStack[] ingredients) {
        int id = getMatchingRecipes(ingredients);
        return id == -1 ? null : EffectsList.get(id);
    }
    
    public CanEffect getCraftedEffects(int idMatchingrecipe) {
        return EffectsList.get(idMatchingrecipe);
    }
    
    public CanCraftingManager getInstance() {
        return this;
    }
    
    public int getMatchingRecipes(ItemStack[] ingredients) {
        Iterator iterator = recipesList.entrySet().iterator();
        Entry entry;
        do {
            if (!iterator.hasNext()) return -1;
            entry = (Entry) iterator.next();
        } while (!matchRecipes(ingredients, (ItemStack[]) entry.getKey()));
        return (int) entry.getValue();
        
    }
    
    public int getNextAvailableId() {
        int i = 0;
        while (recipesList.values().contains(i)) {
            i++;
        }
        return i;
    }
    
    public void initialise() {
        hasInit = true;
    }
    
    public boolean matchRecipes(ItemStack[] ingredients, ItemStack[] recipe) {
        if (recipe.length != ingredients.length) return false;
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == null) return false;
            if (!ingredients[i].getItem().equals(recipe[i].getItem()) || ingredients[i].getItemDamage() != recipe[i].getItemDamage()) return false;
        }
        return true;
    }
    
    /**
     * register recipe here
     */
    public void postInitialise() {
        if (!hasInit) return;
        
        addRecipe(new ItemStack[] { new ItemStack(Items.diamond) }, new CanEffectPotion(getNextAvailableId(), 20, "potion_resistance", new PotionEffect[] { new PotionEffect(11, 1, 2) }));
        addRecipe(new ItemStack[] { new ItemStack(Blocks.diamond_block) }, new CanEffectPotion(getNextAvailableId(), 200, "potion_resistance", new PotionEffect[] { new PotionEffect(11, 1, 4) }));
        addRecipe(new ItemStack[] { new ItemStack(Items.blaze_rod), new ItemStack(Items.ghast_tear) }, new CanEffectPotion(getNextAvailableId(), 200, "potion_resistance", new PotionEffect[] { new PotionEffect(12, 1, 2) }));
        addRecipe(new ItemStack[] { new ItemStack(Blocks.dragon_egg) }, new CanEffectDragon(getNextAvailableId()));
        addRecipe(new ItemStack[] { new ItemStack(Blocks.dragon_egg), new ItemStack(Blocks.dragon_egg), new ItemStack(Blocks.dragon_egg), new ItemStack(Blocks.dragon_egg), new ItemStack(Blocks.dragon_egg), new ItemStack(Blocks.dragon_egg) }, new CanEffectDragon(getNextAvailableId()).setDuration(CanEffectDragon.duration * 10));
        addRecipe(new ItemStack[] { new ItemStack(Items.speckled_melon), new ItemStack(Items.cookie) }, new CanEffectPotion(getNextAvailableId(), 20, "potion_speed", new PotionEffect[] { new PotionEffect(1, 1, 2), new PotionEffect(8, 1, 2) }));
        addRecipe(new ItemStack[] { new ItemStack(Items.speckled_melon), new ItemStack(Items.cookie), new ItemStack(Items.nether_star) }, new CanEffectPotion(getNextAvailableId(), 200, "potion_speed", new PotionEffect[] { new PotionEffect(1, 1, 10), new PotionEffect(8, 1, 10) }));
        addRecipe(new ItemStack[] { new ItemStack(Items.diamond_pickaxe), new ItemStack(Items.diamond_shovel) }, new CanEffectPotion(getNextAvailableId(), 200, "potion_haste", new PotionEffect[] { new PotionEffect(3, 1, 1000) }));
        
    }
    
}
