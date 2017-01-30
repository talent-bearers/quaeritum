package eladkay.quaeritum.common.item.spells

import baubles.api.BaubleType
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.common.util.times
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.IBaubleSpell
import eladkay.quaeritum.api.spell.ISpellProvider
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

        override fun getSpellName(bauble: ItemStack, slot: Int) =
                TooltipHelper.local(if (ISpellProvider.baubleFromSlot(slot) == BaubleType.RING)
                    "spell.${LibMisc.MOD_ID}.windwalkRing.name"
                else TooltipHelper.local("spell.${LibMisc.MOD_ID}.windwalk.name"))

        override fun onCast(player: EntityPlayer, bauble: ItemStack, slot: Int): Boolean {
            if (ISpellProvider.baubleFromSlot(slot) == BaubleType.RING) {
                val look = player.lookVec
                val speedVec = (look * 0.25).addVector(player.motionX, player.motionY, player.motionZ)
                if (speedVec.lengthSquared() > 9)
                    return false

                player.motionX = speedVec.xCoord
                player.motionY = speedVec.yCoord
                player.motionZ = speedVec.zCoord

                player.fallDistance = 0f
            }
            return true
        }

        override fun getCooldown(player: EntityPlayer, bauble: ItemStack, slot: Int) =
                if (ISpellProvider.baubleFromSlot(slot) == BaubleType.RING) 0 else 1800

        override fun onCooldownTick(player: EntityPlayer, bauble: ItemStack, slot: Int, cooldownRemaining: Int) {
            if (!player.capabilities.isFlying && !player.onGround && !player.isElytraFlying)
                if (cooldownRemaining > 600)
                    player.motionY += 0.06
                else {
                    val shifted = cooldownRemaining / 100.0
                    player.motionY += shifted * shifted / 900.0 + shifted / 300.0
                }
            player.fallDistance = 0f
        }
    }

    override fun getBaubleType(p0: ItemStack?) = BaubleType.TRINKET

    override fun getSpell(bauble: ItemStack, slot: Int) = when (ISpellProvider.baubleFromSlot(slot)) {
        BaubleType.RING, BaubleType.CHARM -> Windwalk
        else -> null
    }
}
