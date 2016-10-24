package eladkay.quaritum.common.rituals.diagrams

import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.page.PageText
import eladkay.quaritum.api.lib.LibBook
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.api.rituals.PositionedBlockChalk
import eladkay.quaritum.client.core.TooltipHelper
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.book.ModBook
import eladkay.quaritum.common.book.PageDiagram
import eladkay.quaritum.common.item.ModItems
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import java.util.*

class TransducerCraftingDiagram : CraftingDiagramBase("transducer", arrayOf(ItemStack(Items.STICK), ItemStack(ModItems.dormant), ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal)), //input
        ModItems.transducer, 20, false, 1, true) {

    override fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_TRANSDUCER_PAGE1)))
        val list = ArrayList<PositionedBlock>()
        buildChalks(list)
        pages.add(PageDiagram(list, input))
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_TRANSDUCER, pages, ItemStack(ModBlocks.blueprint))
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, 2)))
    }

    companion object {
        var pages: MutableList<IPage> = ArrayList()
    }
}//name
//output
//animus
//onplayer
//rarity
//requiress)
