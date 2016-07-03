package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.entity.EntityChaosborn;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.entity.effect.EntityLightningBolt;
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

public class RitualSummoning implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.templeoftherift";
    }

    @Nonnull
    @Override
    public boolean run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint te) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 2;
        double z = pos.getZ() + 0.5;
        int rarity = 0;
        for (EntityItem item : Helper.entitiesAroundAltar(te, 4)) {
            ItemStack stack = item.getEntityItem();
            if (!(stack.getItem() instanceof ISoulstone)) continue;
            ISoulstone ss = (ISoulstone) stack.getItem();
            rarity = Math.max(ss.getRarityLevel(stack), rarity);
            world.addWeatherEffect(new EntityLightningBolt(world, item.posX, item.posY, item.posZ, true));
            item.setDead();
        }
        EntityChaosborn chaosborn = new EntityChaosborn(world, rarity, x, y, z);
        for (int i = 0; i <= 10; i++)
            world.addWeatherEffect(new EntityLightningBolt(world, pos.getX() + op(Math.random() * 4), pos.getY(), pos.getZ() + op(Math.random() * 4), true));
        world.setWorldTime(23000);
        return world.spawnEntityInWorld(chaosborn);
    }

    private double op(double in) {
        return Math.random() >= 0.5 ? in : -in;
    }

    @Override
    public boolean canRitualRun(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return Helper.matches(Helper.stacksAroundAltar(tile, 4), getRequiredItems());
    }

    @Nonnull
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal()));
        list.add(new ItemStack(ModBlocks.crystal));
        list.add(new ItemStack(Items.NETHER_WART));
        list.add(new ItemStack(ModItems.awakened));
        return list;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.YELLOW));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.YELLOW));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.RED));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, -2), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 2), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, -2), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 2), EnumDyeColor.LIME));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, -1), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 0), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(2, 0, 1), EnumDyeColor.CYAN));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 2), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 2), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 2), EnumDyeColor.CYAN));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, -1), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 0), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-2, 0, 1), EnumDyeColor.CYAN));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -2), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -2), EnumDyeColor.CYAN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -2), EnumDyeColor.CYAN));
    }
}
