package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.EnumRitualDuration;
import eladkay.quaritum.api.rituals.EnumRitualType;
import eladkay.quaritum.api.rituals.IRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualTrashDiagram implements IRitual {
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.trash";
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
        return true;
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
        return new ArrayList<>(Lists.asList(new ItemStack(Items.blaze_powder), new ItemStack[]{}));
    }

    @Override
    public String getCanonicalName() {
        return "Ritual of Rubbish";
    }

  /*  @Override
    public ArrayList<PositionedChalk> getRequiredPositionedChalk() {
        return Lists.newArrayList();
    }

    @Override
    public ArrayList<ArrayList<PositionedChalk>> getPossibleRequiredPositionedChalks() {
        return Lists.newArrayList();
    }*/

    @Override
    public boolean ignoreChalk() {
        return true;
    }
}
