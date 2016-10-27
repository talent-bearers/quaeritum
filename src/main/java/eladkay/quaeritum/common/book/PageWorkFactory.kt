package eladkay.quaeritum.common.book

/*object PageWorkFactory {
    fun addWorkToBook(entryName: String, work: IWork, stack: ItemStack): Book {
        val list = Lists.newArrayList<PositionedBlock>()
        work.buildPositions(list)
        list.add(PositionedBlock(ModBlocks.foundation.defaultState, BlockPos(0, 0, 0)))
        val sorted = PositionedBlockHelper.sort(list)
        val pages = Lists.newArrayList<IPage>()
        pages.addAll(sorted.filter({ list0 -> !list0.isEmpty() }).map(::PageWork))
        ModBook.register(ModBook.pagesWorks, entryName, pages, stack)
        return ModBook.book
    }
}
*/