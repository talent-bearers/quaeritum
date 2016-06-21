package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.soulstones.ItemAwakenedSoulstone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
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
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, TileEntity te, List<ItemStack> items) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), ItemAwakenedSoulstone.withAnimus(100));
        EntityItem item2 = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK));

        return world.spawnEntityInWorld(item) && world.spawnEntityInWorld(item2);
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
        list.add(new ItemStack(ModItems.dormant));
        list.add(new ItemStack(ModBlocks.flower, 1, 0));
        list.add(new ItemStack(ModBlocks.flower, 1, 0));
        list.add(new ItemStack(ModBlocks.flower, 1, 0));
        list.add(new ItemStack(ModBlocks.flower, 1, 0));
        list.add(new ItemStack(ModBlocks.crystal));
        return list;
    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.MAGENTA));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.MAGENTA));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.MAGENTA));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.MAGENTA));
        return chalks;
    }
}
