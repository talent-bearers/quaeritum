package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.internal.IInternalHandler
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.common.networking.ElementSyncPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.common.DimensionManager

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

    fun getTrueSaveData(): QuaeritumSaveData {
        val world = DimensionManager.getWorld(0)
        val mapStorage = world?.mapStorage ?: return QuaeritumSaveData()

        var saveData = mapStorage.getOrLoadData(QuaeritumSaveData::class.java, LibMisc.MOD_ID) as? QuaeritumSaveData

        if (saveData == null) {
            saveData = QuaeritumSaveData()
            mapStorage.setData(LibMisc.MOD_ID, saveData)
        }

        return saveData
    }

    override fun getSaveData() = getTrueSaveData().animusData
    override fun markSaveDataDirty() = getTrueSaveData().markDirty()
}
