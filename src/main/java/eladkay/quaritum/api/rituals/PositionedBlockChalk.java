package eladkay.quaritum.api.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.common.block.base.BlockModColored;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PositionedBlockChalk extends PositionedBlock {
    public static Block chalk;
    public static Block tempest;
    public PositionedBlockChalk(@Nullable EnumDyeColor state, @Nonnull BlockPos pos) {
        super(getStateFromColor(state), pos, state == null ? null : Lists.newArrayList(BlockModColored.Companion.getCOLOR())); // todo don't reference main from api
    }

    @Nonnull
    public static IBlockState getStateFromColor(@Nullable EnumDyeColor chalkColor) {
        if (chalkColor != null)
            return chalk.getStateFromMeta(chalkColor.ordinal());
        else
            return tempest.getDefaultState();
    }
}
