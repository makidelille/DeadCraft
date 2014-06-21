package mak.dc.common.util.cans;

import net.minecraft.entity.player.EntityPlayer;
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
        unlocalizedName = name;
        this.duration = 20 * duration;
    }
    
    public abstract void applyEffect(World world, EntityPlayer player);
    
    public int getDuration() {
        return duration;
    }
    
    public int getEffectId() {
        return effectId;
    }
    
    public String getName() {
        return StatCollector.translateToLocal("dc.canEffect." + unlocalizedName + ".name");
    }
    
    public abstract void removeEffect(World world, EntityPlayer player);
    
    public CanEffect setDuration(int dur) {
        duration = 20 * dur;
        return this;
    }
    
    public CanEffect setName(String newname) {
        unlocalizedName = newname;
        return this;
    }
    
}
