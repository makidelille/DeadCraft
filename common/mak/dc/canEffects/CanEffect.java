package mak.dc.canEffects;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public abstract class CanEffect {
	
	/**
	 * the id use to identify the effect
	 */
	public final int effectId;
	/**
	 * the effects of the can
	 */
	
	public final String name;
	
	/**
	 * in seconds
	 */
	
	public int duration;
		
	public CanEffect(int effectId, int duration, String name) {
		this.effectId = effectId;
		this.name = name;
		this.duration = 20 * duration;
	}
	public int getEffectId() {
		return this.effectId;
	}
	public String getName() {
		return this.name;
	}
	
	
	
	
	public CanEffect setDuration(int dur) {
		this.duration = 20 * dur;
		return this;
	}
	public abstract void applyEffect(World world, EntityPlayer player);	
	public abstract void removeEffect(World world,EntityPlayer player);
	
	

}
