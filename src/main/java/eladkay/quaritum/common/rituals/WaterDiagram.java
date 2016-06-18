package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WaterDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.water";
    }

    @Override
    public boolean run(@Nonnull World worldIn, @Nullable EntityPlayer player, @Nonnull BlockPos pos, TileEntity te, List<ItemStack> item) {
        Material material = worldIn.getBlockState(pos.up()).getBlock().getMaterial(worldIn.getBlockState(pos.up()));
        boolean notSolid = !material.isSolid();
        if (!worldIn.isAirBlock(pos.up()) && !notSolid) {
            return false;
        } else {
            if (!worldIn.isRemote && notSolid && !material.isLiquid())
                worldIn.destroyBlock(pos.up(), true);
            worldIn.setBlockState(pos.up(), Blocks.FLOWING_WATER.getDefaultState(), 3);
        }
        return false;
    }


    @Override
    public boolean canRitualRun(@Nullable World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Nonnull
    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        return Lists.newArrayList();
    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
       /* chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.BLUE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.BLUE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.BLUE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.BLUE));*/
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, -1, 0), EnumDyeColor.BLUE));
        return chalks;
    }
}
