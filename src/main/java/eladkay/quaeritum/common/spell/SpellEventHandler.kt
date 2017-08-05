package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.internal.InternalHandler
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.item.ItemEvoker
import eladkay.quaeritum.common.networking.LeftClickMessage
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumActionResult
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
        var reagents = ElementHandler.getReagentsTyped(player)
        var clear = true

        val stack = player.heldItemMainhand
        if (stack.item is ItemEvoker) {
            val evocation = ItemEvoker.getEvocationFromStack(stack)
            if (reagents.isEmpty() && ElementHandler.probeReagents(player, *evocation) == EnumActionResult.SUCCESS) {
                reagents = evocation
                clear = false
            }
        }

        if (player.isSneaking || player.heldItemMainhand.item is ItemEvoker) {

            SpellParser(reagents).cast(player)
            // todo fwoosh
            if (clear)
                ElementHandler.clearReagents(player)
        }
    }
}
