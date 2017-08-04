package eladkay.quaeritum.common.spell

import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.IAlchemicalSpell
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.item.ItemEvoker
import eladkay.quaeritum.common.potions.PotionRooted
import eladkay.quaeritum.common.spell.BasicSpells.applyPotionBuff
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.text.TextComponentString

/**
 * @author WireSegal
 * Created at 7:57 PM on 8/3/17.
 */
object EpicSpells {

    // Epic spells are those with five, six, seven, or eight symbols.
    // They are extremely powerful, and have names worded as commands (Unbind Thine Soul, Save Them All)

    init {
        SpellParser.registerSpell(arrayOf(FIRE, WATER, AETHER, SOUL, EARTH, AIR)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("Unbind Thine Soul trailing: $trailing total: $total"), false) // debug
            // todo infuse self with void energy
        }

        SpellParser.registerSpell(arrayOf(CONNECTION, FIRE, SPIRIT, SOUL, WATER, EARTH)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("Save Them All trailing: $trailing total: $total"), false) // debug
            player.world.getEntitiesWithinAABB(EntityPlayer::class.java, player.entityBoundingBox.grow(25.0)) {
                it != null && it != player && it.getDistanceSqToEntity(player) < 625.0
            }.forEach {
                it.heal(trailing * 5f + 5f)
                applyPotionBuff(MobEffects.SPEED, player, trailing + 4, 1, true)
                applyPotionBuff(MobEffects.JUMP_BOOST, player, trailing + 4, 1, true)
                applyPotionBuff(MobEffects.STRENGTH, player, trailing + 4, 1, true)
                applyPotionBuff(MobEffects.RESISTANCE, player, trailing + 4, 1, true)
            }

            player.addPotionEffect(PotionEffect(MobEffects.WITHER, 500, 3))
            player.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 500, 3))
            player.addPotionEffect(PotionEffect(PotionRooted, 500))
            player.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 500, 3))
            player.addPotionEffect(PotionEffect(MobEffects.HUNGER, 500, 3))
        }

        SpellParser.registerSpell(arrayOf(AIR, AETHER, FORM, FLOW, ENTROPY)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("Release The Heavens trailing: $trailing total: $total"), false) // debug
            // todo tornado
        }

        SpellParser.registerSpell(arrayOf(EARTH, SPIRIT, ENTROPY, FIRE, FLOW)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("Walk The Path trailing: $trailing total: $total"), false) // debug
            // todo blocks fall away before you, you can walk through mountains to make a tunnel
        }

        SpellParser.registerSpell(arrayOf(CONNECTION, SPIRIT, ENTROPY, METAL, WATER)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("Steal Their Strength trailing: $trailing total: $total"), false) // debug
            // todo vampirism buff, aoe life drain
        }

        val pattern = arrayOf(CONNECTION, AETHER, SPIRIT, FIRE, AIR, WATER)
        SpellParser.registerSpell(object : IAlchemicalSpell {
            override fun getPattern() = pattern

            override fun performEffect(player: EntityPlayer, trailing: Int, total: Int) {
                player.sendStatusMessage(TextComponentString("Give Them Hope trailing: $trailing total: $total"), false) // debug
                var evoker = player.heldItemMainhand
                if (evoker.item !is ItemEvoker)
                    evoker = player.heldItemOffhand
                if (evoker.item is ItemEvoker) {
                    val evocation = ItemEvoker.getEvocationFromStack(evoker)
                    val parser = SpellParser(evocation)
                    if (parser.spells.any { it.spell != this })
                        player.world.getEntitiesWithinAABB(EntityPlayer::class.java, player.entityBoundingBox.grow(25.0)) {
                            it != null && it != player && it.getDistanceSqToEntity(player) < 625.0
                        }.forEach {
                            ElementHandler.setReagents(player, *evocation)
                        }
                }
            }
        })

    }

}
