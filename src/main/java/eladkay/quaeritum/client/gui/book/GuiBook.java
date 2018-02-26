package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
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
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;
import static eladkay.quaeritum.client.gui.book.ComponentNavBar.ElementWithPage.actualElement;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class GuiBook extends GuiBase {

    public static Sprite ERROR = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/error.png"));
    public static Sprite FOF = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/fof.png"));
    private static Texture GUIDE_BOOK_SHEET = new Texture(new ResourceLocation(MOD_ID, "textures/gui/book/guide_book.png"));
    public static Sprite BOOK = GUIDE_BOOK_SHEET.getSprite("book", 146, 180);
    public static Sprite BOOK_FILLING = GUIDE_BOOK_SHEET.getSprite("book_filling", 146, 180);
    public static Sprite ARROW_NEXT = GUIDE_BOOK_SHEET.getSprite("arrow_next", 18, 10);
    public static Sprite ARROW_NEXT_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_next_pressed", 18, 10);
    public static Sprite ARROW_BACK = GUIDE_BOOK_SHEET.getSprite("arrow_back", 18, 10);
    public static Sprite ARROW_BACK_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_back_pressed", 18, 10);
    public static Sprite ARROW_HOME = GUIDE_BOOK_SHEET.getSprite("arrow_home", 18, 9);
    public static Sprite ARROW_HOME_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_home_pressed", 18, 9);
    public static Sprite BANNER = GUIDE_BOOK_SHEET.getSprite("banner", 140, 31);
    public static Sprite BOOKMARK = GUIDE_BOOK_SHEET.getSprite("bookmark", 133, 13);
    public static Sprite MAGNIFIER = GUIDE_BOOK_SHEET.getSprite("magnifier", 12, 12);
    public static Sprite TITLE_BAR = GUIDE_BOOK_SHEET.getSprite("title_bar", 86, 11);
    public final Book book;
    public final Color mainColor;
    public final Color highlightColor;
    public final Map<Entry, String> contentCache;
    public int bookmarkID;
    public ComponentSprite bookComponent;
    public GuiComponent focus;
    public Stack<IBookElement> history = new Stack<>();
    public IBookElement currentElement;

    public GuiBook(Book book) {
        super(146, 180);
        this.book = book;
        this.contentCache = book.getContentCache();
        this.mainColor = book.bookColor;
        this.highlightColor = book.highlightColor;

        bookComponent = new ComponentSprite(BOOK, 0, 0);
        bookComponent.getColor().setValue(mainColor.darker());

        ComponentSprite bookFilling = new ComponentSprite(BOOK_FILLING, 0, 0);
        bookComponent.add(bookFilling);

        getMainComponents().add(bookComponent);

        // --------- SEARCH BAR --------- //
        {
            ComponentTextBox bar = new ComponentTextBox(this, bookmarkID++,
                    new TFIDFSearch(this).textBoxConsumer(this, () -> new ComponentSearchResults(this)),
                    null);
            bookComponent.add(bar);
        }
        // --------- SEARCH BAR --------- //

        placeInFocus(book);
    }

    public static Runnable getRendererFor(JsonElement icon, Vec2d size) {
        return getRendererFor(icon, size, false);
    }

    public static Runnable getRendererFor(JsonElement icon, Vec2d size, boolean mask) {
        if (icon == null) return null;

        if (icon.isJsonPrimitive()) {
            ResourceLocation iconLocation = new ResourceLocation(icon.getAsString());
            Sprite sprite = new Sprite(new ResourceLocation(iconLocation.getResourceDomain(),
                    "textures/" + iconLocation.getResourcePath() + ".png"));
            return () -> renderSprite(sprite, size, mask);
        } else if (icon.isJsonObject()) {
            ItemStack stack = CraftingHelper.getItemStack(icon.getAsJsonObject(), new JsonContext("minecraft"));
            if (!stack.isEmpty())
                return () -> renderStack(stack, size);
        }
        return null;
    }

    private static void renderSprite(Sprite sprite, Vec2d size, boolean mask) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        if (!mask)
            GlStateManager.color(1, 1, 1, 1);

        sprite.getTex().bind();
        sprite.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, size.getXi(), size.getYi());

        GlStateManager.popMatrix();
    }

    private static void renderStack(ItemStack stack, Vec2d size) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.scale(size.getX() / 16.0, size.getY() / 16.0, 0);

        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        itemRender.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, 0, 0);

        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void placeInFocus(IBookElement element) {
        if (element == actualElement(this))
            return;

        if (currentElement != null)
            history.push(currentElement);
        forceInFocus(element);
    }

    public void forceInFocus(IBookElement element) {
        if (element == actualElement(this))
            return;

        if (focus != null)
            focus.invalidate();
        bookComponent.add(focus = element.createComponent(this));
        currentElement = element;
    }

    public GuiComponent createIndexButton(int indexID, Entry entry, @Nullable Consumer<ComponentVoid> extra) {
        ComponentVoid indexButton = new ComponentVoid(0, 16 * indexID, bookComponent.getSize().getXi() - 32, 16);

        if (extra != null) extra.accept(indexButton);
        indexButton.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            history.add(entry);
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

            Runnable render = getRendererFor(icon, new Vec2d(16, 16));

            if (render != null)
                indexButton.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
                    render.run();
                });
        }

        return indexButton;
    }
}
