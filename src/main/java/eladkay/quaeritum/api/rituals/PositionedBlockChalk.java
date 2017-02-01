package eladkay.quaeritum.api.rituals;

import com.google.common.collect.Lists;
import eladkay.quaeritum.common.block.base.BlockModColored;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PositionedBlockChalk extends PositionedBlock {
    public static Block chalk;
    public static Block tempest;
    public PositionedBlockChalk(@Nullable EnumDyeColor state, @NotNull BlockPos pos) {
        super(getStateFromColor(state), pos, state == null ? null : Lists.newArrayList(BlockModColored.Companion.getCOLOR())); // todo don't reference main from api
    }

    @NotNull
    public static IBlockState getStateFromColor(@Nullable EnumDyeColor chalkColor) {
        if (chalkColor != null)
            return chalk.getStateFromMeta(chalkColor.ordinal());
        else
            return tempest.getDefaultState();
    }
}
