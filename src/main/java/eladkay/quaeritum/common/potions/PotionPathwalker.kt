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
                    .filter { entity.getDistanceSqToCenter(it) <= 16.0 }
                    .forEach { EntityDrill.dropBlock(entity, entity.world, it, false) }
    }
}
