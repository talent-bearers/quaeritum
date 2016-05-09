package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShardedSkiesTier2Diagram implements IDiagram {
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.shardedsky";
    }

    @Nonnull
    @Override
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal()));
        return world.spawnEntityInWorld(item);
    }

    @Override
    public boolean canRitualRun(World world, EntityPlayer player, BlockPos pos, TileEntity tile) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON.ordinal()));
        list.add(new ItemStack(Items.nether_wart));
        list.add(new ItemStack(Items.blaze_powder));
        return list;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {

    }

}
