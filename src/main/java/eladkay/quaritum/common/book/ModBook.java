package eladkay.quaritum.common.book;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import com.google.common.collect.Maps;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModBook {
    public static Book book;
    public static List<CategoryAbstract> categories = new ArrayList<>();

    public static Map<ResourceLocation, EntryAbstract> pagesDiagrams = Maps.newLinkedHashMap();
    public static Map<ResourceLocation, EntryAbstract> pagesMisc = Maps.newLinkedHashMap();
    public static Map<ResourceLocation, EntryAbstract> pagesWorks = Maps.newLinkedHashMap();
    public static Map<ResourceLocation, EntryAbstract> pagesAnimus = Maps.newLinkedHashMap();
    public static CategoryItemStack catagoryDiagrams = null;
    public static CategoryItemStack catagoryWorks = null;
    public static CategoryItemStack catagoryMisc = null;
    public static CategoryItemStack catagoryAnimus = null;

    public static void init() {

        //BlockBlueprint.constructBook();

        catagoryDiagrams = new CategoryItemStack(pagesDiagrams, TooltipHelper.local(LibBook.DIAGRAMS), new ItemStack(ModBlocks.blueprint));
        categories.add(catagoryDiagrams);
        catagoryWorks = new CategoryItemStack(pagesWorks, TooltipHelper.local(LibBook.WORKS), new ItemStack(Blocks.BEACON));
        categories.add(catagoryWorks);
        catagoryMisc = new CategoryItemStack(pagesMisc, TooltipHelper.local(LibBook.MISC), new ItemStack(ModItems.chalk, 1, 5));
        categories.add(catagoryMisc);
        catagoryAnimus = new CategoryItemStack(pagesAnimus, TooltipHelper.local(LibBook.ANIMUS_MANIPULATION), new ItemStack(ModItems.awakened));
        categories.add(catagoryAnimus);

        book = new Book();
        book.setSpawnWithBook(false);
        book.setTitle(TooltipHelper.local(LibBook.NAME));
        book.setDisplayName(TooltipHelper.local(LibBook.NAME));
        book.setAuthor(TooltipHelper.local(LibBook.AUTHOR));
        book.setWelcomeMessage(TooltipHelper.local(LibBook.WELCOME_MESSAGE));
        book.setColor(Color.cyan);
        book.setCategoryList(categories);
        book.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, LibNames.BOOK));
        //book.setPageTexture(new ResourceLocation("quaritum:textures/gui/book.png"));
        GuideAPI.BOOKS.register(book);
    }

    public static void register(Map<ResourceLocation, EntryAbstract> map, String entryName, List<IPage> pages, ItemStack stack) {
        map.put(new ResourceLocation(LibMisc.MOD_ID, entryName), new EntryItemStack(pages, TooltipHelper.local(entryName), stack));
    }
}
