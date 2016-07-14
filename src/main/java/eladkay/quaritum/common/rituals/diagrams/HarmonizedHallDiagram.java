package eladkay.quaritum.common.rituals.diagrams;

import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.crafting.recipes.RecipeAnimusUpgrade;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class HarmonizedHallDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.merger";
    }

    @Override
    public void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        for (EntityItem item : Helper.entitiesAroundAltar(tile, 4)) {
            ItemStack stack = item.getEntityItem();
            if (stack.getItem() instanceof ISoulstone)
                item.setDead();
        }

        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), RecipeAnimusUpgrade.output(Helper.stacksAroundAltar(tile, 4)));
        world.spawnEntityInWorld(item);
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        boolean flag = false;
        List<ItemStack> stacks = Helper.stacksAroundAltar(tile, 4);
        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof ISoulstone) {
                if (flag) return true;
                flag = true;
            }
        }
        return false;
    }

    @Override
    public boolean canRitualRun(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -1), EnumDyeColor.WHITE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 1), EnumDyeColor.WHITE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.WHITE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.WHITE));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.WHITE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.WHITE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.WHITE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.WHITE));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, -2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, -2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 2), EnumDyeColor.BLACK));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, -1), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 0), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 1), EnumDyeColor.BLACK));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 2), EnumDyeColor.BLACK));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, -1), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 0), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 1), EnumDyeColor.BLACK));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -2), EnumDyeColor.BLACK));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -2), EnumDyeColor.BLACK));
    }

    @Override
    public void constructBook() {

    }
}
