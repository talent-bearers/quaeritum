package eladkay.quaritum.common.rituals.diagrams;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageText;
import com.google.common.collect.ImmutableList;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.book.PageDiagram;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GlowSandDiagram extends CraftingDiagramBase {
    public static List<IPage> pages = new ArrayList<>();

    public GlowSandDiagram() {
        super("glowsand", new ItemStack[]{new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Blocks.SAND), new ItemStack(ModBlocks.flower, 1)},
                new ItemStack(ModBlocks.glowsand), ImmutableList.of(), 0, false, 0, false);
    }

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_TRANSDUCER_PAGE1)));
        List<PositionedBlock> list = new ArrayList<>();
        buildChalks(list);
        pages.add(new PageDiagram(list, input));
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_TRANSDUCER, pages, new ItemStack(ModBlocks.blueprint));
    }
}
