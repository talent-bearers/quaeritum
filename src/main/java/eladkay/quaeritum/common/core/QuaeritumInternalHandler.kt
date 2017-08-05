package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.internal.IInternalHandler
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.common.networking.ElementSyncPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP

/**
 * @author WireSegal
 * Created at 11:56 AM on 7/29/17.
 */
object QuaeritumInternalHandler : IInternalHandler {
    override fun syncAnimusData(player: EntityPlayer) {
        if (player is EntityPlayerMP)
            PacketHandler.NETWORK.sendToDimension(ElementSyncPacket(
                    ElementHandler.getReagentsTyped(player), player.entityId), player.world.provider.dimension)
    }
}
