package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.features.kotlin.safeCast
import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.internal.IInternalHandler
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.common.networking.ElementSyncPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTPrimitive

/**
 * @author WireSegal
 * Created at 11:56 AM on 7/29/17.
 */
object QuaeritumInternalHandler : IInternalHandler {
    override fun syncAnimusData(player: EntityPlayer) {
        if (player is EntityPlayerMP) {
            val elements = ElementHandler.getReagents(player)
            val arr = (0 until elements.tagCount())
                    .map { elements[it] }
                    .map { EnumSpellElement.values()[it.safeCast(NBTPrimitive::class.java).int % EnumSpellElement.values().size] }
                    .toTypedArray()
            PacketHandler.NETWORK.sendToDimension(ElementSyncPacket(arr, player.entityId), player.world.provider.dimension)
        }
    }
}
