package eladkay.quaeritum.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 8:38 PM on 8/12/17.
 */
class EntityFrostshock : EntityBaseProjectile {
    constructor(worldIn: World) : super(worldIn)
    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)
    constructor(worldIn: World, shooter: EntityLivingBase) : super(worldIn, shooter)

    override fun getDefaultDamageSource() = causeFrostDamage(null)
    override fun getShotDamageSource(shooter: Entity) = causeFrostDamage(shooter)

    override fun onImpactEntity(entity: Entity, successful: Boolean) {
        super.onImpactEntity(entity, successful)
        if (entity is EntityLivingBase && entity.canBeHitWithPotion())
            entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 200, 1))
    }

    fun causeFrostDamage(indirectEntityIn: Entity?): DamageSource =
            if (indirectEntityIn == null) EntityDamageSource("quaeritum.frost", this).setProjectile()
            else EntityDamageSourceIndirect("quaeritum.frost", this, indirectEntityIn).setProjectile()

}
