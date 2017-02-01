package eladkay.quaeritum.api.rituals;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class PositionedBlock {

    @NotNull
    public final BlockPos pos;
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
    public List<IProperty> getComparables() {
        return toCompare;
    }

    @NotNull
    public IBlockState getState() {
        return state;
    }


    @NotNull
    public BlockPos getPos() {
        return pos;
    }


}
