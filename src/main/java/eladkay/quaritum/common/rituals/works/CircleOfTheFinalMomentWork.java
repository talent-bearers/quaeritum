package eladkay.quaritum.common.rituals.works;

import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.IWork;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.PositionedBlockChalk;
import eladkay.quaritum.common.networking.FancyParticlePacket;
import eladkay.quaritum.common.networking.NetworkHelper;
import eladkay.quaritum.common.rituals.diagrams.CircleOfTheFinalMomentDiagram;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CircleOfTheFinalMomentWork implements IWork {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "circleofthefinalmoment";
    }

    @Override
    public boolean updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile, long ticksExisted) {
        for (BlockPos pos0 : BlockPos.getAllInBox(new BlockPos(-4, 1, -4).add(pos), new BlockPos(4, 8, 4).add(pos)))
            if(world.getTileEntity(pos0) != null && world.getTileEntity(pos0) instanceof ITickable)
                for(int i = 0; i < 4; i++) ((ITickable) world.getTileEntity(pos0)).update();
        NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX() + 0.25, pos.getY(), pos.getZ() + 0.25, 50), world.provider.getDimension(), pos, 32);
        return ticksExisted < 1200;
    }

    @Override
    public boolean initialTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile, @Nullable EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canRitualRun(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Override
    public void buildPositions(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-2, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-1, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(0, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(1, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(2, 1, -7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 1, -7)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-6, 1, -6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-5, 1, -6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 1, -6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 1, -6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 1, -6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(5, 1, -6)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(6, 1, -6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-6, 2, -6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(6, 2, -6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-6, 3, -6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(6, 3, -6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-6, 4, -6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(6, 4, -6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-6, 1, -5)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-5, 1, -5)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 1, -5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(5, 1, -5)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(6, 1, -5)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 2, -5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 3, -5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 4, -5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(0, 5, -5)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-6, 1, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-4, 1, -4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 1, -4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-1, 1, -4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(0, 1, -4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(1, 1, -4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 1, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(4, 1, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(6, 1, -4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-6, 1, -3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-3, 1, -3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 1, -3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 1, -3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(3, 1, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(6, 1, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, -3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-4, 1, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-3, 1, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(3, 1, -2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(4, 1, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, -1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-4, 1, -1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(4, 1, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, 0)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-5, 1, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-4, 1, 0)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(4, 1, 0)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(5, 1, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, 0)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-5, 2, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(5, 2, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-5, 3, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(5, 3, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-5, 4, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(5, 4, 0), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-5, 5, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(5, 5, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, 1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-4, 1, 1)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(4, 1, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-4, 1, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-3, 1, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(3, 1, 2)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(4, 1, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-7, 1, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-6, 1, 3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-3, 1, 3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 1, 3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 1, 3)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(3, 1, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(6, 1, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(7, 1, 3)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-6, 1, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-4, 1, 4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-2, 1, 4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(-1, 1, 4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(0, 1, 4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(1, 1, 4)));
        chalks.add(new PositionedBlockChalk(null, new BlockPos(2, 1, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(4, 1, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(6, 1, 4)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-6, 1, 5)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-5, 1, 5)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 1, 5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(5, 1, 5)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(6, 1, 5)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 2, 5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 3, 5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(0, 4, 5), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(0, 5, 5)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-6, 1, 6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-5, 1, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-4, 1, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 1, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 1, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(4, 1, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(5, 1, 6)));
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(6, 1, 6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-6, 2, 6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(6, 2, 6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(-6, 3, 6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlock(Blocks.OBSIDIAN.getDefaultState(), new BlockPos(6, 3, 6), null)); // minecraft:obsidian
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(-6, 4, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.RED, new BlockPos(6, 4, 6)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-3, 1, 7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-2, 1, 7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(-1, 1, 7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(0, 1, 7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(1, 1, 7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(2, 1, 7)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.YELLOW, new BlockPos(3, 1, 7)));
    }

    @Nullable
    @Override
    public IDiagram getDiagramCounterpart() {
        return new CircleOfTheFinalMomentDiagram();
    }
}
