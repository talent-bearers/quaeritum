package eladkay.quaritum.common.rituals.diagrams;

import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.PositionedBlockChalk;
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
    public static void incrementAllWorldTimes(World worldserver, int amount) {
        worldserver.setWorldTime(worldserver.getWorldTime() + (long) amount);
    }

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
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(-4, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-2, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-1, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(0, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(1, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(2, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(4, 0, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 0, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-2, 0, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(0, 0, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(2, 0, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 0, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-2, 0, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-1, 0, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(0, 0, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(1, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(2, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 0, -1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 0, -1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-1, 0, -1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(1, 0, -1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(-3, 0, 0)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 0, 0)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(3, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 0, 1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 0, 1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-1, 0, 1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(1, 0, 1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-2, 0, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-1, 0, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(0, 0, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(1, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(2, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 0, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-2, 0, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(0, 0, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(2, 0, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 0, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(-4, 0, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-2, 0, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-1, 0, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(0, 0, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(1, 0, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(2, 0, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.BLACK, new BlockPos(4, 0, 4)));
    }


    @Override
    public void constructBook() {

    }
}
