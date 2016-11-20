package eladkay.quaeritum.common.item.spells

import baubles.api.BaubleType
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.IBaubleSpell
import eladkay.quaeritum.common.item.base.ItemSpellBauble
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 6:08 PM on 11/5/16.
 */
class ItemWindwalkerCharm : ItemSpellBauble(LibNames.WINDWALKER) {
    object Windwalk : IBaubleSpell {
        override fun getIconStack(bauble: ItemStack, slot: Int) = bauble

        override fun getSpellName(bauble: ItemStack, slot: Int) = TooltipHelper.local("spell.${LibMisc.MOD_ID}.windwalk.name")

        override fun onCast(player: EntityPlayer, bauble: ItemStack, slot: Int) = true // Always succeed

        override fun getCooldown(player: EntityPlayer, bauble: ItemStack, slot: Int) = 1800

        override fun onCooldownTick(player: EntityPlayer, bauble: ItemStack, slot: Int, cooldownRemaining: Int) {
            if (!player.capabilities.isFlying && !player.isElytraFlying) {
                if (cooldownRemaining > 600 && (player.motionY < -0.15 || player.prevPosY == player.posY) && !player.isSneaking) {
                    player.onGround = true
                    player.fallDistance = 0f
                    player.motionY = 0.0
                } else if (cooldownRemaining > 600 && player.isSneaking || cooldownRemaining > 300) {
                    player.fallDistance = 0f
                    player.motionY = Math.max(player.motionY, -0.5)
                }
            }
        }
    }

    override fun getBaubleType(p0: ItemStack?) = BaubleType.CHARM

    override fun getSpell(bauble: ItemStack, slot: Int) = Windwalk
}
