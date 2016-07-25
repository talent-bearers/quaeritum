package eladkay.quaritum.common.book;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IWork;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageWorkFactory {
    public static Book addWorkToBook(String entryName, IWork work, ItemStack stack) {
        ArrayList<PositionedBlock> list = Lists.newArrayList();
        work.buildPositions(list);
        list.add(new PositionedBlock(ModBlocks.foundation.getDefaultState(), new BlockPos(0, 0, 0)));
        ArrayList<ArrayList<PositionedBlock>> sorted = PositionedBlockHelper.sort(list);
        List<IPage> pages = Lists.newArrayList();
        pages.addAll(sorted.stream().filter(list0 -> !list0.isEmpty()).map(PageWork::new).collect(Collectors.toList()));
        ModBook.register(ModBook.pagesWorks, entryName, pages ,stack);
        return ModBook.book;
    }
}
