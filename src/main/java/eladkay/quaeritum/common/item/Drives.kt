package eladkay.quaeritum.common.item

import eladkay.quaeritum.api.animus.EnumAnimusTier
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import java.awt.Color

/**
 * @author WireSegal
 * Created at 8:34 PM on 1/2/18.
 */
class ItemPassionDrive : ItemDrive("passion", EnumAnimusTier.LUCIS) {
    override fun affectEntity(stack: ItemStack, player: EntityPlayer, count: Int, target: EntityLivingBase) {
        val fakeFireball = EntityLargeFireball(player.world, player, 0.0, 0.0, 0.0)
        target.attackEntityFrom(DamageSource.causeFireballDamage(fakeFireball, player), 2f)
        target.setFire(20)
    }

    override val colors = listOf(Color(0x805920))
}

class ItemVibrancyDrive : ItemDrive("vibrancy", EnumAnimusTier.VERDIS) {
    override fun affectEntity(stack: ItemStack, player: EntityPlayer, count: Int, target: EntityLivingBase) {
        target.heal(0.75f)
        (target as? EntityPlayer)?.foodStats?.addStats(1, 0.5f)
    }

    override val colors = listOf(Color(0x588020))
}

class ItemTwinDrive : ItemDrive("twin", EnumAnimusTier.FERRUS) {
    override fun affectEntity(stack: ItemStack, player: EntityPlayer, count: Int, target: EntityLivingBase) {
        if (target.canBeHitWithPotion()) {
            target.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 20, 1, true, true))
            target.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, 20, 0, true, true))
            target.addPotionEffect(PotionEffect(MobEffects.FIRE_RESISTANCE, 20, 0, true, true))
            target.addPotionEffect(PotionEffect(MobEffects.REGENERATION, 20, 0, true, true))
        }
    }

    override val colors = listOf(Color(0x588020), Color(0x805920))
}
