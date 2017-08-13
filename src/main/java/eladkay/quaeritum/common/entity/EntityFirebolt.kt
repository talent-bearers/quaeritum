package eladkay.quaeritum.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 8:38 PM on 8/12/17.
 */
class EntityFirebolt : EntityBaseProjectile {
    constructor(worldIn: World) : super(worldIn)
    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)
    constructor(worldIn: World, shooter: EntityLivingBase) : super(worldIn, shooter)

    override fun getDefaultDamageSource(): DamageSource {
        return DamageSource.IN_FIRE
    }

    override fun getShotDamageSource(shooter: Entity): DamageSource {
        return DamageSource.IN_FIRE // todo
    }

    override fun onImpactEntity(entity: Entity, successful: Boolean) {
        super.onImpactEntity(entity, successful)
        entity.setFire(1000)
    }
}
