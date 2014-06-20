package mak.dc.common.util.cans;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CanEffectDragon extends CanEffect {
    
    public static final int duration = 60;
    private static CanEffect[] effects = new CanEffect[] { new CanEffectFly(0).setDuration(duration), new CanEffectPotion(0, duration, "subEffect", new PotionEffect[] { new PotionEffect(1, 1, 5), new PotionEffect(8, 1, 0) }), new CanEffectPotion(0, duration, "subEffect", new PotionEffect[] { new PotionEffect(11, 1, 50) }), new CanEffectPotion(0, duration, "subEffect", new PotionEffect[] { new PotionEffect(3, 1, 1000), new PotionEffect(3, 1, 1000) }), new CanEffectPotion(0, duration, "subEffect", new PotionEffect[] { new PotionEffect(5, 1, 500) }) };
    
    public CanEffectDragon(int effectId) {
        super(effectId, duration, "dragon");
        
    }
    
    @Override
    public void applyEffect(World world, EntityPlayer player) {
        for (CanEffect effect : effects) {
            effect.applyEffect(world, player);
        }
        
    }
    
    @Override
    public void removeEffect(World world, EntityPlayer player) {
        for (CanEffect effect : effects) {
            effect.removeEffect(world, player);
        }
        
    }
    
}
