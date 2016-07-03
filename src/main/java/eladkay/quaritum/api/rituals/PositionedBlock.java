package eladkay.quaritum.api.rituals;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PositionedBlock {
    @Nonnull
    public BlockPos pos;
    @Nonnull
    private IBlockState state;
    @Nullable
    private List<IProperty> toCompare;

    public PositionedBlock(@Nonnull IBlockState state, @Nonnull BlockPos pos, @Nullable List<IProperty> toCompare) {
        this.state = state;
        this.pos = pos;
        this.toCompare = toCompare;
    }

    public PositionedBlock(@Nonnull IBlockState state, @Nonnull BlockPos pos) {
        this(state, pos, null);
    }

    @Nonnull
    public static PositionedBlock constructPosChalk(@Nullable EnumDyeColor color, BlockPos pos) {
        return new PositionedBlockChalk(color, pos);
    }

    @Nullable
    public List<IProperty> getComparables() {
        return toCompare;
    }

    @Nonnull
    public IBlockState getState() {
        return state;
    }

    @Nonnull
    public PositionedBlock setState(@Nonnull IBlockState state) {
        this.state = state;
        return this;
    }

    @Nonnull
    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }


}
