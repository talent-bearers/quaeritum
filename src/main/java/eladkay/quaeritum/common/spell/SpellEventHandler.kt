package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.kotlin.safeCast
import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.internal.InternalHandler
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.networking.LeftClickMessage
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTPrimitive
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 12:43 PM on 7/29/17.
 */
object SpellEventHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun leftClick(evt: PlayerInteractEvent.LeftClickEmpty) {
        PacketHandler.NETWORK.sendToServer(LeftClickMessage())
    }

    @SubscribeEvent
    fun joinWorld(evt: EntityJoinWorldEvent) {
        val entity = evt.entity
        if (entity is EntityPlayer)
            InternalHandler.getInternalHandler().syncAnimusData(entity)
    }

    fun leftClickServer(player: EntityPlayer) {
        if (player.isSneaking) {
            val elements = ElementHandler.getReagents(player)
            if (elements.tagCount() == 0) return

            val arr = (0 until elements.tagCount())
                    .map { elements[it] }
                    .map { EnumSpellElement.values()[it.safeCast(NBTPrimitive::class.java).int % EnumSpellElement.values().size] }
                    .toTypedArray()
            SpellParser(arr).cast(player)
            // todo fwoosh
            ElementHandler.clearReagents(player)
        }
    }
}
