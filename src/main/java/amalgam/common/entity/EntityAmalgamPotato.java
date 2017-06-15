package amalgam.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityAmalgamPotato extends EntityThrowable
{
  float explosion = 0.5F;
  float damage = 1.0F;
  int color;
  
  public EntityAmalgamPotato(World world) {
    super(world);
  }
  
  public EntityAmalgamPotato(World world, EntityLivingBase living, float vFactor, float explosion, float damage, int color) {
    super(world, living);
    this.motionX *= vFactor;
    this.motionY *= vFactor;
    this.motionZ *= vFactor;
    
    this.explosion = explosion;
    this.damage = damage;
    this.color = color;
  }
  
  protected void onImpact(MovingObjectPosition movObjPos)
  {
    if (movObjPos.entityHit != null) {
      movObjPos.entityHit.attackEntityFrom(net.minecraft.util.DamageSource.causeThrownDamage(this, getThrower()), this.damage);
    }
    if (!this.worldObj.isRemote) {
      if (movObjPos.entityHit != null) {
        this.worldObj.createExplosion(this, movObjPos.entityHit.posX, movObjPos.entityHit.posY, movObjPos.entityHit.posZ, this.explosion, true);
      }
      else {
        this.worldObj.createExplosion(this, movObjPos.blockX, movObjPos.blockY, movObjPos.blockZ, this.explosion, true);
      }
      setDead();
    }
  }
  
  public int getColor() {
    return this.color;
  }
}
