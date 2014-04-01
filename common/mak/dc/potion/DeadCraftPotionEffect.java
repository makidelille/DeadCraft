package mak.dc.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class DeadCraftPotionEffect extends PotionEffect {

	private int duration;

	public DeadCraftPotionEffect(int id, int duration, int amplifier, boolean isAmbiant) {
		super(id, duration, amplifier, isAmbiant);
		this.setCurativeItems(null);
		this.duration = duration;
		
	}
	
	@Override
	public boolean onUpdate(EntityLivingBase entLive) {
		if(this.duration > 0) {
			if(DeadCraftPotion.potionTypes[this.getPotionID()].isReady(getDuration(), getAmplifier()))
				this.performEffect(entLive);
			this.duration--;
			}
			return this.duration > 0;
	}
	
	@Override
	public void performEffect(EntityLivingBase entLive) {
		if (this.duration > 0)
        {
            DeadCraftPotion.potionTypes[this.getPotionID()].performEffect(entLive, this.getAmplifier());
        }
	}
	
	@Override
	public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound par1nbtTagCompound) {
		NBTTagCompound nbt = super.writeCustomPotionEffectToNBT(par1nbtTagCompound);
		nbt.setBoolean("isDeadCraft", true);
		return nbt;
	}
	
	public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound par0NBTTagCompound)
    {
        byte b0 = par0NBTTagCompound.getByte("Id");
        byte b1 = par0NBTTagCompound.getByte("Amplifier");
        int i = par0NBTTagCompound.getInteger("Duration");
        boolean flag = par0NBTTagCompound.getBoolean("Ambient");
        boolean flagDC = par0NBTTagCompound.getBoolean("isDeadCraft");
        if(flagDC) return new DeadCraftPotionEffect(b0, i, b1, flag);
        else return new PotionEffect(b0, i, b1, flag);
    }
	
	

}
