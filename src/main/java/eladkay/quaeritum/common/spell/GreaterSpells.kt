package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.EnumSpellType.*
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.potions.PotionEmbodiment
import eladkay.quaeritum.common.potions.PotionWrath
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.init.MobEffects
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.RayTraceResult

/**
 * @author WireSegal
 * Created at 8:37 PM on 7/31/17.
 */
object GreaterSpells {

    // Greater spells are those with three, or four symbols. They don't cost anything extra to have multiples.

    fun applyShiftedBuff(potion: Potion, player: EntityLivingBase, trailing: Int, amplifier: Boolean) {
        if (!player.canBeHitWithPotion()) return
        val potionEffect = player.removeActivePotionEffect(potion) ?: PotionEffect(potion)
        player.addPotionEffect(PotionEffect(potion, Math.max(5 + (trailing * 5), 1) * (if (amplifier) 20 else 25) + potionEffect.duration,
                if (amplifier) trailing / 2 else 0, true, true))
    }

    init {
        EpicSpells

        SpellParser.registerSpell(arrayOf(AIR, ENTROPY, EARTH), EVOCATION, "sparkbolt") { player, trailing, _ ->
            val lookedAt = RaycastUtils.getEntityLookedAt(player, 32.0 + trailing * 6.0)
            val block = RaycastUtils.raycast(player, 32.0 + trailing * 6.0)
            if (lookedAt != null)
                player.world.addWeatherEffect(EntityLightningBolt(player.world,
                        lookedAt.posX,
                        lookedAt.posY,
                        lookedAt.posZ, false))
            else if (block != null)
                if (block.typeOfHit == RayTraceResult.Type.MISS)
                    player.world.addWeatherEffect(EntityLightningBolt(player.world,
                            player.posX + player.lookVec.x * (32.0 + trailing * 6.0),
                            player.posY + player.lookVec.y * (32.0 + trailing * 6.0),
                            player.posZ + player.lookVec.z * (32.0 + trailing * 6.0), false))
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
            applyShiftedBuff(PotionEmbodiment, player, trailing + 3, true)
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, FIRE), ALTERATION, "weaponry") { player, trailing, _ ->
            applyShiftedBuff(PotionWrath, player, trailing + 3, true)
        }

        SpellParser.registerSpell(arrayOf(FIRE, WATER, EARTH, AIR), EVOCATION, "gatecrash") { player, trailing, _ ->
            // todo giant ball of destructive energy
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, SOUL, WATER, EARTH), RESTORATION, "true_heal") { player, trailing, _ ->
            applyShiftedBuff(MobEffects.REGENERATION, player, trailing + 3, true)
            applyShiftedBuff(MobEffects.SATURATION, player, 0, false)
            val entity = RaycastUtils.getEntityLookedAt(player)
            if (entity != null && entity is EntityLivingBase)
                applyShiftedBuff(MobEffects.REGENERATION, entity, trailing + 3, true)
        }
    }
}
