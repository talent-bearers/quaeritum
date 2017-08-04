package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 12:04 PM on 7/29/17.
 */
@PacketRegister(Side.CLIENT)
class ElementSyncPacket(@Save var list: Array<EnumSpellElement> = arrayOf(), @Save var eid: Int = 0) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val player = LibrarianLib.PROXY.getClientPlayer().world.getEntityByID(eid)
        if (player is EntityPlayer) ElementHandler.setReagents(player, *list)
    }
}
