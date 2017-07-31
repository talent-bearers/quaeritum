package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.kotlin.safeCast
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.util.EnumActionResult
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 12:04 PM on 7/29/17.
 */
@PacketRegister(Side.SERVER)
class ElementAddPacket(@Save var idx: Int = 0) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val player = ctx.serverHandler.player
        if (idx == -1) {
            ElementHandler.clearReagents(player)
            // todo breaking sound
            return
        } else if (idx == -2) {
            val elements = ElementHandler.getReagents(player)
            if (elements.tagCount() == 0) return

            val arr = (0 until elements.tagCount())
                    .map { elements[it] }
                    .map { EnumSpellElement.values()[it.safeCast(NBTPrimitive::class.java).int % EnumSpellElement.values().size] }
                    .toTypedArray()
            SpellParser(arr).cast(player)
            // todo fwoosh
            ElementHandler.clearReagents(player)
            return
        }
        val result = ElementHandler.addReagents(player, EnumSpellElement.values()[idx % EnumSpellElement.values().size])
        when (result[0]) {
            EnumActionResult.SUCCESS -> Unit // todo woosh
            EnumActionResult.PASS -> Unit
            else -> Unit // todo breaking sound
        }
    }
}
