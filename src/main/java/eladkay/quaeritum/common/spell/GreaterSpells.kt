package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.EnumSpellType.*
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.potions.PotionEmbodiment
import eladkay.quaeritum.common.potions.PotionWrath
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.RayTraceResult

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

        SpellParser.registerSpell(arrayOf(AIR, ENTROPY, EARTH), EVOCATION, "sparkbolt") { player, trailing, _ ->
            val block = RaycastUtils.raycast(player, 32.0 + trailing * 6.0)
            if (block != null)
                if (block.typeOfHit == RayTraceResult.Type.MISS)
                    player.world.addWeatherEffect(EntityLightningBolt(player.world,
                            player.posX + player.lookVec.x * 32,
                            player.posY + player.lookVec.y * 32,
                            player.posZ + player.lookVec.z * 32, false))
                else
                    player.world.addWeatherEffect(EntityLightningBolt(player.world, block.hitVec.x, block.hitVec.y, block.hitVec.z, false))
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, CONNECTION, SOUL), ALTERATION, "trade") { player, trailing, _ ->
            TradeAttumentHandler.makeTrade(player, time = 3600 + trailing * 200)
        }

        SpellParser.registerSpell(arrayOf(SOUL, FORM, AIR), INCARNATION, "fogbind") { player, trailing, _ ->
            // todo create fake block that duplicates block you were looking at
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, WATER), ALTERATION, "protection") { player, trailing, _ ->
            applyShiftedBuff(PotionEmbodiment, player, trailing + 3, 1, true)
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, FIRE), ALTERATION, "weaponry") { player, trailing, _ ->
            applyShiftedBuff(PotionWrath, player, trailing + 3, 1, true)
        }

        SpellParser.registerSpell(arrayOf(FIRE, WATER, EARTH, AIR), EVOCATION, "gatecrash") { player, trailing, _ ->
            // todo giant ball of destructive energy
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, SOUL, WATER, EARTH), RESTORATION, "true_heal") { player, trailing, _ ->
            player.heal(trailing * 8f + 4f)
            val entity = RaycastUtils.getEntityLookedAt(player)
            if (entity != null && entity is EntityLivingBase)
                entity.heal(trailing * 8f + 4f)
        }
    }
}
