package mak.dc.common.util.cans;

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
        if (!world.isRemote) {
            for (PotionEffect element : effect) {
                player.addPotionEffect(element);
            }
        }
    }
    
    @Override
    public void removeEffect(World world, EntityPlayer player) {
        if (!world.isRemote) {
            for (PotionEffect element : effect) {
                player.removePotionEffect(element.getPotionID());
            }
        }
        
    }
    
}
