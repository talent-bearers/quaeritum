package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 9:56 PM on 12/25/17.
 */
@PacketRegister(Side.CLIENT)
class MessageTowerApplied(var value: Long = 0L) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        if (value == Long.MAX_VALUE)
            LibrarianLib.PROXY.getClientPlayer().entityData.removeTag("quaeritum-tower")
        else
            LibrarianLib.PROXY.getClientPlayer().entityData.setLong("quaeritum-tower", value)
    }
}
