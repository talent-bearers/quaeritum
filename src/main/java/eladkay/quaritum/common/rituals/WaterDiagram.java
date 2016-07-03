package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

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
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos0, TileEntity te, List<ItemStack> item) {
        BlockPos pos = pos0.up();
        if (world.isRemote)
            return false;

        if (!world.canMineBlockBody(player, pos))
            return false;

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFluidHandler) {
            FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
            int amount = ((IFluidHandler) tile).fill(null, fluid, false);

            if (amount > 0)
                ((IFluidHandler) tile).fill(null, fluid, true);
            return false;
        }

        if (world.getBlockState(pos).getBlock() == Blocks.CAULDRON) {
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
            return false;
        }
        this.tryPlaceWater(world, pos);
        return true;
    }

    public boolean tryPlaceWater(World worldIn, BlockPos pos) {

        Material material = worldIn.getBlockState(pos).getBlock().getMaterial(null);
        boolean notSolid = !material.isSolid();

        if (!worldIn.isAirBlock(pos) && !notSolid) {
            return false;
        } else {
            if (worldIn.provider.doesWaterVaporize()) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.playSound(null, i, j, k, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D, 0);
            } else {
                if (!worldIn.isRemote && notSolid && !material.isLiquid())
                    worldIn.destroyBlock(pos, true);

                worldIn.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 3);
            }

            return true;
        }
    }


    @Override
    public boolean canRitualRun(@Nullable World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        BlockPos blockPos = pos.up();
        return !(!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial(null).isSolid()) &&
                !((world.getBlockState(blockPos).getBlock() == Blocks.WATER || world.getBlockState(blockPos).getBlock()
                == Blocks.FLOWING_WATER) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
                && world.getBlockState(pos.down()) == PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 0), EnumDyeColor.BLUE).getState();
        //yes I know well this is bad
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
