package eladkay.quaeritum.common.entity

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.EntitySelectors
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants


/**
 * @author WireSegal
 * Created at 6:32 PM on 8/7/17.
 */
@Suppress("LeakingThis")
abstract class EntityBaseProjectile(worldIn: World) : Entity(worldIn), IProjectile {
    var shootingEntity: Entity? = null
    var knockback = 1f

    var gravity: Float
        get() = dataManager.get(GRAVITY)
        set(value) = dataManager.set(GRAVITY, value)

    var damage: Float
        get() = dataManager.get(DAMAGE)
        set(value) = dataManager.set(DAMAGE, value)

    init {
        setSize(0.5f, 0.5f)
    }

    constructor(worldIn: World, x: Double, y: Double, z: Double) : this(worldIn) {
        setPosition(x, y, z)
    }

    constructor(worldIn: World, shooter: EntityLivingBase) : this(worldIn, shooter.posX, shooter.posY + shooter.eyeHeight.toDouble() - 0.1, shooter.posZ) {
        shootingEntity = shooter
    }

    override fun entityInit() {
        dataManager.register(DAMAGE, 2f)
        dataManager.register(GRAVITY, 0.05f)
    }

    fun setAim(shooter: Entity, pitch: Float, yaw: Float, velocity: Float, inaccuracy: Float) {
        val xHeading = -MathHelper.sin(yaw * Math.PI.toFloat() / 180) * MathHelper.cos(pitch * Math.PI.toFloat() / 180)
        val yHeading = -MathHelper.sin(pitch * Math.PI.toFloat() / 180)
        val zHeading = MathHelper.cos(yaw * Math.PI.toFloat() / 180) * MathHelper.cos(pitch * Math.PI.toFloat() / 180)
        shoot(xHeading.toDouble(), yHeading.toDouble(), zHeading.toDouble(), velocity, inaccuracy)
        motionX += shooter.motionX
        motionZ += shooter.motionZ

        if (!shooter.onGround) motionY += shooter.motionY
    }

    override fun shoot(xOrig: Double, yOrig: Double, zOrig: Double, velocity: Float, inaccuracy: Float) {
        var x = xOrig
        var y = yOrig
        var z = zOrig
        val magnitude = MathHelper.sqrt(x * x + y * y + z * z)
        x /= magnitude.toDouble()
        y /= magnitude.toDouble()
        z /= magnitude.toDouble()
        x += rand.nextGaussian() * 0.0075 * inaccuracy.toDouble()
        y += rand.nextGaussian() * 0.0075 * inaccuracy.toDouble()
        z += rand.nextGaussian() * 0.0075 * inaccuracy.toDouble()
        x *= velocity.toDouble()
        y *= velocity.toDouble()
        z *= velocity.toDouble()
        motionX = x
        motionY = y
        motionZ = z
        val horizontalMagnitude = MathHelper.sqrt(x * x + z * z)
        rotationYaw = (MathHelper.atan2(x, z) * (180.0 / Math.PI)).toFloat()
        rotationPitch = (MathHelper.atan2(y, horizontalMagnitude.toDouble()) * (180.0 / Math.PI)).toFloat()
        prevRotationYaw = rotationYaw
        prevRotationPitch = rotationPitch
    }

