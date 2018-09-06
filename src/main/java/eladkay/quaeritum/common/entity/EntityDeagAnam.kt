package eladkay.quaeritum.common.entity

import com.teamwizardry.librarianlib.features.kotlin.createStackKey
import com.teamwizardry.librarianlib.features.kotlin.managedValue
import com.teamwizardry.librarianlib.features.kotlin.with
import eladkay.quaeritum.common.item.historic.ItemDeagAnam
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.util.Constants
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

/**
 * @author WireSegal
 * Created at 9:04 AM on 7/17/18.
 */
class EntityDeagAnam(worldIn: World) : Entity(worldIn), IProjectile {

    init {
        this.setSize(0.5f, 0.5f)
    }

    constructor(worldIn: World, x: Double, y: Double, z: Double): this(worldIn) {
        this.setPosition(x, y, z)
    }

    constructor(worldIn: World, shooter: EntityLivingBase): this(worldIn, shooter.posX, shooter.posY + shooter.eyeHeight - 0.1, shooter.posZ) {
        this.thrower = shooter
    }


    private var xTile: Int = -1
    private var yTile: Int = -1
    private var zTile: Int = -1
    private var inTile: Block? = null
    private var inGround: Boolean = false
    private var throwableShake: Int = 0
    private var throwerName: String = ""
    private var ignoreEntity: Entity? = null
    private var ignoreTime: Int = 0

    private val gravityVelocity = 0f

    override fun isPushedByWater() = false

    companion object {
        @JvmField
        val ITEM = EntityDeagAnam::class.createStackKey()
    }

    var stack by managedValue(ITEM)

    override fun entityInit() {
        with(ITEM, ItemStack.EMPTY)
    }

    @SideOnly(Side.CLIENT)
    override fun isInRangeToRenderDist(distance: Double): Boolean {
        var d0 = entityBoundingBox.averageEdgeLength * 4.0
        if (d0.isNaN())
            d0 = 4.0

        return distance < d0 * d0 * 64 * 64
    }

