package eladkay.quaritum.common.rituals.diagrams;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageText;
import com.google.common.collect.ImmutableList;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.PositionedBlockChalk;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.book.PageDiagram;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TransducerCraftingDiagram extends CraftingDiagramBase {
    public static List<IPage> pages = new ArrayList<>();

    public TransducerCraftingDiagram() {
        super
                ("transducer", //name
                        new ItemStack[]{new ItemStack(Items.STICK), new ItemStack(ModItems.dormant), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal())}, //input
                        ModItems.transducer, //output
                        20, //animus
                        false, //onplayer
                        1, //rarity
                        true); //requiress)
    }

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_TRANSDUCER_PAGE1)));
        List<PositionedBlock> list = new ArrayList<>();
        buildChalks(list);
        pages.add(new PageDiagram(list, input));
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_TRANSDUCER, pages, new ItemStack(ModBlocks.blueprint));
    }

    @Override
    public void buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-1, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(0, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(1, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-2, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PURPLE, new BlockPos(-1, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(0, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PURPLE, new BlockPos(1, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(2, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-2, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-1, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(1, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(2, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-2, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PURPLE, new BlockPos(-1, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(0, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PURPLE, new BlockPos(1, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(2, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-1, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(0, 0, 2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(1, 0, 2)));
    }
}
