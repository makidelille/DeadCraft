package mak.dc.util.canEffects;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class CanEffect {
	
	/**
	 * the id use to identify the effect
	 */
	public final int effectId;
	/**
	 * the effects of the can
	 */
	
	private String unlocalizedName;
	
	/**
	 * in seconds
	 */
	
	private int duration;
		
	public CanEffect(int effectId, int duration, String name) {
		this.effectId = effectId;
		this.unlocalizedName = name;
		this.duration = 20 * duration;
	}
	public int getEffectId() {
		return this.effectId;
	}
	public String getName() {
		return StatCollector.translateToLocal("dc.canEffect." + this.unlocalizedName + ".name");
	}
	public int getDuration() {
		return this.duration;
	}
	
	
	
	
	public CanEffect setDuration(int dur) {
		this.duration = 20 * dur;
		return this;
	}
	
	public CanEffect setName(String newname) {
		this.unlocalizedName = newname;
		return this;
	}
	
	
	public abstract void applyEffect(World world, EntityPlayer player);	
	public abstract void removeEffect(World world,EntityPlayer player);
	
	

}
