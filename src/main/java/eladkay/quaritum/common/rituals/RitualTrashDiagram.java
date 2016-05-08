package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedChalk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RitualTrashDiagram implements IDiagram {
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.trash";
    }

    @Nonnull
    @Override
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos) {
        return true;
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
    public void buildChalks(@Nonnull List<PositionedChalk> chalks) {

    }

}
