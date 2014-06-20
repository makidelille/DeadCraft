package mak.dc.common.entity.ai;

import mak.dc.common.items.DeadCraftItems;
import mak.dc.common.items.ItemMindController;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityAITemptMindController extends EntityAITempt {
    
    // TODO work on it later
    
    private EntityPlayer player;
    private EntityPlayer temptingPlayer;
    private EntityCreature temptedEntity;
    private double speed;
    
    public EntityAITemptMindController(EntityCreature temptedEntity, double speed, boolean scaredByPlayerMovement, EntityPlayer player) {
        super(temptedEntity, speed, DeadCraftItems.mindController, false);
        this.player = player;
        this.speed = speed;
        this.temptedEntity = temptedEntity;
    }
    
    @Override
    public boolean continueExecuting() {
        if (!super.continueExecuting()) return false;
        else {
            temptingPlayer = temptedEntity.worldObj.getClosestPlayerToEntity(temptedEntity, 10.0D);
            if (temptingPlayer == null) return false;
            ItemStack is = temptingPlayer.getCurrentEquippedItem();
            if (is == null) return false;
            if (is.getItem() instanceof ItemMindController && temptingPlayer.equals(player)) return true;
            return false;
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) return false;
        else {
            temptingPlayer = temptedEntity.worldObj.getClosestPlayerToEntity(temptedEntity, 10.0D);
            if (temptingPlayer == null) return false;
            ItemStack is = temptingPlayer.getCurrentEquippedItem();
            if (is == null) return false;
            if (is.getItem() instanceof ItemMindController && temptingPlayer.equals(player)) return true;
            return false;
        }
    }
    
    @Override
    public void updateTask() {
        temptedEntity.getLookHelper().setLookPositionWithEntity(temptingPlayer, 30.0F, temptedEntity.getVerticalFaceSpeed());
        if (temptedEntity.getDistanceSqToEntity(temptingPlayer) < 6.25D) {
            temptedEntity.getNavigator().clearPathEntity();
        } else {
            temptedEntity.getNavigator().tryMoveToEntityLiving(temptingPlayer, speed);
        }
    }
    
}
