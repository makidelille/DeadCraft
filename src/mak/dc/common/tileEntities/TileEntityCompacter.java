package mak.dc.common.tileEntities;

import net.minecraft.item.ItemStack;

public class TileEntityCompacter extends TileEntityDeadCraftWithPower {
    private static final int MAXCHARGESPEED = 50;
    private static final int MAXCHARGE = 5_000;
    
    private static final byte slotPower = 0;
    private static final byte slotInput = 1;
    private static final byte slotOutput = 2;
    
    private ItemStack[] inv = new ItemStack[3];
    
    @Override
    protected int getMaxChargeSpeed() {
        return MAXCHARGESPEED;
    }
    
    @Override
    protected int getMaxPower() {
        return MAXCHARGE;
    }
    
}
