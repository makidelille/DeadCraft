package mak.dc.entity.ai;

import mak.dc.items.DeadCraftItems;
import mak.dc.items.ItemMindController;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAITemptMindController extends EntityAITempt {

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
	
	public boolean shouldExecute(){
        if(!super.shouldExecute()) return false;
        else {
        	this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
        	if(temptingPlayer == null) return false;
        	 ItemStack is = this.temptingPlayer.getCurrentEquippedItem();
             if(is == null) return false;
             if(is.getItem() instanceof ItemMindController && temptingPlayer.equals(player)) return true;
        	return false;
        }
    }
	
	@Override
	public boolean continueExecuting() {
		if(!super.continueExecuting()) return false;
		else {
        	this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
        	if(temptingPlayer == null) return false;
        	 ItemStack is = this.temptingPlayer.getCurrentEquippedItem();
             if(is == null) return false;
             if(is.getItem() instanceof ItemMindController && temptingPlayer.equals(player)) return true;
        	return false;
        }
	}
	
	@Override
	public void updateTask() {
		this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, (float)this.temptedEntity.getVerticalFaceSpeed());
		if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) this.temptedEntity.getNavigator().clearPathEntity();
        else this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.speed);
	}

}
