package eladkay.quaritum.api.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface IDiagram {
    @Nonnull
    String getUnlocalizedName();

    @Nonnull
    boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos);

    @Nonnull
    boolean canRitualRun(@Nullable World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile);

    @Nullable
    ArrayList<ItemStack> getRequiredItems();

    void buildChalks(@Nonnull List<PositionedBlock> chalks);
}
