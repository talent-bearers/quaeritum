package eladkay.quaritum.api.rituals;

import eladkay.quaritum.common.block.ModBlocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PositionedBlock {
    public static final PositionedBlock BLUEPRINT = new PositionedBlock(ModBlocks.blueprint.getDefaultState(), new BlockPos(0, 0, 0));
    @Nonnull
    public final BlockPos pos;
    @Nonnull
    private final IBlockState state;
    @Nullable
    private final List<IProperty> toCompare;

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
    public BlockPos getPos() {
        return pos;
    }



}