    override fun setVelocity(x: Double, y: Double, z: Double) {
        super.setVelocity(x, y, z)

        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            val f = MathHelper.sqrt(x * x + z * z)
            rotationPitch = (MathHelper.atan2(y, f.toDouble()) * (180.0 / Math.PI)).toFloat()
            rotationYaw = (MathHelper.atan2(x, z) * (180.0 / Math.PI)).toFloat()
            prevRotationPitch = rotationPitch
            prevRotationYaw = rotationYaw
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch)
        }
    }

    override fun onUpdate() {
        super.onUpdate()

        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            val f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ)
            rotationYaw = (MathHelper.atan2(motionX, motionZ) * (180.0 / Math.PI)).toFloat()
            rotationPitch = (MathHelper.atan2(motionY, f.toDouble()) * (180.0 / Math.PI)).toFloat()
            prevRotationYaw = rotationYaw
            prevRotationPitch = rotationPitch
        }

        val position = Vec3d(posX, posY, posZ)
        var projected = Vec3d(posX + motionX, posY + motionY, posZ + motionZ)
        var trace = world.rayTraceBlocks(position, projected, false, true, false)

        if (trace != null)
            projected = Vec3d(trace.hitVec.x, trace.hitVec.y, trace.hitVec.z)

        val entity = findEntityOnPath(position, projected)

        if (entity != null)
            trace = RayTraceResult(entity)

        if (trace != null && trace.entityHit is EntityPlayer && shootingEntity is EntityPlayer &&
                (shootingEntity as EntityPlayer).canAttackPlayer(trace.entityHit as EntityPlayer))
            trace = null

        if (trace != null)
            onHit(trace)

        posX += motionX
        posY += motionY
        posZ += motionZ
        val horizontalMagnitude = MathHelper.sqrt(motionX * motionX + motionZ * motionZ)
        rotationYaw = (MathHelper.atan2(motionX, motionZ) * (180.0 / Math.PI)).toFloat()

        rotationPitch = (MathHelper.atan2(motionY, horizontalMagnitude.toDouble()) * (180.0 / Math.PI)).toFloat()
        while (rotationPitch - prevRotationPitch < -180.0f) prevRotationPitch -= 360.0f
        while (rotationPitch - prevRotationPitch >= 180.0f) prevRotationPitch += 360.0f
        while (rotationYaw - prevRotationYaw < -180.0f) prevRotationYaw -= 360.0f
        while (rotationYaw - prevRotationYaw >= 180.0f) prevRotationYaw += 360.0f

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f
        var motionModifier = 0.99f

        if (isInWater) {
            for (i in 0..3)
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25, posY - motionY * 0.25, posZ - motionZ * 0.25, motionX, motionY, motionZ, *IntArray(0))
            motionModifier = 0.6f
        }

        if (isWet) extinguish()

        motionX *= motionModifier.toDouble()
        motionY *= motionModifier.toDouble()
        motionZ *= motionModifier.toDouble()

        if (!hasNoGravity()) motionY -= gravity

        setPosition(posX, posY, posZ)
        doBlockCollisions()
    }

    abstract fun getDefaultDamageSource(): DamageSource?
    abstract fun getShotDamageSource(shooter: Entity): DamageSource?
    open fun onImpactEntity(entity: Entity, successful: Boolean) {
        if (entity is EntityEnderman) setDead()
    }

    open fun onImpactBlock(hitVec: Vec3d, side: EnumFacing, position: BlockPos) = setDead()

    protected fun onHit(trace: RayTraceResult) {
        val entity = trace.entityHit

        if (entity != null) {

            val magnitude = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
            val damage = MathHelper.ceil(magnitude.toDouble() * damage)

            val shooter = shootingEntity
            val source = if (shooter != null)
                getShotDamageSource(shooter)
            else
                getDefaultDamageSource()

            if (isBurning && entity !is EntityEnderman)
                entity.setFire(5)

            if (source == null) onImpactEntity(entity, true)
            else {
                if (entity.attackEntityFrom(source, damage.toFloat())) {
                    if (entity is EntityLivingBase && knockback > 0) {
                        val horizontalMagnitude = MathHelper.sqrt(motionX * motionX + motionZ * motionZ)
                        if (horizontalMagnitude > 0.0f)
                            entity.addVelocity(motionX * knockback * 0.6 / horizontalMagnitude.toDouble(), 0.1, motionZ * knockback * 0.6 / horizontalMagnitude.toDouble())
                    }

                    if (!world.isRemote)
                        onImpactEntity(entity, true)

                } else if (!world.isRemote)
                    onImpactEntity(entity, false)
            }
        } else if (!world.isRemote)
            onImpactBlock(trace.hitVec, trace.sideHit, trace.blockPos)

    }

    protected fun findEntityOnPath(start: Vec3d, end: Vec3d): Entity? {
        val predicate = Predicates.and(ARROW_TARGETS, Predicate {
            it != shootingEntity
        })
        var entity: Entity? = null
        val list = world.getEntitiesInAABBexcluding(this, entityBoundingBox.expand(motionX, motionY, motionZ).grow(1.0), predicate)
        var maxDistance = 0.0

        for (onPath in list) {
            val boundingBox = onPath.entityBoundingBox.grow(0.3)
            val trace = boundingBox.calculateIntercept(start, end)

            if (trace != null) {
                val d1 = start.squareDistanceTo(trace.hitVec)

                if (d1 < maxDistance || maxDistance == 0.0) {
                    entity = onPath
                    maxDistance = d1
                }
            }
        }

        return entity
    }

    public override fun writeEntityToNBT(compound: NBTTagCompound) {
        compound.setFloat("damage", damage)
        compound.setFloat("knockback", knockback)
        compound.setFloat("gravity", gravity)
    }

    public override fun readEntityFromNBT(compound: NBTTagCompound) {
        if (compound.hasKey("damage", Constants.NBT.TAG_ANY_NUMERIC)) damage = compound.getFloat("damage")
        if (compound.hasKey("knockback", Constants.NBT.TAG_ANY_NUMERIC)) knockback = compound.getFloat("knockback")
        if (compound.hasKey("gravity", Constants.NBT.TAG_ANY_NUMERIC)) gravity = compound.getFloat("knockback")
    }

    override fun canTriggerWalking() = false

    override fun canBeAttackedWithItem() = false

    override fun getEyeHeight() = 0.0f

    companion object {
        @JvmField
        val DAMAGE: DataParameter<Float> = EntityDataManager.createKey(EntityBaseProjectile::class.java, DataSerializers.FLOAT)
        @JvmField
        val GRAVITY: DataParameter<Float> = EntityDataManager.createKey(EntityBaseProjectile::class.java, DataSerializers.FLOAT)

        private val ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Predicate<Entity> { it!!.canBeCollidedWith() })
    }
}
