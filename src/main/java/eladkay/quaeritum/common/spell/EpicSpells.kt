package eladkay.quaeritum.common.spell

import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.EnumSpellType.*
import eladkay.quaeritum.api.spell.IAlchemicalSpell
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.api.spell.SpellParser.registerSpell
import eladkay.quaeritum.common.item.ItemEvoker
import eladkay.quaeritum.common.potions.PotionPathwalker
import eladkay.quaeritum.common.potions.PotionRooted
import eladkay.quaeritum.common.potions.PotionVampirism
import eladkay.quaeritum.common.spell.GreaterSpells.applyShiftedBuff
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect

/**
 * @author WireSegal
 * Created at 7:57 PM on 8/3/17.
 */
object EpicSpells {

    // Epic spells are those with five, six, seven, or eight symbols.
    // They are extremely powerful, and have names worded as commands (Unbind Thine Soul, Save Them All)

    private lateinit var hope: IAlchemicalSpell

    init {
        SpellParser.registerSpell(arrayOf(CONNECTION, FIRE, SPIRIT, SOUL, WATER, EARTH), RESTORATION, "save_them_all") { player, trailing, _ ->
            player.world.getEntitiesWithinAABB(EntityPlayer::class.java, player.entityBoundingBox.grow(25.0)) {
                it != null && it != player && it.getDistanceSqToEntity(player) < 625.0
            }.forEach {
                it.heal(trailing * 5f + 5f)
                applyShiftedBuff(MobEffects.SPEED, player, trailing + 4, 1, true)
                applyShiftedBuff(MobEffects.JUMP_BOOST, player, trailing + 4, 1, true)
                applyShiftedBuff(MobEffects.STRENGTH, player, trailing + 4, 1, true)
                applyShiftedBuff(MobEffects.RESISTANCE, player, trailing + 4, 1, true)
            }

            player.addPotionEffect(PotionEffect(MobEffects.WITHER, 500, 3))
            player.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 500, 3))
            player.addPotionEffect(PotionEffect(PotionRooted, 500))
            player.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 500, 3))
            player.addPotionEffect(PotionEffect(MobEffects.HUNGER, 500, 3))
        }

        SpellParser.registerSpell(arrayOf(AIR, AETHER, FORM, FLOW, ENTROPY), INCARNATION, "release_the_heavens") { player, trailing, _ ->
            // todo tornado
        }

        SpellParser.registerSpell(arrayOf(EARTH, SPIRIT, ENTROPY, FIRE, FLOW), EVOCATION, "walk_the_path") { player, trailing, _ ->
            applyShiftedBuff(PotionPathwalker, player, trailing + 4, 1, false)
        }

        SpellParser.registerSpell(arrayOf(CONNECTION, SPIRIT, ENTROPY, METAL, WATER), ALTERATION, "steal_their_strength") { player, trailing, _ ->
            applyShiftedBuff(PotionVampirism, player, trailing + 4, 1, false)
        }

        SpellParser.registerSpell(arrayOf(FIRE, WATER, SOUL, AETHER, AETHER, SOUL, EARTH, AIR), ALTERATION, "unbind_thine_soul") { player, _, _ ->
            // todo infuse self with void energy
        }

        hope = registerSpell(arrayOf(CONNECTION, AETHER, SPIRIT, FIRE, AIR, WATER, SOUL, FLOW), RESTORATION, "give_them_hope") {
            player, _, _ ->

            var evoker = player.heldItemMainhand
            if (evoker.item !is ItemEvoker)
                evoker = player.heldItemOffhand
            else {
                val evocation = ItemEvoker.getEvocationFromStack(evoker)
                val parser = SpellParser(evocation)
                if (parser.spells.any { it.spell == hope })
                    evoker = player.heldItemOffhand
            }

            if (evoker.item is ItemEvoker) {
                val evocation = ItemEvoker.getEvocationFromStack(evoker)
                val parser = SpellParser(evocation)
                if (parser.spells.none { it.spell == hope })
                    player.world.getEntitiesWithinAABB(EntityPlayer::class.java, player.entityBoundingBox.grow(25.0)) {
                        it != null && it != player && it.getDistanceSqToEntity(player) < 625.0
                    }.forEach {
                        ElementHandler.setReagents(player, *evocation)
                    }
            }
        }

    }

}
