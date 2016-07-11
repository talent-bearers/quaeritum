package eladkay.quaritum.common.rituals;

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
import eladkay.quaritum.common.book.Dimension;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.book.PageDiagram;
import eladkay.quaritum.common.networking.FancyParticlePacket;
import eladkay.quaritum.common.networking.NetworkHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShardedSkiesDiagram implements IDiagram {

    public static List<IPage> pages = new ArrayList<>();

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_SHARDED_SKIES_PAGE1)));
        List list = new ArrayList<>();
        buildChalks(list);
        pages.add(new PageDiagram(list, getRequiredItems(), new Dimension(1, 1), 88, 64));
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_SHARDED_SKIES_NAME, pages, new ItemStack(ModBlocks.flower));
    }
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.shardedsky";
    }

    @Override
    public void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tes) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON.ordinal()));

        for(EntityItem stack : Helper.entitiesAroundAltar(tes, 4)) {
            if(!Helper.isEntityItemInList(stack, getRequiredItems())) continue;
            WorldServer server = (WorldServer) tes.getWorld();
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.getPosition().getX() + 0.5, stack.getPosition().getY() + 1, stack.getPosition().getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
            stack.setDead();
        }
        world.spawnEntityInWorld(item);
    }

    @Override
    public boolean onPrepUpdate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile, int ticksRemaining) {
       // NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX(), pos.getY(), pos.getZ(), 100), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16);
        boolean flag = false;
        for(EntityItem stack : Helper.entitiesAroundAltar(tile, 4))
            if (Helper.isEntityItemInList(stack, getRequiredItems())) {
                NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX(), pos.getY(), pos.getZ(), 50), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16);
                flag = true;
            }
        return flag;
    }

    @Override
    public int getPrepTime(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return 50;
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return Helper.matches(Helper.stacksAroundAltar(tile, 4), getRequiredItems());
    }

    @Override
    public boolean canRitualRun(World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return true;
    }

    @Nonnull
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(Blocks.RED_FLOWER));
        return list;
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlock.BLUEPRINT);
        //NO-OP
    }

}
