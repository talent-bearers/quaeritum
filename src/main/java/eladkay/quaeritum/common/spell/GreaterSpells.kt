package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.potions.PotionEmbodiment
import eladkay.quaeritum.common.potions.PotionWrath
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextComponentString

/**
 * @author WireSegal
 * Created at 8:37 PM on 7/31/17.
 */
object GreaterSpells {

    // Greater spells are those with three, or four symbols. They don't cost anything extra to have multiples.

    fun applyShiftedBuff(potion: Potion, player: EntityLivingBase, trailing: Int, total: Int, amplifier: Boolean) {
        val potionEffect = player.removeActivePotionEffect(potion) ?: PotionEffect(potion)
        player.addPotionEffect(PotionEffect(potion, Math.max(5 + (trailing * 5) / total, 1) * (if (amplifier) 20 else 25) + potionEffect.duration,
                if (amplifier) trailing / 2 else 0))
    }

    init {
        EpicSpells

        SpellParser.registerSpell(arrayOf(AIR, ENTROPY, EARTH)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("sparkbolt trailing: $trailing total: $total"), false) // debug
            val block = RaycastUtils.raycast(player, 32.0)
            if (block != null)
                if (block.typeOfHit == RayTraceResult.Type.MISS)
                    player.world.addWeatherEffect(EntityLightningBolt(player.world,
                            player.posX + player.lookVec.x * 32,
                            player.posY + player.lookVec.y * 32,
                            player.posZ + player.lookVec.z * 32, false))
                else
                    player.world.addWeatherEffect(EntityLightningBolt(player.world, block.hitVec.x, block.hitVec.y, block.hitVec.z, false))
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, CONNECTION, SOUL)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("trade position trailing: $trailing total: $total"), false) // debug
            TradeAttumentHandler.makeTrade(player, time = 3700 - total * 100 + trailing * 200)
        }

        SpellParser.registerSpell(arrayOf(SOUL, FORM, AIR)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("fogbind trailing: $trailing total: $total"), false) // debug
            // todo create fake block that duplicates block you were looking at
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, WATER)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("embody protection trailing: $trailing total: $total"), false) // debug
            applyShiftedBuff(PotionEmbodiment, player, trailing + 3, 1, true)
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, FIRE)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("embody weaponry trailing: $trailing total: $total"), false) // debug
            applyShiftedBuff(PotionWrath, player, trailing + 3, 1, true)
        }

        SpellParser.registerSpell(arrayOf(FIRE, WATER, EARTH, AIR)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("gatecrash trailing: $trailing total: $total"), false) // debug
            // todo giant ball of destructive energy
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, SOUL, WATER, EARTH)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("true healing trailing: $trailing total: $total"), false) // debug
            player.heal(trailing * 8f + 4f)
            val entity = RaycastUtils.getEntityLookedAt(player)
            if (entity != null && entity is EntityLivingBase)
                entity.heal(trailing * 8f + 4f)
        }
    }
}
