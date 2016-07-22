package eladkay.quaritum.common.rituals.diagrams;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageText;
import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.INetworkProvider;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.PositionedBlockChalk;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.book.PageDiagram;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfusionDiagram implements IDiagram {
    public static List<IPage> pages = new ArrayList<>();

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.infusion";
    }

    @Override
    public void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tes) {
       /* EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), ItemAwakenedSoulstone.withAnimus(100, 2));
        EntityItem item2 = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK));

        world.spawnEntityInWorld(item);
        world.spawnEntityInWorld(item2);*/
        for (EntityItem stack : Helper.entitiesAroundAltar(tes, 4)) {
            WorldServer server = (WorldServer) tes.getWorld();
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.getPosition().getX() + 0.5, stack.getPosition().getY() + 1, stack.getPosition().getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
            stack.setDead();
        }
        ItemStack stack = Helper.getNearestAttunedSoulstone(tes, 4);
        if (stack == null || !(stack.getItem() instanceof INetworkProvider) || !((INetworkProvider) stack.getItem()).isReceiver(stack))
            return;
        UUID player = ((INetworkProvider) stack.getItem()).getPlayer(stack);
        AnimusHelper.Network.setInfused(player, true);
    }

    @Override
    public boolean canRitualRun(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return true;
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return Helper.matches(Helper.stacksAroundAltar(tile, 4), getRequiredItems());
    }

    @Nonnull
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(ModItems.dormant));
        list.add(new ItemStack(ModBlocks.flower, 4, BlockAnimusFlower.Variants.ARCANE.ordinal()));
        list.add(new ItemStack(ModBlocks.crystal));
        return list;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(-1, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(0, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(1, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(-1, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(1, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(-1, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(0, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.MAGENTA, new BlockPos(1, 0, 1)));
    }

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_INFUSION_PAGE1)));
        List<PositionedBlock> l = Lists.newArrayList();
        buildChalks(l);
        pages.add(new PageDiagram(l, getRequiredItems()));
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_INFUSION_NAME, pages, new ItemStack(ModBlocks.crystal));

    }
}
