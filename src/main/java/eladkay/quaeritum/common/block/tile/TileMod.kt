package eladkay.quaeritum.common.block.tile

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer

/**
 * @author WireSegal
 * *         Created at 12:57 PM on 6/9/16.
 */
abstract class TileMod : TileEntity(), ITickable {

    override fun shouldRefresh(world: World?, pos: BlockPos?, oldState: IBlockState, newState: IBlockState): Boolean {
        return oldState.block !== newState.block
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        writeCustomNBT(compound)
        super.writeToNBT(compound)

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        readCustomNBT(compound)
        super.readFromNBT(compound)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(super.getUpdateTag())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, -999, updateTag)
    }

    abstract fun writeCustomNBT(compound: NBTTagCompound)

    abstract fun readCustomNBT(compound: NBTTagCompound)

    override fun onDataPacket(net: NetworkManager?, pkt: SPacketUpdateTileEntity?) {
        super.onDataPacket(net, pkt)
        readCustomNBT(pkt!!.nbtCompound)
    }

    override fun markDirty() {
        super.markDirty()
        dispatchTEToNearbyPlayers(this)
    }

    override fun update() {
        if (!isInvalid && worldObj.isBlockLoaded(getPos(), !worldObj.isRemote)) {
            updateEntity()
        }
    }

    protected open fun updateEntity() {
        // NO-OP
    }

    companion object {
        fun dispatchTEToNearbyPlayers(tile: TileEntity) {
            if (tile.world is WorldServer) {
                val ws = tile.world as WorldServer
                val packet = tile.updatePacket ?: return

                for (player in ws.playerEntities) {
                    val playerMP = player as EntityPlayerMP

                    if (playerMP.getDistanceSq(tile.pos) < 64 * 64 && ws.playerChunkMap.isPlayerWatchingChunk(playerMP, tile.pos.x shr 4, tile.pos.z shr 4)) {
                        playerMP.connection.sendPacket(packet)
                    }
                }

            }
        }
    }
}
