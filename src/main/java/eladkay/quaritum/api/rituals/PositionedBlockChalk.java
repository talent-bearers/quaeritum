package eladkay.quaritum.api.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PositionedBlockChalk extends PositionedBlock {
    public PositionedBlockChalk(@Nullable EnumDyeColor state, @Nonnull BlockPos pos) {
        super(getStateFromColor(state), pos, state == null ? null : Lists.newArrayList(BlockChalk.COLOR));
    }

    @Nonnull
    public static IBlockState getStateFromColor(@Nullable EnumDyeColor chalkColor) {
        if (chalkColor != null)
            return ModBlocks.chalk.getStateFromMeta(chalkColor.ordinal());
        else
            return ModBlocks.tempest.getDefaultState();
    }
}
