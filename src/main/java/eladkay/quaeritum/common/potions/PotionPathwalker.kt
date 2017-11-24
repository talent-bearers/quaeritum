package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import eladkay.quaeritum.common.entity.EntityDrill
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.math.BlockPos

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
object PotionPathwalker : PotionMod(LibNames.PATHWALKER, false, 0x50CE23) {

    override fun isReady(duration: Int, amplifier: Int) = true

    override fun performEffect(entity: EntityLivingBase, amplifier: Int) {
        if (!entity.world.isRemote)
            BlockPos.getAllInBox(entity.position.add(-4, 0, -4), entity.position.add(4, 5, 4))
                    .filter { it.distanceSq(entity.position) <= 16.0 }
                    .forEach { EntityDrill.dropBlock(entity, entity.world, it, false) }

        val dist = -0.05
        val shift = 0.175

        if (entity.world.containsAnyLiquid(entity.entityBoundingBox.offset(0.0, dist + shift, 0.0)) && entity.motionY < 0.5) {
            entity.motionY += 0.15
            entity.fallDistance = 0f
        } else if (entity.world.containsAnyLiquid(entity.entityBoundingBox.offset(0.0, dist, 0.0)) && entity.motionY < 0.0) {
            entity.motionY = 0.0
            entity.fallDistance = 0f
            entity.onGround = true
        } else if (entity.world.containsAnyLiquid(entity.entityBoundingBox.offset(0.0, dist + entity.motionY - 0.05, 0.0)) && entity.motionY < 0.0) {
            entity.setPosition(entity.posX, Math.floor(entity.posY), entity.posZ)
            entity.motionY /= 5
            entity.fallDistance = 0f
            entity.onGround = true
        }
    }
}