    fun shoot(entityThrower: Entity, rotationPitchIn: Float, rotationYawIn: Float, pitchOffset: Float, velocity: Float, inaccuracy: Float) {
        val xHeading = -MathHelper.sin(rotationYawIn * Math.PI.toFloat() / 180) * MathHelper.cos(rotationPitchIn * Math.PI.toFloat() / 180)
        val yHeading = -MathHelper.sin((rotationPitchIn + pitchOffset) * Math.PI.toFloat() / 180)
        val zHeading = MathHelper.cos(rotationYawIn * Math.PI.toFloat() / 180) * MathHelper.cos(rotationPitchIn * Math.PI.toFloat() / 180)
        shoot(xHeading.toDouble(), yHeading.toDouble(), zHeading.toDouble(), velocity, inaccuracy)
        motionX += entityThrower.motionX
        motionZ += entityThrower.motionZ

        if (!entityThrower.onGround) motionY += entityThrower.motionY
    }

    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        val magnitude = MathHelper.sqrt(x * x + y * y + z * z)
        motionX = (x / magnitude + rand.nextGaussian() * 0.0075 * inaccuracy) * velocity
        motionY = (y / magnitude + rand.nextGaussian() * 0.0075 * inaccuracy) * velocity
        motionZ = (z / magnitude + rand.nextGaussian() * 0.0075 * inaccuracy) * velocity
        val horizontalMagnitude = MathHelper.sqrt(motionX * motionX + motionZ * motionZ)
        rotationYaw = (MathHelper.atan2(motionX, motionZ) * (180.0 / Math.PI)).toFloat()
        rotationPitch = (MathHelper.atan2(motionY, horizontalMagnitude.toDouble()) * (180.0 / Math.PI)).toFloat()
        prevRotationYaw = rotationYaw
        prevRotationPitch = rotationPitch
    }

    @SideOnly(Side.CLIENT)
    override fun setVelocity(x: Double, y: Double, z: Double) {
        motionX = x
        motionY = y
        motionZ = z

        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            val f = MathHelper.sqrt(x * x + z * z)
            rotationYaw = (MathHelper.atan2(x, z) * (180.0 / Math.PI)).toFloat()
            rotationPitch = (MathHelper.atan2(y, f.toDouble()) * (180.0 / Math.PI)).toFloat()
            prevRotationYaw = rotationYaw
            prevRotationPitch = rotationPitch
        }
    }

    override fun onUpdate() {
        lastTickPosX = posX
        lastTickPosY = posY
        lastTickPosZ = posZ
        super.onUpdate()

        if (throwableShake > 0) --throwableShake

        if (inGround) {
            if (world.getBlockState(BlockPos(xTile, yTile, zTile)).block == inTile)
                return

            inGround = false
            motionX *= rand.nextFloat() * 0.2
            motionY *= rand.nextFloat() * 0.2
            motionZ *= rand.nextFloat() * 0.2
        }

        var pos = Vec3d(posX, posY, posZ)
        var cast = Vec3d(posX + motionX, posY + motionY, posZ + motionZ)
        var trace = world.rayTraceBlocks(pos, cast)
        pos = Vec3d(posX, posY, posZ)
        cast = Vec3d(posX + motionX, posY + motionY, posZ + motionZ)

        if (trace != null)
            cast = Vec3d(trace.hitVec.x, trace.hitVec.y, trace.hitVec.z)

        var collidingWith: Entity? = null
        val list = world.getEntitiesWithinAABBExcludingEntity(this, entityBoundingBox.expand(motionX, motionY, motionZ).grow(1.0))
        var closestCollision = 0.0
        var ignoreThisCollision = false

        for (entity in list) {
            if (entity.canBeCollidedWith()) {
                if (entity == ignoreEntity) {
                    ignoreThisCollision = true
                } else if (thrower != null && ticksExisted < 2 && ignoreEntity == null) {
                    ignoreEntity = entity
                    ignoreThisCollision = true
                } else {
                    ignoreThisCollision = false
                    val collisionBox = entity.entityBoundingBox.grow(0.3)
                    val traceToBox = collisionBox.calculateIntercept(pos, cast)

                    if (traceToBox != null) {
                        val d1 = pos.squareDistanceTo(traceToBox.hitVec)

                        if (d1 < closestCollision || closestCollision == 0.0) {
                            collidingWith = entity
                            closestCollision = d1
                        }
                    }
                }
            }
        }

        if (ignoreEntity != null)
            if (ignoreThisCollision)
                ignoreTime = 2
            else if (ignoreTime-- <= 0)
                ignoreEntity = null

        if (collidingWith != null)
            trace = RayTraceResult(collidingWith)

        if (trace != null)
            if (trace.typeOfHit == RayTraceResult.Type.BLOCK &&
                    world.getBlockState(trace.blockPos).block == Blocks.PORTAL) setPortal(trace.blockPos)
            else if (!ForgeEventFactory.onProjectileImpact(this, trace))
                onImpact(trace)

        posX += motionX
        posY += motionY
        posZ += motionZ
        val horizontalMag = MathHelper.sqrt(motionX * motionX + motionZ * motionZ)
        rotationYaw = (MathHelper.atan2(motionX, motionZ) * (180.0 / Math.PI)).toFloat()
        rotationPitch = (MathHelper.atan2(motionY, horizontalMag.toDouble()) * (180.0 / Math.PI)).toFloat()

        while (rotationPitch - prevRotationPitch < -180.0f) prevRotationPitch -= 360.0f
        while (rotationPitch - prevRotationPitch >= 180.0f) prevRotationPitch += 360.0f
        while (rotationYaw - prevRotationYaw < -180.0f) prevRotationYaw -= 360.0f
        while (rotationYaw - prevRotationYaw >= 180.0f) prevRotationYaw += 360.0f

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f
        var momentum = 0.99f
        val gravity = gravityVelocity

        if (isInWater) {
            for (j in 0..3)
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25, posY - motionY * 0.25, posZ - motionZ * 0.25, motionX, motionY, motionZ)

            momentum = 0.8f
        }

        if (inGround) {
            motionX *= momentum
            motionY *= momentum
            motionZ *= momentum

            if (!hasNoGravity())
                motionY -= gravity
        }

        setPosition(posX, posY, posZ)
    }

    public override fun writeEntityToNBT(compound: NBTTagCompound) {
        compound.setInteger("xTile", xTile)
        compound.setInteger("yTile", yTile)
        compound.setInteger("zTile", zTile)
        val loc = inTile?.let { Block.REGISTRY.getNameForObject(it) }
        compound.setString("inTile", loc.toString())
        compound.setByte("shake", throwableShake.toByte())
        compound.setByte("inGround", (if (inGround) 1 else 0).toByte())

        if (throwerName.isEmpty() && thrower is EntityPlayer) {
            throwerName = thrower?.name ?: ""
        }

        compound.setString("ownerName", throwerName)

        stack = ItemStack(compound.getCompoundTag("Item"))

    }

    public override fun readEntityFromNBT(compound: NBTTagCompound) {
        xTile = compound.getInteger("xTile")
        yTile = compound.getInteger("yTile")
        zTile = compound.getInteger("zTile")

        inTile = (if (compound.hasKey("inTile", Constants.NBT.TAG_STRING))
            Block.getBlockFromName(compound.getString("inTile"))
        else Block.getBlockById(compound.getByte("inTile").toInt()))

        throwableShake = compound.getByte("shake").toInt()
        inGround = compound.getByte("inGround").toInt() == 1
        throwerName = compound.getString("ownerName")

        compound.setTag("Item", stack.writeToNBT(NBTTagCompound()))

    }

    var thrower: EntityLivingBase? = null
        private set
        get() {
            if (field == null && !throwerName.isEmpty()) {
                thrower = world.getPlayerEntityByName(throwerName)

                if (field == null && world is WorldServer) {
                    val entity = (world as WorldServer).getEntityFromUuid(UUID.fromString(throwerName))
                    if (entity is EntityLivingBase)
                        field = entity
                }
            }

            return field
        }

    fun getDamage(): Double {
        val attributes = AttributeMap()
        val attribute = attributes.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
        thrower?.let { attribute.baseValue = it.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue }
        attributes.applyAttributeModifiers(stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND))

        return attribute.attributeValue

    }

    fun onImpact(result: RayTraceResult) {
        if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
            val entity = result.entityHit
            if (entity != null) {
                val damageSource = thrower?.let { DamageSource.causeIndirectDamage(this, it) }
                        ?: DamageSource.causeThrownDamage(this, this)
                entity.attackEntityFrom(damageSource, getDamage().toFloat())

                applyToEntity(entity, ItemDeagAnam.getType(stack))
            }
        } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            val hitPos = result.blockPos
            xTile = hitPos.x
            yTile = hitPos.y
            zTile = hitPos.z
            val stateAt = world.getBlockState(hitPos)
            motionX = result.hitVec.x - posX
            motionY = result.hitVec.y - posY
            motionZ = result.hitVec.z - posZ
            val magnitude = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
            posX -= motionX / magnitude * 0.05
            posY -= motionY / magnitude * 0.05
            posZ -= motionZ / magnitude * 0.05
            playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f))
            inGround = true

            throwableShake = 7

            if (stateAt.material != Material.AIR)
                inTile?.onEntityCollision(world, hitPos, stateAt, this)
        }
    }

    fun applyToEntity(entity: Entity, type: DeagAnamType) {

    }

}
