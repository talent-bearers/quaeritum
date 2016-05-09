package eladkay.quaritum.common.core;

import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class PositionedBlockHelper {
    public static void setStateFromColor(PositionedBlock block, EnumDyeColor chalkColor) {
        if (chalkColor.ordinal() == 16)
            block.setState(ModBlocks.chalk.getDefaultState());
        else
            block.setState(ModBlocks.chalk.getStateFromMeta(chalkColor.ordinal()));

    }

    public static PositionedBlock positionedBlockWith(BlockPos pos, EnumDyeColor chalkColor) {
        PositionedBlock block = new PositionedBlock(null, pos);
        setStateFromColor(block, chalkColor);
        return block;
    }

    public static BlockPos blockPosSum(BlockPos one, BlockPos two) {
        return new BlockPos(one.getX() + two.getX(), one.getY() + two.getY(), one.getZ() + two.getZ());
    }

    public static boolean isChalkSetupValid(List<PositionedBlock> list, TileEntity entity) {
        BlockPos pos = entity.getPos();
        World world = entity.getWorld();
        for (PositionedBlock block : list)
            if (world.getBlockState(pos.add(block.getPos().getX(), block.getPos().getY(), block.getPos().getZ())) != block.getState())
                return false;
        return true;
    }


}
