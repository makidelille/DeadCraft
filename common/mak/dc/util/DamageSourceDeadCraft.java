package mak.dc.util;

import net.minecraft.util.DamageSource;

public class DamageSourceDeadCraft extends DamageSource {

	public static DamageSource lightning = new DamageSourceDeadCraft("lightning").setDamageBypassesArmor().setFireDamage();
	
	
	
	
	protected DamageSourceDeadCraft(String par1Str) {
		super(par1Str);
	}

	
	@Override
	protected DamageSourceDeadCraft setDamageBypassesArmor() {
		return (DamageSourceDeadCraft) super.setDamageBypassesArmor();
	}
	
	@Override
	protected DamageSourceDeadCraft setDamageAllowedInCreativeMode() {
		return (DamageSourceDeadCraft) super.setDamageAllowedInCreativeMode();
	}
	
	@Override
	protected DamageSourceDeadCraft setFireDamage() {
		return (DamageSourceDeadCraft) super.setFireDamage();
	}
}
