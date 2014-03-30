package mak.dc.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.potion.PotionEffect;

public class FlyEffect extends net.minecraft.potion.Potion {

	protected FlyEffect(int par1, boolean par2, int par3) {
		super(par1, par2, par3);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLiving,	BaseAttributeMap attributeMap, int par3) {
			//attributeMap.getAttributeInstanceByName(arg0);
	}
	
	@Override
	public void affectEntity(EntityLivingBase par1EntityLivingBase,
			EntityLivingBase par2EntityLivingBase, int par3, double par4) {
		// TODO Auto-generated method stub
		super.affectEntity(par1EntityLivingBase, par2EntityLivingBase, par3, par4);
	}
	
	
	@Override
	public void performEffect(EntityLivingBase par1EntityLivingBase, int par2) {
		// TODO Auto-generated method stub
		super.performEffect(par1EntityLivingBase, par2);
	}


}
