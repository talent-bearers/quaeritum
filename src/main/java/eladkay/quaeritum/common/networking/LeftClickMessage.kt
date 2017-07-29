package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import eladkay.quaeritum.common.spell.SpellEventHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 12:04 PM on 7/29/17.
 */
@PacketRegister(Side.SERVER)
class LeftClickMessage : PacketBase() {
    override fun handle(ctx: MessageContext) {
        SpellEventHandler.leftClickServer(ctx.serverHandler.player)
    }
}
