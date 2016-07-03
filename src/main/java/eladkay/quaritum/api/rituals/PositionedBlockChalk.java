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
    @Nonnull
    public static IBlockState getStateFromColor(@Nonnull EnumDyeColor chalkColor) {
        return ModBlocks.chalk.getStateFromMeta(chalkColor.ordinal());
    }

    public PositionedBlockChalk(@Nonnull EnumDyeColor state, @Nonnull BlockPos pos) {
        super(getStateFromColor(state), pos, Lists.newArrayList(BlockChalk.COLOR));
    }
}
