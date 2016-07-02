package eladkay.quaritum.api.rituals;

import com.google.common.collect.Lists;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public final class PositionedBlock {
    public BlockPos pos;
    private IBlockState state;
    public List<IProperty> toCompare = Lists.newArrayList();

    private PositionedBlock(@Nullable IBlockState state, int x, int y, int z) {
        pos = new BlockPos(x, y, z);
    }

    public PositionedBlock(@Nullable IBlockState state, BlockPos pos) {
        this.pos = pos;
    }

    public static PositionedBlock constructPosChalk(IBlockState state, int x, int y, int z) {
        return new PositionedBlock(state, x, y, z);
    }

    public static PositionedBlock constructPosChalk(IBlockState state, BlockPos pos) {
        return new PositionedBlock(state, pos);
    }

    public PositionedBlock copy() {
        return new PositionedBlock(state, pos);
    }

    public IBlockState getState() {
        return state;
    }

    public PositionedBlock setState(IBlockState state) {
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
