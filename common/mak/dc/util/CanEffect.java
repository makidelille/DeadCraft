package mak.dc.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CanEffect {
	
	/**
	 * the id use to identify the effect
	 */
	public final int effectId;
	/**
	 * the effects of the can
	 */
	public final PotionEffect[] effects;
	
	public CanEffect(int effectId, PotionEffect[] effects2) {
		this.effectId = effectId;
		this.effects = effects2;
		
	}

	public int getEffectId() {
		return this.effectId;
	}
	
	public void applyEffect(World world, EntityPlayer player){
		if(!world.isRemote) {
			for (int i = 0; i < effects.length; i++) {
				player.addPotionEffect((PotionEffect) effects[i]);
			}
		}
	}
	
	public void removeEffect(World world,EntityPlayer player) {
		if(!world.isRemote) {
			for (int i = 0; i < effects.length; i++) {
				player.removePotionEffect(effects[i].getPotionID());
			}
		}
	}

}
