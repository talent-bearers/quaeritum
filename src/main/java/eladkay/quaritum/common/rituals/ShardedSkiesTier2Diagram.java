package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShardedSkiesTier2Diagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.shardedsky";
    }

    @Override
    public boolean run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint te) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal()));
        return world.spawnEntityInWorld(item);
    }

    @Override
    public boolean canRitualRun(World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return Helper.matches(Helper.stacksAroundAltar(tile, 4), getRequiredItems());
    }

    @Nonnull
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON.ordinal()));
        list.add(new ItemStack(Items.NETHER_WART));
        list.add(new ItemStack(Items.BLAZE_POWDER));
        return list;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.LIME));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 1), EnumDyeColor.GREEN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 1), EnumDyeColor.GREEN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -1), EnumDyeColor.GREEN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.GREEN));
    }

}
