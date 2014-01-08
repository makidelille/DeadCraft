package mak.dc.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

public class EntityAIAvoidAPlayer extends EntityAIAvoidEntity {

	private EntityPlayer playerToAvoid;
	private EntityCreature entity;
	
	private PathNavigate entityPathNavigate;
	private PathEntity entityPathEntity;
	private double distanceFromEntity;
	private double farSpeed;
	private Entity closestLivingEntity;
	private double nearSpeed;
	
	public EntityAIAvoidAPlayer(EntityCreature entity, EntityPlayer player, float range, double par4, double par6) {
		super(entity, EntityPlayer.class, range, par4, par6);
		this.playerToAvoid = player;
		this.entity = entity;
		this.entityPathNavigate = entity.getNavigator();
		this.distanceFromEntity = range;
		this.farSpeed = par4;
		this.nearSpeed = par6;
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.entity.getAttackTarget() instanceof EntityPlayer) {
			EntityPlayer target = (EntityPlayer) this.entity.getAttackTarget();
			if(target == playerToAvoid) {			
				this.closestLivingEntity = target;
				
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, this.entity.worldObj.getWorldVec3Pool().getVecFromPool(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

		        if (vec3 == null)
		        {
		            return false;
		        }
		        else if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.entity))
		        {
		            return false;
		        }
		        else
		        {
		            this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
		            return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
		        }
			}
		}return false;
	}
	
	

	@Override
	public boolean continueExecuting() {
		return !this.entityPathNavigate.noPath();
	}

	
	@Override
	public void startExecuting() {
		this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
	}
	
	@Override
	public void updateTask() {
		if (this.entity.getDistanceSqToEntity(this.closestLivingEntity) < this.distanceFromEntity) {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        }
        else {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }
	}
	
}
