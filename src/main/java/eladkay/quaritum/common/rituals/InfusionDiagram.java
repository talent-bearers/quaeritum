package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.soulstones.ItemAwakenedSoulstone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InfusionDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.infusion";
    }

    @Nonnull
    @Override
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), ItemAwakenedSoulstone.withAnimus(80));
        world.setBlockState(pos.add(1, 0, 0), Blocks.air.getDefaultState());
        world.setBlockState(pos.add(-1, 0, 0), Blocks.air.getDefaultState());
        world.setBlockState(pos.add(0, 0, 1), Blocks.air.getDefaultState());
        world.setBlockState(pos.add(0, 0, -1), Blocks.air.getDefaultState());

        return world.spawnEntityInWorld(item);
    }

    @Nonnull
    @Override
    public boolean canRitualRun(@Nullable World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        return new ArrayList(Lists.asList(new ItemStack(ModItems.dormant), new ItemStack[]{}));
    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(new PositionedBlock(ModBlocks.flower.getDefaultState(), new BlockPos(1, 0, 0)));
        chalks.add(new PositionedBlock(ModBlocks.flower.getDefaultState(), new BlockPos(-1, 0, 0)));
        chalks.add(new PositionedBlock(ModBlocks.flower.getDefaultState(), new BlockPos(0, 0, 1)));
        chalks.add(new PositionedBlock(ModBlocks.flower.getDefaultState(), new BlockPos(0, 0, -1)));
        return chalks;
    }
}
