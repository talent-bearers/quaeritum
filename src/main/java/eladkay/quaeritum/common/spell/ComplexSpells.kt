package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.entity.EntityFrostshock
import eladkay.quaeritum.common.potions.PotionIronskin
import eladkay.quaeritum.common.potions.PotionRooted
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemDye.applyBonemeal
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextComponentString

/**
 * @author WireSegal
 * Created at 8:37 PM on 7/31/17.
 */
object ComplexSpells {
    fun applyBuffToOther(potion: Potion, player: EntityPlayer, trailing: Int, total: Int, amplifier: Boolean) {
        val entity = RaycastUtils.getEntityLookedAt(player)
        if (entity != null && entity is EntityLivingBase)
            BasicSpells.applyPotionBuff(potion, entity, trailing, total, amplifier)
    }

    // Complex spells are those with two symbols. They are less affected by total spells.

    init {
        GreaterSpells

        SpellParser.registerSpell(arrayOf(EARTH, CONNECTION)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("resist other trailing: $trailing total: $total"), false) // debug
            applyBuffToOther(MobEffects.RESISTANCE, player, trailing, total, true)
        }

        SpellParser.registerSpell(arrayOf(WATER, CONNECTION)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("heal other trailing: $trailing total: $total"), false) // debug
            val entity = RaycastUtils.getEntityLookedAt(player)
            if (entity != null && entity is EntityLivingBase) {
                if (entity is EntityPlayer) {
                    if (entity.foodStats.foodLevel > 0) {
                        entity.heal(trailing * 3f + 2f)
                        entity.addPotionEffect(PotionEffect(MobEffects.HUNGER, total * 20, total * 2))
                    }
                } else entity.heal(trailing * 3f + 2f)
                if (total > 3) entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 500, total - 2))
            }
        }

        SpellParser.registerSpell(arrayOf(METAL, CONNECTION)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("ironskin other trailing: $trailing total: $total"), false) // debug
            applyBuffToOther(PotionIronskin, player, trailing, total / 2, false)
        }

        SpellParser.registerSpell(arrayOf(FORM, CONNECTION)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("dissipate other trailing: $trailing total: $total"), false) // debug
            applyBuffToOther(MobEffects.INVISIBILITY, player, trailing, total / 2, false)
        }

        SpellParser.registerSpell(arrayOf(FLOW, CONNECTION)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("flow other trailing: $trailing total: $total"), false) // debug
            applyBuffToOther(MobEffects.JUMP_BOOST, player, trailing, total / 2, true)
            applyBuffToOther(MobEffects.SPEED, player, trailing, total / 2, true)
        }

        SpellParser.registerSpell(arrayOf(EARTH, WATER)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("bonemeal trailing: $trailing total: $total"), false) // debug
            val block = RaycastUtils.raycast(player, (player as EntityPlayerMP).interactionManager.blockReachDistance + 3, false)
            if (block != null && block.typeOfHit == RayTraceResult.Type.BLOCK) {
                val pos = block.blockPos
                for (i in 0..2 * trailing / total)
                    if (applyBonemeal(ItemStack(Items.DYE, 1, 15), player.world, pos, player))
                        player.world.playEvent(2005, pos, 0)
            }
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("wall of earth trailing: $trailing total: $total"), false) // debug
            // todo wall of earth
        }

        SpellParser.registerSpell(arrayOf(METAL, FORM)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("wall of iron trailing: $trailing total: $total"), false) // debug
            // todo wall of iron bars
        }

        SpellParser.registerSpell(arrayOf(EARTH, FLOW)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("drill trailing: $trailing total: $total"), false) // debug
            // todo drill into earth
        }

        SpellParser.registerSpell(arrayOf(SOUL, SPIRIT)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("strike fear trailing: $trailing total: $total"), false) // debug
            var i = Math.max(2 + trailing - total / 2, 1)
            player.world.getEntitiesInAABBexcluding(player, player.entityBoundingBox.grow(5.0)) {
                it != null && it !is EntityPlayer && it.getDistanceSqToEntity(player) < 25.0
            }.forEach {
                if (it is EntityLivingBase && i-- != 0)
                    it.addPotionEffect(PotionEffect(PotionRooted, 200))
            }
        }

        SpellParser.registerSpell(arrayOf(WATER, FORM)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("ice shock trailing: $trailing total: $total"), false) // debug
            val frostshock = EntityFrostshock(player.world, player)
            val shiftTotal = Math.max(1, total - 1)
            val velocity = Math.min(2f, 3f * shiftTotal / (trailing + 1))
            val inaccuracy = Math.max(shiftTotal - trailing.toFloat(), 0f)
            val damage = 3.5f + 3.5f * trailing / shiftTotal
            frostshock.damage = damage
            frostshock.gravity = velocity * 0.0125f
            frostshock.knockback = 0.0f
            frostshock.setAim(player, player.rotationPitch, player.rotationYaw, Math.min(velocity, 2f), inaccuracy)
            player.world.spawnEntity(frostshock)
        }
    }
}
