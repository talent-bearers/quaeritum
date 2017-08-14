package eladkay.quaeritum.common.spell

import com.teamwizardry.librarianlib.features.kotlin.setVelocityAndUpdate
import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.World
import java.util.*

/**
 * @author WireSegal
 * *         Created at 10:27 PM on 7/28/17.
 */
object TradeAttumentHandler {

    private data class PositionTrade(val uuid: UUID, val tickStamp: Long)

    private val trades = WeakHashMap<EntityPlayer, PositionTrade>()

    private fun World.getEntityByUUID(uuid: UUID)
            = this.loadedEntityList.firstOrNull { uuid == it.uniqueID }

    fun getTrade(player: EntityPlayer, time: Int): Entity? {
        val trade = trades[player] ?: return null
        val tick = player.world.totalWorldTime - trade.tickStamp
        if (tick > time) return null
        return player.world.getEntityByUUID(trade.uuid)
    }

    fun makeTrade(player: EntityPlayer, time: Int = 3600) {
        var previousTrade = true
        var entity = getTrade(player, time)
        val raycast = RaycastUtils.getEntityLookedAt(player)
        trades.remove(player)
        if (entity == null || entity == raycast) {
            entity = raycast
            previousTrade = false
        }
        if (entity == null) return

        swap(player, entity)
        if (!previousTrade)
            trades[player] = PositionTrade(entity.uniqueID, player.world.totalWorldTime)
    }

    fun swap(player: EntityPlayer, other: Entity) {
        val otherX = other.posX
        val otherY = other.posY
        val otherZ = other.posZ
        val otherMotionX = other.motionX
        val otherMotionY = other.motionY
        val otherMotionZ = other.motionZ
        val otherYaw = other.rotationYaw
        val otherPitch = other.rotationPitch
        val otherFallDistance = other.fallDistance

        other.fallDistance = player.fallDistance
        if (other is EntityPlayerMP)
            other.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch)
        else
            other.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch)
        other.setVelocityAndUpdate(player.motionX, player.motionY, player.motionZ)

        player.fallDistance = otherFallDistance
        if (player is EntityPlayerMP)
            player.connection.setPlayerLocation(otherX, otherY, otherZ, otherYaw, otherPitch)
        else
            player.setPositionAndRotation(otherX, otherY, otherZ, otherYaw, otherPitch)
        player.setVelocityAndUpdate(otherMotionX, otherMotionY, otherMotionZ)
    }
}
