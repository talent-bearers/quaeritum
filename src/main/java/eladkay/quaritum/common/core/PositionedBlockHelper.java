package eladkay.quaritum.common.core;

import eladkay.quaritum.api.rituals.PositionedBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class PositionedBlockHelper {

    public static PositionedBlock positionedBlockWith(BlockPos pos, EnumDyeColor chalkColor) {
        return PositionedBlock.constructPosChalk(chalkColor, pos);
    }

    public static boolean isChalkSetupValid(List<PositionedBlock> list, TileEntity entity, String optionalName) {
        World world = entity.getWorld();
        for (PositionedBlock block : list) {
            IBlockState state = world.getBlockState(entity.getPos().add(block.getPos()));

            if (block.getState().getBlock() == state.getBlock()) {
                LogHelper.logDebug("Block check OK");
                List<IProperty> comparables = block.getComparables();
                if (comparables != null) for (IProperty property : comparables) {
                    if (block.getState().getValue(property) != state.getValue(property)) {
                        LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + " for ritual " + optionalName + ". Got " + state);
                        return false;
                    }
                }
                else if (block.getState() != state) {
                    LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + " for ritual " + optionalName + ". Got " + state);
                    return false;
                }
            }
        }


        return true;
    }

}
