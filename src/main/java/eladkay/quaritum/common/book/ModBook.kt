package eladkay.quaritum.common.book

import amerifrance.guideapi.api.GuideAPI
import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.category.CategoryItemStack
import amerifrance.guideapi.entry.EntryItemStack
import com.google.common.collect.Maps
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaritum.api.lib.LibBook
import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.item.ModItems
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.util.*

object ModBook {
    var book: Book
    var categories: MutableList<CategoryAbstract> = ArrayList()

    var pagesDiagrams: MutableMap<ResourceLocation, EntryAbstract> = Maps.newLinkedHashMap<ResourceLocation, EntryAbstract>()
    var pagesMisc: MutableMap<ResourceLocation, EntryAbstract> = Maps.newLinkedHashMap<ResourceLocation, EntryAbstract>()
    var pagesWorks: MutableMap<ResourceLocation, EntryAbstract> = Maps.newLinkedHashMap<ResourceLocation, EntryAbstract>()
    var pagesAnimus: MutableMap<ResourceLocation, EntryAbstract> = Maps.newLinkedHashMap<ResourceLocation, EntryAbstract>()
    var catagoryDiagrams: CategoryItemStack
    var catagoryWorks: CategoryItemStack
    var catagoryMisc: CategoryItemStack
    var catagoryAnimus: CategoryItemStack

    init {

        //BlockBlueprint.constructBook();

        catagoryDiagrams = CategoryItemStack(pagesDiagrams, TooltipHelper.local(LibBook.DIAGRAMS), ItemStack(ModBlocks.blueprint))
        categories.add(catagoryDiagrams)
        catagoryWorks = CategoryItemStack(pagesWorks, TooltipHelper.local(LibBook.WORKS), ItemStack(Blocks.BEACON))
        categories.add(catagoryWorks)
        catagoryMisc = CategoryItemStack(pagesMisc, TooltipHelper.local(LibBook.MISC), ItemStack(ModItems.chalk, 1, 5))
        categories.add(catagoryMisc)
        catagoryAnimus = CategoryItemStack(pagesAnimus, TooltipHelper.local(LibBook.ANIMUS_MANIPULATION), ItemStack(ModItems.awakened))
        categories.add(catagoryAnimus)

        book = Book()
        book.title = TooltipHelper.local(LibBook.NAME)
        book.displayName = TooltipHelper.local(LibBook.NAME)
        book.author = TooltipHelper.local(LibBook.AUTHOR)
        book.welcomeMessage = TooltipHelper.local(LibBook.WELCOME_MESSAGE)
        book.color = Color(0xFFFFFF)
        book.categoryList = categories
        book.registryName = ResourceLocation(LibMisc.MOD_ID, LibNames.BOOK)
        GuideAPI.BOOKS.register(book)
    }

    fun register(map: MutableMap<ResourceLocation, EntryAbstract>, entryName: String, pages: List<IPage>, stack: ItemStack) {
        map.put(ResourceLocation(LibMisc.MOD_ID, entryName), EntryItemStack(pages, TooltipHelper.local(entryName), stack))
    }

    fun meta(): Int {
        for (book in GuideAPI.BOOKS.values)
            if (book == ModBook.book) return GuideAPI.BOOKS.values.indexOf(book)
        return -1
    }
}
