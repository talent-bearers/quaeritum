package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
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
            if (healthRemaining == 0f && entity is EntityPlayer)
                healthRemaining = Math.min(20 - entity.foodStats.foodLevel.toFloat(), 5f)
            entity.world.getEntitiesWithinAABB(EntityLivingBase::class.java, entity.entityBoundingBox.grow(7.0)) {
                it != null && it != entity && it.canBeHitWithPotion() && it.getDistanceSq(entity) <= 25.0
            }.forEach {
                        if (it.health > 0.5) {
                            if (healthRemaining != 0f) {
                                val taken = Math.min(healthRemaining, it.health - 0.5f)
                                it.attackEntityFrom(DamageSource.MAGIC, taken)
                                entity.heal(taken)
                                if (entity is EntityPlayer) {
                                    entity.foodStats.addStats(taken.toInt(), 1f)
                                }
                                healthRemaining -= taken
                            }
                        } else {
                            it.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 500, 3))
                            it.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 500, 10))
                        }
                        // todo masquerade-style lightning effect
                    }
        }
    }
}
