package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.EnumRitualDuration;
import eladkay.quaritum.api.rituals.EnumRitualType;
import eladkay.quaritum.api.rituals.IRitual;
import eladkay.quaritum.api.rituals.PositionedChalk;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.flowers.Common;
import eladkay.quaritum.common.item.flowers.CommonArcane;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ShardedSkiesTier2Diagram implements IRitual {
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.shardedsky";
    }

    @Override
    public EnumRitualType getRitualType() {
        return EnumRitualType.DIAGRAM;
    }

    @Override
    public EnumRitualDuration getRitualDuration() {
        return EnumRitualDuration.INSTANT;
    }

    @Override
    public boolean runOnce(World world, EntityPlayer player, BlockPos pos) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack((CommonArcane) ModBlocks.commonArcane));
        return world.spawnEntityInWorld(item);
    }

    @Override
    public boolean runDurable(World world, EntityPlayer player, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRitualRun(World world, EntityPlayer player, BlockPos pos, TileEntity tile) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack((Common) ModBlocks.common));
        list.add(new ItemStack(Items.nether_wart));
        list.add(new ItemStack(Items.blaze_powder));
        return list;
    }

    @Override
    public String getCanonicalName() {
        return "Sharded Skies";
    }

    @Override
    public ArrayList<PositionedChalk> getRequiredPositionedChalk() {
        return Lists.newArrayList();
    }

    @Override
    public ArrayList<ArrayList<PositionedChalk>> getPossibleRequiredPositionedChalks() {
        return Lists.newArrayList();
    }

    @Override
    public boolean ignoreChalk() {
        return true;
    }
}
