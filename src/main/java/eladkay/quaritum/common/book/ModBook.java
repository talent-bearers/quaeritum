package eladkay.quaritum.common.book;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import com.google.common.collect.Maps;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.block.BlockBlueprint;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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

        BlockBlueprint.constructBook();

        catagoryDiagrams = new CategoryItemStack(pagesDiagrams, TooltipHelper.local(LibBook.DIAGRAMS), new ItemStack(ModBlocks.blueprint));
        categories.add(catagoryDiagrams);
        catagoryWorks = new CategoryItemStack(pagesWorks, TooltipHelper.local(LibBook.WORKS), new ItemStack(Blocks.BEACON));
        categories.add(catagoryWorks);
        catagoryMisc = new CategoryItemStack(pagesMisc, TooltipHelper.local(LibBook.MISC), new ItemStack(ModItems.chalk, 1, 5));
        categories.add(catagoryMisc);
        catagoryAnimus = new CategoryItemStack(pagesAnimus, TooltipHelper.local(LibBook.ANIMUS_MANIPULATION), new ItemStack(ModItems.awakened));
        categories.add(catagoryAnimus);

        book = new Book();
        book.setSpawnWithBook(true);
        book.setTitle(TooltipHelper.local(LibBook.NAME));
        book.setDisplayName(TooltipHelper.local(LibBook.NAME));
        book.setAuthor(TooltipHelper.local(LibBook.AUTHOR));
        book.setWelcomeMessage(TooltipHelper.local(LibBook.WELCOME_MESSAGE));
        book.setColor(Color.cyan);
        book.setCategoryList(categories);
        book.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, LibNames.BOOK));

        GameRegistry.register(book);
        //GuideAPI.setModel(book, new ResourceLocation(LibMisc.MOD_ID, LibNames.BOOK));
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            GuideAPI.setModel(book, new ModelResourceLocation(LibMisc.MOD_ID + ":" + LibNames.BOOK, "inventory"), "inventory");
            //GuideAPI.setModel(book, new ModelResourceLocation(LibMisc.MOD_ID + ":" + LibNames.BOOK), "type=book");
            //GuideAPI.setModel(book);
        }
        /*if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            ModelLoader.setCustomModelResourceLocation(GuideAPI.guideBook, GuideRegistry.getIndexOf(book), new ModelResourceLocation(new ResourceLocation(LibMisc.MOD_ID, LibNames.BOOK), "type=book"));*/
    }
}
