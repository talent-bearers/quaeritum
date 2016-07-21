package eladkay.quaritum.common.rituals.diagrams;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageText;
import com.google.common.collect.ImmutableList;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.book.Dimension;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.book.PageDiagram;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TransducerCraftingDiagram extends CraftingDiagramBase {
    public TransducerCraftingDiagram() {
        super
             ("transducer", //name
              new ItemStack[]{new ItemStack(Items.STICK), new ItemStack(ModItems.dormant), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal())}, //input
              ModItems.transducer, //output
              ImmutableList.of(PositionedBlock.BLUEPRINT), //positionedblocks
              20, //animus
              false, //onplayer
              1, //rarity
              true); //requiress)
    }
    public static List<IPage> pages = new ArrayList<>();

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_TRANSDUCER_PAGE1)));
        List list = new ArrayList<>();
        buildChalks(list);
        pages.add(new PageDiagram(list, input, new Dimension(1, 1), 88, 64));
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_TRANSDUCER, pages, new ItemStack(ModBlocks.blueprint));
    }
}
