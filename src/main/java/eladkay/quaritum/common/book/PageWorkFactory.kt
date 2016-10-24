package eladkay.quaritum.common.book

import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import com.google.common.collect.Lists
import eladkay.quaritum.api.rituals.IWork
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.core.PositionedBlockHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import java.util.*
import java.util.stream.Collectors

object PageWorkFactory {
    fun addWorkToBook(entryName: String, work: IWork, stack: ItemStack): Book {
        val list = Lists.newArrayList<PositionedBlock>()
        work.buildPositions(list)
        list.add(PositionedBlock(ModBlocks.foundation.defaultState, BlockPos(0, 0, 0)))
        val sorted = PositionedBlockHelper.sort(list)
        val pages = Lists.newArrayList<IPage>()
        pages.addAll(sorted.stream().filter({ list0 -> !list0.isEmpty() }).map(Function<ArrayList<PositionedBlock>, PageWork> { PageWork(it) }).collect(Collectors.toList<PageWork>()))
        ModBook.register(ModBook.pagesWorks, entryName, pages, stack)
        return ModBook.book
    }
}
