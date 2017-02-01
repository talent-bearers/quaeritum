package eladkay.quaeritum.api.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public interface IWork {
    @NotNull
    String getUnlocalizedName();

    boolean updateTick(@NotNull World world, @NotNull BlockPos pos, @NotNull TileEntity tile, long ticksExisted);

    boolean initialTick(@NotNull World world, @NotNull BlockPos pos, @NotNull TileEntity tile, @Nullable EntityPlayer player);

    boolean canRitualRun(@NotNull World world, @NotNull BlockPos pos, @NotNull TileEntity tile);

    void buildPositions(@NotNull List<PositionedBlock> chalks);

    @Nullable
    IDiagram getDiagramCounterpart();

    default void constructBook() {
        //NO-OP
    }
}
