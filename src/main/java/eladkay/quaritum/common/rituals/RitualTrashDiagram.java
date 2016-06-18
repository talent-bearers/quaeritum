package eladkay.quaritum.common.rituals;

import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.core.PositionedBlockHelper;
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
import java.util.Collections;
import java.util.List;

public class RitualTrashDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.trash";
    }

    @Nonnull
    @Override
    public boolean run(@Nonnull World world, @Nullable EntityPlayer player, @Nonnull BlockPos pos, TileEntity te, List<ItemStack> item) {
        return true;
    }

    @Override
    public boolean canRitualRun(World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        return new ArrayList<>(Collections.singletonList(new ItemStack(Items.BLAZE_POWDER)));
    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.ORANGE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.ORANGE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.ORANGE));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.ORANGE));

        return chalks;
    }

}
