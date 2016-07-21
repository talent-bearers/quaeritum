package eladkay.quaritum.common.rituals.diagrams;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageText;
import com.google.common.collect.Lists;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.book.PageDiagram;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.networking.FancyParticlePacket;
import eladkay.quaritum.common.networking.NetworkHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShardedSkiesTier2Diagram implements IDiagram {
    public static List<IPage> pages = new ArrayList<>();

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.shardedsky";
    }

    @Override
    public void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint te) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal()));
        world.spawnEntityInWorld(item);
        for (EntityItem stack : Helper.entitiesAroundAltar(te, 4)) {
            WorldServer server = (WorldServer) te.getWorld();
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.getPosition().getX() + 0.5, stack.getPosition().getY() + 1, stack.getPosition().getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
            stack.setDead();
        }
    }

    @Override
    public boolean canRitualRun(World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return Helper.matches(Helper.stacksAroundAltar(tile, 4), getRequiredItems());
    }

    @Override
    public int getPrepTime(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return 50;
    }

    @Override
    public boolean onPrepUpdate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile, int ticksRemaining) {
        boolean flag = true;
        for (ItemStack stack : getRequiredItems())
            if (!Helper.isStackInList(stack, Helper.stacksAroundAltar(tile, 4))) return false;
        NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX(), pos.getY(), pos.getZ(), 50), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16);
        /*for(EntityItem stack : Helper.entitiesAroundAltar(tile, 4)) {
            if (!Helper.isEntityItemInList(stack, getRequiredItems())) {
                flag = false;
                System.out.println("hi");
            } else
                NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX(), pos.getY(), pos.getZ(), 50), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16);
        }*/
        return true;
    }

    @Nonnull
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON.ordinal()));
        list.add(new ItemStack(Items.NETHER_WART));
        list.add(new ItemStack(Items.BLAZE_POWDER));
        return list;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, -1), EnumDyeColor.GREEN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, -1), EnumDyeColor.GREEN));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.LIME));

        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 1), EnumDyeColor.GREEN));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.LIME));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 1), EnumDyeColor.GREEN));
    }

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_SHARDED_SKIES2_PAGE1)));
        List<PositionedBlock> list = new ArrayList<>();
        buildChalks(list);
        pages.add(new PageDiagram(list, getRequiredItems()));
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_SHARDED_SKIES_NAME2, pages, new ItemStack(ModBlocks.flower, 1, 1));
    }

}
