package eladkay.quaeritum.api.rituals;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PositionedBlock {

    @NotNull
    private final BlockPos pos;
    @NotNull
    private final IBlockState state;
    @Nullable
    private final List<IProperty> toCompare;

    public PositionedBlock(@NotNull IBlockState state, @NotNull BlockPos pos, @Nullable List<IProperty> toCompare) {
        this.state = state;
        this.pos = pos;
        this.toCompare = toCompare;
    }

    public PositionedBlock(@NotNull IBlockState state, @NotNull BlockPos pos) {
        this(state, pos, null);
    }

    @NotNull
    public static PositionedBlock constructPosChalk(@Nullable EnumDyeColor color, BlockPos pos) {
        return new PositionedBlockChalk(color, pos);
    }

    @Nullable
    public final List<IProperty> getComparables() {
        return toCompare;
    }

    @NotNull
    public final IBlockState getState() {
        return state;
    }


    @NotNull
    public final BlockPos getPos() {
        return pos;
    }

    @NotNull
    public final PositionedBlock transform(EnumFacing facing, int mirrorAlongX) {
        switch (facing) {
            case WEST:
                return new PositionedBlock(state, new BlockPos(pos.getZ() * mirrorAlongX, pos.getY(), -pos.getX()), toCompare);
            case SOUTH:
                return new PositionedBlock(state, new BlockPos(-pos.getX() * mirrorAlongX, pos.getY(), -pos.getZ()), toCompare);
            case EAST:
                return new PositionedBlock(state, new BlockPos(-pos.getZ() * mirrorAlongX, pos.getY(), pos.getX()), toCompare);
            default:
                return new PositionedBlock(state, new BlockPos(pos.getX() * mirrorAlongX, pos.getY(), pos.getZ()), toCompare);
        }
    }


}
