package eladkay.quaritum.common.rituals.diagrams;

import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.networking.FancyParticlePacket;
import eladkay.quaritum.common.networking.NetworkHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CircleOfTheFinalMomentDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "circleofthefinalmoment";
    }

    @Override
    public void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        //noop lol
    }

    @Override
    public int getPrepTime(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return 600;
    }

    @Override
    public boolean onPrepUpdate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile, int ticksRemaining) {
        NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX() + 0.25, pos.up().getY(), pos.getZ() + 0.25, 50), world.provider.getDimension(), pos, 32);
        incrementAllWorldTimes(world, 20);
        return true;
    }

    public static void incrementAllWorldTimes(World worldserver, int amount) {
        worldserver.setWorldTime(worldserver.getWorldTime() + (long) amount);
    }

    @Override
    public boolean canRitualRun(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 2), null));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, -1), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 0), null));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 1), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -2), null));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 0), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 1), null));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, -2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, -1), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -2), null));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, -2), null));

    }


    @Override
    public void constructBook() {

    }
}
