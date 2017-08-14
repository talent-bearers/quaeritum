package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.DamageSource

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
object PotionVampirism : PotionMod(LibNames.VAMPIRISM, false, 0xB22018) {

    override fun isReady(duration: Int, amplifier: Int) = duration % 10 == 0

    override fun performEffect(entity: EntityLivingBase, amplifier: Int) {
        if (!entity.world.isRemote) {
            var healthRemaining = Math.min(entity.maxHealth - entity.health, 5f)
            if (healthRemaining == 0f) return
            entity.world.getEntitiesWithinAABB(EntityLivingBase::class.java, entity.entityBoundingBox.grow(7.0)) {
                it != null && it != entity && it !is EntityArmorStand && it.health > 1 && it.getDistanceSqToEntity(entity) <= 25.0
            }.forEach {
                val taken = Math.min(healthRemaining, it.health - 1)
                it.attackEntityFrom(DamageSource.MAGIC, taken)
                entity.heal(taken)
                healthRemaining -= taken
                // todo masquerade-style lightning effect
            }
        }
    }
}
