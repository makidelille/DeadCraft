package mak.dc.util;

import net.minecraft.util.DamageSource;

public class DamageSourceDeadCraft extends DamageSource {

	

	public static DamageSource lightning = new DamageSourceDeadCraft("lightning").setDamageBypassesArmor().setFireDamage();
	
public DamageSourceDeadCraft(String string) {
		super(string);
	}
	
	
	
}
