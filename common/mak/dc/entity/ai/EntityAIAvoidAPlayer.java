package mak.dc.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIAvoidAPlayer extends EntityAIAvoidEntity {

	private EntityPlayer playerToAvoid;
	
	public EntityAIAvoidAPlayer(EntityLiving entity, EntityPlayer player, float range, double par4, double par6) {
		super((EntityCreature) entity, EntityPlayer.class, range, par4, par6);
		this.playerToAvoid = player;
	}
	
	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return super.shouldExecute();
	}
	
	@Override
	public boolean continueExecuting() {
		// TODO Auto-generated method stub
		return super.continueExecuting();
	}

	@Override
	public boolean isInterruptible() {
		// TODO Auto-generated method stub
		return super.isInterruptible();
	}
	
	@Override
	public void startExecuting() {
		// TODO Auto-generated method stub
		super.startExecuting();
	}
	
	@Override
	public void updateTask() {
		// TODO Auto-generated method stub
		super.updateTask();
	}
	
}
