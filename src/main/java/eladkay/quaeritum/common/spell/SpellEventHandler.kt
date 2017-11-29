package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.internal.InternalHandler
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.common.item.ItemEvoker
import eladkay.quaeritum.common.networking.LeftClickMessage
import eladkay.quaeritum.common.potions.PotionVoidbind
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumActionResult
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * @author WireSegal
 * Created at 12:43 PM on 7/29/17.
 */
object SpellEventHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun tick(evt: TickEvent.ClientTickEvent) {
        if (evt.phase != TickEvent.Phase.END) return
        lastTickCanceled = canceled
        canceled = false
    }

    var lastTickCanceled = false
    var canceled = false

    @SubscribeEvent
    fun leftClick(evt: PlayerInteractEvent.LeftClickBlock) {
        if (ItemEvoker.hasEvocation(evt.entityPlayer.heldItemMainhand)) {
            evt.isCanceled = true
            canceled = true
        }

        if (!lastTickCanceled)
            PacketHandler.NETWORK.sendToServer(LeftClickMessage())
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

        if (PotionVoidbind.hasEffect(player)) {
            val look = player.lookVec
            val speedVec = look
                    .scale(0.75)
                    .addVector(player.motionX, player.motionY, player.motionZ)

            player.motionX = speedVec.x
            player.motionY = speedVec.y
            player.motionZ = speedVec.z
            player.velocityChanged = true
        }

        val stack = player.heldItemMainhand
        if (ItemEvoker.hasEvocation(stack)) {
            val evocation = ItemEvoker.getEvocationFromStack(stack)
            if (reagents.isEmpty() && ElementHandler.probeReagents(player, *evocation) == EnumActionResult.SUCCESS) {
                reagents = evocation
                clear = false
            }
        }

        if (player.isSneaking || ItemEvoker.hasEvocation(stack)) {

            SpellParser(reagents).cast(player)
            // todo fwoosh
            if (clear)
                ElementHandler.clearReagents(player)
        }
    }
}
