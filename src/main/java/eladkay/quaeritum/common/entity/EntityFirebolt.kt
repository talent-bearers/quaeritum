package eladkay.quaeritum.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 8:38 PM on 8/12/17.
 */
class EntityFirebolt : EntityBaseProjectile {
    constructor(worldIn: World) : super(worldIn)
    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)
    constructor(worldIn: World, shooter: EntityLivingBase) : super(worldIn, shooter)

    override fun getDefaultDamageSource() = causeFireballDamage(null)
    override fun getShotDamageSource(shooter: Entity) = causeFireballDamage(shooter)

    override fun onUpdate() {
        super.onUpdate()
        setFire(100)
    }

    override fun onImpactBlock(hitVec: Vec3d, side: EnumFacing, position: BlockPos) {
        super.onImpactBlock(hitVec, side, position)
        val pos = position.offset(side)
        if (world.getBlockState(pos).block.isReplaceable(world, pos))
            world.setBlockState(pos, Blocks.FIRE.defaultState)
    }

    fun causeFireballDamage(indirectEntityIn: Entity?): DamageSource =
            if (indirectEntityIn == null) EntityDamageSource("onFire", this).setFireDamage().setProjectile()
            else EntityDamageSourceIndirect("fireball", this, indirectEntityIn).setFireDamage().setProjectile()

}
