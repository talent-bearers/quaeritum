package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, TileEntity te, List<ItemStack> item) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 2;
        double z = pos.getZ() + 0.5;
        int rarity = 0;
        TileEntityBlueprint blueprint = (TileEntityBlueprint) te;
        for (int i = 0; i < blueprint.items.size(); i++)
            if (blueprint.items.get(i).getItem() instanceof ISoulstone) {
                ISoulstone ss = ((ISoulstone) blueprint.items.get(i).getItem());
                rarity = ss.getRarityLevel(blueprint.items.get(i));
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

    @Nonnull
    @Override
    public boolean canRitualRun(@Nullable World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal()));
        list.add(new ItemStack(ModBlocks.crystal));
        list.add(new ItemStack(Items.NETHER_WART));
        list.add(new ItemStack(ModItems.awakened));
        return list;
    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
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

        return chalks;
    }
}
