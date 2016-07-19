package eladkay.quaritum.common.core;

import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class PositionedBlockHelper {

    public static PositionedBlock positionedBlockWith(BlockPos pos, EnumDyeColor chalkColor) {
        return PositionedBlock.constructPosChalk(chalkColor, pos);
    }

    public static int getChalkPriority(List<PositionedBlock> list, TileEntity entity, String optionalName) {
        World world = entity.getWorld();
        int chalks = 0;
        for (PositionedBlock block : list) {
            IBlockState state = world.getBlockState(entity.getPos().add(block.getPos()));
            List<IProperty> comparables = block.getComparables();

            if (block.getState().getBlock() == state.getBlock()) {
                LogHelper.logDebug("Block check OK for " + optionalName);
                if (comparables != null) {
                    for (IProperty property : comparables) {
                        if (block.getState().getValue(property) != state.getValue(property)) {
                            LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + " for ritual " + optionalName + ". Got " + state);
                            return -1;
                        }
                    }
                    chalks++;
                } else if (block.getState() != state) {
                    LogHelper.logDebug("Expected " + block.getState() + " in " + block.getPos() + " for ritual " + optionalName + ". Got " + state);
                    return -1;
                } else chalks++;
            } else {
                return -1;
            }
        }

        return chalks;
    }
    public static BlockPos getDimensions(List<PositionedBlock> list) {
        AxisAlignedBB bb = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        for(PositionedBlock block : list) bb.addCoord(block.getPos().getX(), block.getPos().getY(), block.getPos().getZ());
        return new BlockPos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
    }
    @Deprecated
    public static ItemStack[][][] getItemStackArrayArrayArrayFromPositionedBlockList(List<PositionedBlock> blocks) {
        ItemStack[][][] ret = new ItemStack[50][50][50];
        for(PositionedBlock block : blocks) {
            ItemStack stack = new ItemStack(ModItems.chalk, 1, block.getState().getBlock().getMetaFromState(block.getState()));
            ret[block.getPos().getX() + 3][block.getPos().getY() + 3][block.getPos().getZ() + 3] = stack;
        }
        return ret;
    }
    public static ItemStack getStackFromChalk(PositionedBlock block) {
        if(block.getState().getBlock() == ModBlocks.blueprint) return new ItemStack(ModItems.picture, 1, 0);
        return new ItemStack(ModItems.chalk, 1, block.getState().getBlock().getMetaFromState(block.getState()));
    }
    public static ItemStack getStackFromChalk(PositionedBlock block, boolean flat) {
        if(block.getState().getBlock() == ModBlocks.blueprint) return new ItemStack(ModItems.picture, 1, 0);
        ItemStack stack = new ItemStack(ModItems.chalk, 1, block.getState().getBlock().getMetaFromState(block.getState()));
        if(flat)
            ItemNBTHelper.setBoolean(stack, LibNBT.FLAT, true);
        return stack;
    }

}
