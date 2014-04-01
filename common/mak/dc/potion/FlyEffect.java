package mak.dc.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FlyEffect extends PotionEffect {
	
	private int duration;

	protected FlyEffect(int duration) {
		super(0, 0);
		this.duration = duration;
	}
	
	
}
