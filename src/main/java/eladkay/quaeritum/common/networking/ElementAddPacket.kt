package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
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
            SpellParser(ElementHandler.getReagentsTyped(player)).cast(player)
            // todo fwoosh
            ElementHandler.clearReagents(player)
            return
        }
        val result = ElementHandler.addReagents(player, EnumSpellElement.values()[idx % EnumSpellElement.values().size])
        when (result) {
            EnumActionResult.SUCCESS -> Unit // todo woosh
            else -> Unit // todo breaking sound
        }
    }
}
