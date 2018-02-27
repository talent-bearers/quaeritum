package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class GuiBook extends GuiBase implements IBookGui {

    public static Sprite ERROR = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/error.png"));
    public static Sprite FOF = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/fof.png"));
    public final Book book;
    public final Color mainColor;
    public final Color highlightColor;
    public final Map<Entry, String> contentCache;
    private final Sprite bookSprite;
    private final Sprite bookFillingSprite;
    private final Sprite arrowNext;
    private final Sprite arrowNextPressed;
    private final Sprite arrowBack;
    private final Sprite arrowBackPressed;
    private final Sprite arrowHome;
    private final Sprite arrowHomePressed;
    private final Sprite banner;
    private final Sprite bookmark;
    private final Sprite magnifier;
    private final Sprite titleBar;
    public int bookmarkID;
    public ComponentSprite bookComponent;
    public GuiComponent focus;
    public Stack<IBookElement> history = new Stack<>();
    public IBookElement currentElement;

    public GuiBook(Book book) {
        super(146, 180);
        this.book = book;

        ResourceLocation bookRl = new ResourceLocation(book.textureSheet);

        Texture guideBookSheet = new Texture(new ResourceLocation(bookRl.getResourceDomain(), "textures/" + bookRl.getResourcePath() + ".png"));
        bookSprite = guideBookSheet.getSprite("book", 146, 180);
        bookFillingSprite = guideBookSheet.getSprite("book_filling", 146, 180);
        arrowNext = guideBookSheet.getSprite("arrow_next", 18, 10);
        arrowNextPressed = guideBookSheet.getSprite("arrow_next_pressed", 18, 10);
        arrowBack = guideBookSheet.getSprite("arrow_back", 18, 10);
        arrowBackPressed = guideBookSheet.getSprite("arrow_back_pressed", 18, 10);
        arrowHome = guideBookSheet.getSprite("arrow_home", 18, 9);
        arrowHomePressed = guideBookSheet.getSprite("arrow_home_pressed", 18, 9);
        banner = guideBookSheet.getSprite("banner", 140, 31);
        bookmark = guideBookSheet.getSprite("bookmark", 133, 13);
        magnifier = guideBookSheet.getSprite("magnifier", 12, 12);
        titleBar = guideBookSheet.getSprite("title_bar", 86, 11);

        this.contentCache = book.getContentCache();
        this.mainColor = book.bookColor;
        this.highlightColor = book.highlightColor;

        bookComponent = new ComponentSprite(bindingSprite(), 0, 0);
        bookComponent.getColor().setValue(mainColor.darker());

        ComponentSprite bookFilling = new ComponentSprite(pageSprite(), 0, 0);
        bookComponent.add(bookFilling);

        getMainComponents().add(bookComponent);

        // --------- SEARCH BAR --------- //
        {
            ComponentSearchBar bar = new ComponentSearchBar(this, bookmarkID++,
                    new TFIDFSearch(this).textBoxConsumer(this, () -> new ComponentSearchResults(this)));
            bookComponent.add(bar);
        }
        // --------- SEARCH BAR --------- //

        placeInFocus(book);
    }

    @NotNull
    @Override
    public Sprite bindingSprite() {
        return bookSprite;
    }

    @NotNull
    @Override
    public Sprite pageSprite() {
        return bookFillingSprite;
    }

    @NotNull
    @Override
    public Sprite nextSprite(boolean pressed) {
        return pressed ? arrowNextPressed : arrowNext;
    }

    @NotNull
    @Override
    public Sprite backSprite(boolean pressed) {
        return pressed ? arrowBackPressed : arrowBack;
    }

    @NotNull
    @Override
    public Sprite homeSprite(boolean pressed) {
        return pressed ? arrowHomePressed : arrowHome;
    }

    @NotNull
    @Override
    public Sprite bannerSprite() {
        return banner;
    }

    @NotNull
    @Override
    public Sprite bookmarkSprite() {
        return bookmark;
    }

    @NotNull
    @Override
    public Sprite searchIconSprite() {
        return magnifier;
    }

    @NotNull
    @Override
    public Sprite titleBarSprite() {
        return titleBar;
    }

    @Override
    public @NotNull Map<Entry, String> getCachedSearchContent() {
        return contentCache;
    }

    @NotNull
    @Override
    public GuiComponent makeNavigationButton(int indexID, Entry entry, @Nullable Consumer<ComponentVoid> extra) {
        ComponentVoid indexButton = new ComponentVoid(0, 16 * indexID, bookComponent.getSize().getXi() - 32, 16);

        if (extra != null) extra.accept(indexButton);
        indexButton.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            placeInFocus(entry);
        });

        // SUB INDEX PLATE RENDERING
        {
            final String title = I18n.format(entry.titleKey).replace("&", "§");
            final JsonElement icon = entry.icon;

            ComponentText textComponent = new ComponentText(20, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
            textComponent.getUnicode().setValue(true);
            textComponent.getText().setValue(title);
            indexButton.add(textComponent);

            indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
                textComponent.getText().setValue(" " + TextFormatting.ITALIC.toString() + title);
            });

            indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
                textComponent.getText().setValue(TextFormatting.RESET.toString() + title);
            });

            indexButton.render.getTooltip().func((Function<GuiComponent, List<String>>) guiComponent -> {
                List<String> list = new ArrayList<>();
                TooltipHelper.addToTooltip(list, entry.titleKey);

                String desc = entry.descKey;
                String used = LibrarianLib.PROXY.canTranslate(desc) ? desc : desc + "0";
                if (LibrarianLib.PROXY.canTranslate(used)) {
                    TooltipHelper.addToTooltip(list, used);
                    int i = 0;
                    while (LibrarianLib.PROXY.canTranslate(desc + (++i)))
                        TooltipHelper.addToTooltip(list, desc + i);
                }

                for (int i = 1; i < list.size(); i++)
                    list.set(i, TextFormatting.GRAY + list.get(i));
                return list;
            });

            Runnable render = IBookGui.getRendererFor(icon, new Vec2d(16, 16));

            if (render != null)
                indexButton.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
                    render.run();
                });
        }

        return indexButton;
    }

    @Override
    public @NotNull GuiComponent getMainComponent() {
        return bookComponent;
    }

    @Override
    public @Nullable GuiComponent getFocus() {
        return focus;
    }

    @Override
    public void setFocus(@Nullable GuiComponent component) {
        focus = component;
    }

    @Override
    public @NotNull Stack<IBookElement> getHistory() {
        return history;
    }

    @Override
    public @Nullable IBookElement getCurrentElement() {
        return currentElement;
    }

    @Override
    public void setCurrentElement(@Nullable IBookElement element) {
        currentElement = element;
    }

    @Override
    public @NotNull Book getBook() {
        return book;
    }
}
