package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.crafting.recipes.RecipeAnimusUpgrade;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.soulstones.ItemAwakenedSoulstone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HarmonizedHallDiagram implements IDiagram {
    private static ItemStack output(List<ItemStack> inputs) {
        ItemStack out = ItemAwakenedSoulstone.withAnimus(0);
        int animus = 0;
        List<Integer> rarities = Lists.newArrayList();
        if (!(out.getItem() instanceof ISoulstone))
            return out;
        ISoulstone outItem = ModItems.awakened;
      /*  inputs.stream().filter(stack -> stack != null && stack.getItem() instanceof ISoulstone).forEach(stack -> {
            outItem.addAnimus(out, AnimusHelper.getRarity(stack));
            rarities.add(AnimusHelper.getRarity(stack));
        });*/
        for (ItemStack stack : inputs) {
            animus += outItem.getAnimusLevel(stack);
            rarities.add(outItem.getRarityLevel(stack));
        }
        outItem.addRarity(out, RecipeAnimusUpgrade.getSmallestInList(rarities));
        outItem.addAnimus(out, animus);
        //outItem.addRarity(out, getAverageOfList(rarities));
        return out;

    }

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.merger";
    }

    @Override
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tileEntity, List<ItemStack> items) {
        System.out.println("hgg");
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), RecipeAnimusUpgrade.output(items));
        for (ItemStack stack : items) {
            player.addChatComponentMessage(new TextComponentString(ModItems.awakened.toString(stack)));
        }
        if (!world.isRemote)
            world.spawnEntityInWorld(item);
        return true;
    }

    @Override
    public boolean canRitualRun(@Nullable World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Nonnull
    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        return new ArrayList(Collections.singletonList(new ItemStack(ModItems.awakened)));
        //return Lists.newArrayList();
    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
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
        return chalks;
    }
}
