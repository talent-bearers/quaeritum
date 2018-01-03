package eladkay.quaeritum.common

import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.common.item.ItemDrive
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.item.ItemStack
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

    override val color = Color(0x805920)
}

class ItemVibrancyDrive : ItemDrive("vibrancy", EnumAnimusTier.VERDIS) {
    override fun affectEntity(stack: ItemStack, player: EntityPlayer, count: Int, target: EntityLivingBase) {
        target.heal(0.75f)
        (target as? EntityPlayer)?.foodStats?.addStats(1, 0.5f)
    }

    override val color = Color(0x588020)
}
