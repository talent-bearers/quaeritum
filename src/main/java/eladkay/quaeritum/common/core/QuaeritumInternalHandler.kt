package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.api.internal.IInternalHandler
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.common.networking.AnimusSync
import eladkay.quaeritum.common.networking.ElementSyncPacket
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.fml.common.FMLCommonHandler

/**
 * @author WireSegal
 * Created at 11:56 AM on 7/29/17.
 */
object QuaeritumInternalHandler : IInternalHandler {
    override fun syncAnimusData(player: EntityPlayer) {
        if (player is EntityPlayerMP) {
            PacketHandler.NETWORK.sendToDimension(ElementSyncPacket(
                    ElementHandler.getReagentsTyped(player), player.entityId), player.world.provider.dimension)
            PacketHandler.NETWORK.sendTo(AnimusSync(getTrueSaveData().writeToNBT(NBTTagCompound())), player)
        }
    }

    fun getTrueSaveData(): QuaeritumSaveData {
        val world = (if (FMLCommonHandler.instance().effectiveSide.isClient)
            ClientRunnable.produce { Minecraft.getMinecraft().world }
        else DimensionManager.getWorld(0)) ?: throw UnsupportedOperationException("There's no world?")
        val mapStorage = world.mapStorage ?: return QuaeritumSaveData()

        var saveData = mapStorage.getOrLoadData(QuaeritumSaveData::class.java, LibMisc.MOD_ID) as? QuaeritumSaveData

        if (saveData == null) {
            saveData = QuaeritumSaveData()
            mapStorage.setData(LibMisc.MOD_ID, saveData)
        }

        return saveData
    }

    override fun getSaveData() = getTrueSaveData().animusData
    override fun markSaveDataDirty() {
        getTrueSaveData().markDirty()
        if (FMLCommonHandler.instance().effectiveSide.isServer)
            PacketHandler.NETWORK.sendToAll(AnimusSync(getTrueSaveData().writeToNBT(NBTTagCompound())))
    }
}
