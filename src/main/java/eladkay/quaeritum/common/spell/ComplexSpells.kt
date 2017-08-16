package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.EnumSpellType.*
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.entity.EntityDrill
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

        SpellParser.registerSpell(arrayOf(EARTH, CONNECTION), ALTERATION, "resist_other") { player, trailing, total ->
            applyBuffToOther(MobEffects.RESISTANCE, player, trailing, total, true)
        }

        SpellParser.registerSpell(arrayOf(WATER, CONNECTION), RESTORATION, "heal_other") { player, trailing, total ->
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

        SpellParser.registerSpell(arrayOf(METAL, CONNECTION), ALTERATION, "ironskin_other") { player, trailing, total ->
            applyBuffToOther(PotionIronskin, player, trailing, total / 2, false)
        }

        SpellParser.registerSpell(arrayOf(FORM, CONNECTION), ALTERATION, "dissipate_other") { player, trailing, total ->
            applyBuffToOther(MobEffects.INVISIBILITY, player, trailing, total / 2, false)
        }

        SpellParser.registerSpell(arrayOf(FLOW, CONNECTION), ALTERATION, "flow_other") { player, trailing, total ->
            applyBuffToOther(MobEffects.JUMP_BOOST, player, trailing, total / 2, true)
            applyBuffToOther(MobEffects.SPEED, player, trailing, total / 2, true)
        }

        SpellParser.registerSpell(arrayOf(EARTH, WATER), RESTORATION, "bonemeal") { player, trailing, total ->
            val block = RaycastUtils.raycast(player, (player as EntityPlayerMP).interactionManager.blockReachDistance + 3, false)
            if (block != null && block.typeOfHit == RayTraceResult.Type.BLOCK) {
                val pos = block.blockPos
                for (i in 0..2 * trailing / total)
                    if (applyBonemeal(ItemStack(Items.DYE, 1, 15), player.world, pos, player))
                        player.world.playEvent(2005, pos, 0)
            }
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM), EVOCATION, "earth_wall") { player, trailing, total ->
            // todo wall of earth
        }

        SpellParser.registerSpell(arrayOf(METAL, FORM), EVOCATION, "iron_wall") { player, trailing, total ->
            // todo wall of iron bars
        }

        SpellParser.registerSpell(arrayOf(EARTH, FLOW), EVOCATION, "drill") { player, trailing, total ->
            val drill = EntityDrill(player.world, player)
            val shiftTotal = Math.max(1, total - 1)
            val velocity = Math.min(2f, 3f * shiftTotal / (trailing + 1))
            val inaccuracy = Math.max(shiftTotal - trailing.toFloat(), 0f)
            val blocksBreakable = 10 + 10 * trailing / shiftTotal
            drill.damage = 0f
            drill.gravity = velocity * 0.0125f
            drill.knockback = 0.0f
            drill.blocksLeft = blocksBreakable
            drill.setAim(player, player.rotationPitch, player.rotationYaw, Math.min(velocity, 2f), inaccuracy)
            player.world.spawnEntity(drill)
        }

        SpellParser.registerSpell(arrayOf(SOUL, SPIRIT), ALTERATION, "fear") { player, trailing, total ->
            var i = Math.max(2 + trailing - total / 2, 1)
            player.world.getEntitiesInAABBexcluding(player, player.entityBoundingBox.grow(5.0)) {
                it != null && it !is EntityPlayer && it.getDistanceSqToEntity(player) < 25.0
            }.forEach {
                if (it is EntityLivingBase && i-- != 0)
                    it.addPotionEffect(PotionEffect(PotionRooted, 200))
            }
        }

        SpellParser.registerSpell(arrayOf(WATER, FORM), EVOCATION, "ice_shock") { player, trailing, total ->
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
