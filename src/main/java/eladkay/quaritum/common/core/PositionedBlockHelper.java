package eladkay.quaritum.common.core;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
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
        block.toCompare = Lists.newArrayList(BlockChalk.COLOR);
        return block;
    }

    public static BlockPos blockPosSum(BlockPos one, BlockPos two) {
        return new BlockPos(one.getX() + two.getX(), one.getY() + two.getY(), one.getZ() + two.getZ());
    }

    public static boolean isChalkSetupValid(List<PositionedBlock> list, TileEntity entity) {
        /*BlockPos pos = entity.getPos();
        World world = entity.getWorld();
        for (PositionedBlock block : list) {
            if (world.getBlockState(pos.add(block.getPos()))world.getBlockState(pos.add(block.getPos().getX(), block.getPos().getY(), block.getPos().getZ())) != block.getState()) {
                LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + ". Got " + world.getBlockState(pos.add(block.getPos())));
                return false;
            }*/
        return true;
        // }
    }

    public static boolean isChalkSetupValid(List<PositionedBlock> list, TileEntity entity, String optionalName) {
        BlockPos pos = entity.getPos();
        World world = entity.getWorld();
        for (PositionedBlock block : list) {
            for (IProperty property : block.toCompare) {
                IBlockState state = world.getBlockState(entity.getPos().add(block.getPos()));
                //yes I know I can just && this
                if (block.getState().getBlock() == state.getBlock()) {
                    System.out.println("Block check OK");
                    if (block.getState().getValue(property) == state.getValue(property)) {
                        LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + " for ritual " + optionalName + ". Got " + world.getBlockState(pos.add(block.getPos())));
                        return false;
                    }
                }
            }
        }

        return true;
    }

    //hardcoded to check for color
    public static boolean isChalkSetupValidHC(List<PositionedBlock> list, TileEntity entity, String optionalName) {
        BlockPos pos = entity.getPos();
        World world = entity.getWorld();
        for (PositionedBlock block : list) {
            IBlockState state = world.getBlockState(entity.getPos().add(block.getPos()));
            if(state.getBlock() != block.getState().getBlock()) {
                System.out.println("Dif block");
                return false;
            } else
                System.out.println(state.getBlock() + "==" + block.getState().getBlock());
            if (block.getState().getValue(BlockChalk.COLOR) == state.getValue(BlockChalk.COLOR)) {
                LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + " for ritual " + optionalName + ". Got " + world.getBlockState(pos.add(block.getPos())));
                return false;
            }


        }

        return true;
    }


}
