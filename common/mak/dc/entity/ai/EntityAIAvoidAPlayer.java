package mak.dc.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAIAvoidAPlayer extends EntityAIAvoidEntity {

	private EntityPlayer playerToAvoid;
	private EntityCreature entity;
	
	private PathNavigate entityPathNavigate;
	
	public EntityAIAvoidAPlayer(EntityCreature entity, EntityPlayer player, float range, double par4, double par6) {
		super(entity, EntityPlayer.class, range, par4, par6);
		this.playerToAvoid = player;
		this.entity = entity;
		this.entityPathNavigate = entity.getNavigator();
	}
	
	@Override
	public boolean shouldExecute() {
		System.out.println("shouldExecute");
		if(this.entity.getAttackTarget() instanceof EntityPlayer) {
			EntityPlayer target = (EntityPlayer) this.entity.getAttackTarget();
			if(target == playerToAvoid) {
				System.out.println("yes");
				System.out.println(this.entity.getAttackTarget());
				return true;
			}
		}return super.shouldExecute();
	}
	

	@Override
	public boolean continueExecuting() {
		System.out.println("continueExecuting");
		
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
	//	super.updateTask();
	}
	
}
