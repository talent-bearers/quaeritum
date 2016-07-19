package eladkay.quaritum.common.rituals.works;

import eladkay.quaritum.api.rituals.IWork;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.tile.TileEntityFoundationStone;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SimpleTestWork implements IWork {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "quaritum.works.test";
    }

    @Override
    public boolean updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityFoundationStone tile, long ticksExisted) {
        return ticksExisted < 100;
    }

    @Override
    public boolean initialTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityFoundationStone tile, @Nullable EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canRitualRun(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityFoundationStone tile) {
        return true;
    }

    @Override
    public void buildPositions(@Nonnull List<PositionedBlock> blocks) {
        blocks.add(new PositionedBlock(Block.REGISTRY.getObject(new ResourceLocation("minecraft:stone")).getDefaultState(), new BlockPos(1, 0, 1)));
    }
}
