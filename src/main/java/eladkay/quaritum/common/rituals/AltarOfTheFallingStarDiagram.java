package eladkay.quaritum.common.rituals;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.INetworkProvider;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.core.ChatHelper;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AltarOfTheFallingStarDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.altarofthefallingstar";
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        List<ItemStack> stacks = Helper.stacksAroundAltar(tile, 4);
        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof ISoulstone) return true;
        }
        return false;
    }

    @Override
    public boolean run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint te) {
        boolean flag = false;

        List<EntityItem> entities = Helper.entitiesAroundAltar(te, 4);

        UUID player = null;
        for (EntityItem item : entities) {
            ItemStack stack = item.getEntityItem();
            if (!(stack.getItem() instanceof INetworkProvider) || ((INetworkProvider) stack.getItem()).isReceiver(stack))
                continue;
            player = ((INetworkProvider) stack.getItem()).getPlayer(stack);
            break;
        }
        if (player == null) return false;

        for (EntityItem item : entities) {
            ItemStack stack = item.getEntityItem();
            if (!(stack.getItem() instanceof ISoulstone)) continue;
            ISoulstone ss = (ISoulstone) stack.getItem();
            AnimusHelper.Network.addAnimus(player, ss.getAnimusLevel(stack));
            AnimusHelper.Network.addAnimusRarity(player, ss.getAnimusLevel(stack));
            item.setDead();
            flag = true;
        }
        if (flag) for (EntityPlayer playerEntity : world.playerEntities)
            if (playerEntity.getUniqueID().equals(player))
                ChatHelper.sendNoSpam1(playerEntity, new TextComponentTranslation("misc.quaritum.rushOfEnergy"));
        return flag;
    }


    @Override
    public boolean canRitualRun(World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        List<EntityItem> entities = Helper.entitiesAroundAltar(tile, 4);
        for (EntityItem entity : entities) {
            ItemStack stack = entity.getEntityItem();
            if (stack.getItem() instanceof INetworkProvider && ((INetworkProvider) stack.getItem()).isReceiver(stack))
                return true;
        }
        return false;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.RED));
    }
}
