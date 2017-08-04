package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.SpellParser
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.text.TextComponentString

/**
 * @author WireSegal
 * Created at 8:37 PM on 7/31/17.
 */
object GreaterSpells {

    // Greater spells are those with three, or four symbols. They don't cost anything extra to have multiples.

    init {
        EpicSpells

        SpellParser.registerSpell(arrayOf(AIR, ENTROPY, EARTH)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("sparkbolt trailing: $trailing total: $total"), false) // debug
            // todo
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, CONNECTION, SOUL)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("trade position trailing: $trailing total: $total"), false) // debug
            // todo
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, WATER)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("embody armor trailing: $trailing total: $total"), false) // debug
            // todo
        }

        SpellParser.registerSpell(arrayOf(EARTH, FORM, METAL, FIRE)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("embody weapon trailing: $trailing total: $total"), false) // debug
            // todo
        }

        SpellParser.registerSpell(arrayOf(FIRE, WATER, EARTH, AIR)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("gatecrash trailing: $trailing total: $total"), false) // debug
            // todo
        }

        SpellParser.registerSpell(arrayOf(SPIRIT, SOUL, WATER, EARTH)) { player, trailing, total ->
            player.sendStatusMessage(TextComponentString("true healing trailing: $trailing total: $total"), false) // debug
            player.heal(trailing * 4f + 2f)
            val entity = RaycastUtils.getEntityLookedAt(player)
            if (entity != null && entity is EntityLivingBase)
                entity.heal(trailing * 4f + 2f)
        }
    }
}
