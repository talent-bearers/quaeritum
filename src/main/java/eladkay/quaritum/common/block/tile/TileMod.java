package eladkay.quaritum.common.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 *         Created at 12:57 PM on 6/9/16.
 */
public abstract class TileMod extends TileEntity implements ITickable {
    public static void dispatchTEToNearbyPlayers(TileEntity tile) {
        if (tile.getWorld() instanceof WorldServer) {
            WorldServer ws = ((WorldServer) tile.getWorld());
            SPacketUpdateTileEntity packet = tile.getUpdatePacket();

            if (packet == null)
                return;

            for (EntityPlayer player : ws.playerEntities) {
                EntityPlayerMP playerMP = ((EntityPlayerMP) player);

                if (playerMP.getDistanceSq(tile.getPos()) < 64 * 64
                        && ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4)) {
                    playerMP.connection.sendPacket(packet);
                }
            }

        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        writeCustomNBT(compound);
        super.writeToNBT(compound);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        readCustomNBT(compound);
        super.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(super.getUpdateTag());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, -999, getUpdateTag());
    }

    abstract public void writeCustomNBT(NBTTagCompound compound);

    abstract public void readCustomNBT(NBTTagCompound compound);

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readCustomNBT(pkt.getNbtCompound());
    }

    @Override
    public final void update() {
        if (!isInvalid() && worldObj.isBlockLoaded(getPos(), !worldObj.isRemote)) {
            updateEntity();
        }
    }

    protected void updateEntity() {
        // NO-OP
    }
}
