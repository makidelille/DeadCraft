package mak.dc.common.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
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
        playerToAvoid = player;
        this.entity = entity;
        entityPathNavigate = entity.getNavigator();
        distanceFromEntity = range;
        farSpeed = par4;
        nearSpeed = par6;
    }
    
    @Override
    public boolean continueExecuting() {
        return !entityPathNavigate.noPath();
    }
    
    @Override
    public boolean shouldExecute() {
        closestLivingEntity = playerToAvoid;
        
        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 16, 7, entity.worldObj.getWorldVec3Pool().getVecFromPool(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));
        
        if (vec3 == null) return false;
        else if (closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < closestLivingEntity.getDistanceSqToEntity(entity)) return false;
        else {
            entityPathEntity = entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
            return entityPathEntity == null ? false : entityPathEntity.isDestinationSame(vec3);
        }
    }
    
    @Override
    public void startExecuting() {
        entityPathNavigate.setPath(entityPathEntity, farSpeed);
    }
    
    @Override
    public void updateTask() {
        if (entity.getDistanceSqToEntity(closestLivingEntity) < distanceFromEntity) {
            entity.getNavigator().setSpeed(nearSpeed);
        } else {
            entity.getNavigator().setSpeed(farSpeed);
        }
    }
    
}
