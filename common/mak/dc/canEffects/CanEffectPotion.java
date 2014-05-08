package mak.dc.canEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CanEffectPotion extends CanEffect {

	private PotionEffect[] effect;
	
	public CanEffectPotion(int effectId, int duration, String name, PotionEffect[] effects) {
		super(effectId, duration, name);
		effect = effects;
	}

	@Override
	public void applyEffect(World world, EntityPlayer player) {
		if(!world.isRemote)
			for(int i=0; i< effect.length; i++) {
				player.addPotionEffect(effect[i]);
			}
	}

	@Override
	public void removeEffect(World world, EntityPlayer player) {
		if(!world.isRemote)
			for(int i=0; i< effect.length; i++) {
				player.removePotionEffect(effect[i].getPotionID());
			}

	}

}
