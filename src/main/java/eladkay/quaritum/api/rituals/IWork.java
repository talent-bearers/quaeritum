package eladkay.quaritum.api.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IWork {
    @Nonnull
    String getUnlocalizedName();

    boolean updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile, long ticksExisted);

    boolean initialTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile, @Nullable EntityPlayer player);

    boolean canRitualRun(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile);

    void buildPositions(@Nonnull List<PositionedBlock> chalks);

    @Nullable
    IDiagram getDiagramCounterpart();

    default void constructBook() {
        //NO-OP
    }
}
