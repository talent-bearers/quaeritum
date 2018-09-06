package eladkay.quaeritum.common.entity

import com.teamwizardry.librarianlib.features.kotlin.createFloatKey
import com.teamwizardry.librarianlib.features.kotlin.createIntKey
import com.teamwizardry.librarianlib.features.kotlin.managedValue
import com.teamwizardry.librarianlib.features.kotlin.with
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory

/**
 * @author WireSegal
 * Created at 8:38 PM on 8/12/17.
 */
class EntityGatecrash : EntityBaseProjectile {

    constructor(worldIn: World) : super(worldIn)
    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)
    constructor(worldIn: World, shooter: EntityLivingBase) : super(worldIn, shooter)

    companion object {
        val TICKS_SINCE_IMPACT = EntityGatecrash::class.createIntKey()
        val POWER = EntityGatecrash::class.createFloatKey()
    }

    var power by managedValue(POWER)
    var impactTime by managedValue(TICKS_SINCE_IMPACT)
    val impacted get() = impactTime < 0

    override fun entityInit() {
        super.entityInit()
        with(TICKS_SINCE_IMPACT, -1)
        with(POWER, 1f)
    }

    override fun getDefaultDamageSource(): DamageSource? = null
    override fun getShotDamageSource(shooter: Entity): DamageSource? = null

    override fun onImpactEntity(entity: Entity, successful: Boolean) = impact(positionVector)
    override fun onImpactBlock(hitVec: Vec3d, side: EnumFacing, position: BlockPos) = impact(hitVec)

    override fun onUpdate() {
        if (impacted) {
            motionX = 0.0
            motionY = 0.0
            motionZ = 0.0
        }

        super.onUpdate()

        if (impacted && !world.isRemote) {
            if (impactTime++ == 20) {
                val explosion = Explosion(world, this, posX, posY, posZ, power, false, true)
                if (!ForgeEventFactory.onExplosionStart(world, explosion)) {
                    explosion.doExplosionA()
                    for (pos in explosion.affectedBlockPositions) {
                        val state = world.getBlockState(pos)
                        val block = state.block

                        if (state.material != Material.AIR) {
                            if (block.canDropFromExplosion(explosion))
                                block.dropBlockAsItemWithChance(world, pos, state, 1 / power, 0)

                            block.onBlockExploded(world, pos, explosion)
                        }
                    }
                }
            } else if (impactTime > 30)
                setDead()
        }

    }

    fun impact(position: Vec3d) {
        if (!impacted) {
            impactTime = 0
            motionX = 0.0
            motionY = 0.0
            motionZ = 0.0
            posX = position.x
            posY = position.y
            posZ = position.z
        }
    }

}
