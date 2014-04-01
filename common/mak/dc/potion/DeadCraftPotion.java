package mak.dc.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class DeadCraftPotion extends Potion {

    public static final DeadCraftPotion[] potionTypes = new DeadCraftPotion[16];
	public static DeadCraftPotion fly = (DeadCraftPotion) (new DeadCraftPotion(1,false, 5149489).setPotionName("potion.fly"));
	
	
	protected DeadCraftPotion(int id, boolean isBad, int liquidColor) {
		super(id, isBad, liquidColor);
		potionTypes[id] = this;
	}
	
	@Override
	public void performEffect(EntityLivingBase entLive, int par2) {
		if(!entLive.worldObj.isRemote) {
			if(this.id == fly.id) {
				if(entLive instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entLive;
					player.capabilities.allowFlying = true; //TODO check if it works
				}
				
			}else {
				
				
			}
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		if(this.id == fly.id) {
			return duration > 0 ;
		}else
			return false;
	}
	
	

}
