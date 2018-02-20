package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;

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
            ComponentSearchBar bar = new ComponentSearchBar(this, bookmarkID++, this::search, null);
            bookComponent.add(bar);
        }
        // --------- SEARCH BAR --------- //

        placeInFocus(book);
    }

    public static Runnable getRendererFor(JsonElement icon) {
        return getRendererFor(icon, false);
    }

    public static Runnable getRendererFor(JsonElement icon, boolean mask) {
        if (icon == null) return null;

        if (icon.isJsonPrimitive()) {
            ResourceLocation iconLocation = new ResourceLocation(icon.getAsString());
            Sprite sprite = new Sprite(new ResourceLocation(iconLocation.getResourceDomain(),
                    "textures/" + iconLocation.getResourcePath() + ".png"));
            return () -> renderSprite(sprite, mask);
        } else if (icon.isJsonObject()) {
            ItemStack stack = CraftingHelper.getItemStack(icon.getAsJsonObject(), new JsonContext("minecraft"));
            if (!stack.isEmpty())
                return () -> renderStack(stack);
        }
        return null;
    }

    private static void renderSprite(Sprite sprite, boolean mask) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        if (!mask)
            GlStateManager.color(1, 1, 1, 1);

        sprite.getTex().bind();
        sprite.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, 16, 16);

        GlStateManager.popMatrix();
    }

    private static void renderStack(ItemStack stack) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();

        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        itemRender.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, 0, 0);

        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void placeInFocus(IBookElement element) {
        if (element == currentElement)
            return;

        if (currentElement != null)
            history.push(currentElement);
        if (focus != null)
            focus.invalidate();
        bookComponent.add(focus = element.createComponent(this));
        currentElement = element;
    }

    public void forceInFocus(IBookElement element) {
        if (element == currentElement)
            return;

        if (focus != null)
            focus.invalidate();
        bookComponent.add(focus = element.createComponent(this));
        currentElement = element;
    }

    public void search(String type) {
        ComponentSearchResults toFocus = focus instanceof ComponentSearchResults ?
                (ComponentSearchResults) focus :
                new ComponentSearchResults(this);


        String query = type.replace("'", "").toLowerCase(Locale.ROOT);
        String[] keywords = query.split(" ");

        ArrayList<TfidfSearchResult> unfilteredTfidfResults = new ArrayList<>();
        ArrayList<MatchCountSearchResult> matchCountSearchResults = new ArrayList<>();

        final int nbOfDocuments = contentCache.size();
        for (Entry cachedComponent : contentCache.keySet()) {
            String cachedDocument = contentCache
                    .get(cachedComponent)
                    .toLowerCase(Locale.ROOT)
                    .replace("'", "");

            List<String> words = Arrays.asList(cachedDocument.split("\\s+"));
            long mostRepeatedWord =
                    words.stream()
                            .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                            .entrySet()
                            .stream()
                            .max(Comparator.comparing(Map.Entry::getValue))
                            .get().getValue();

            double documentTfidf = 0;
            for (String keyword : keywords) {
                long keywordOccurance = Pattern.compile("\\b" + keyword).splitAsStream(cachedDocument).count() - 1;
                double termFrequency = 0.5 + (0.5 * keywordOccurance / mostRepeatedWord);

                int keywordDocumentOccurance = 0;
                for (Entry documentComponent : contentCache.keySet()) {
                    String documentContent = contentCache.get(documentComponent).toLowerCase(Locale.ROOT);
                    if (documentContent.contains(keyword)) {
                        keywordDocumentOccurance++;
                    }
                }
                keywordDocumentOccurance = keywordDocumentOccurance == 0 ? keywordDocumentOccurance + 1 : keywordDocumentOccurance;

                double inverseDocumentFrequency = Math.log(nbOfDocuments / (keywordDocumentOccurance));

                double keywordTfidf = termFrequency * inverseDocumentFrequency;

                documentTfidf += keywordTfidf;
            }

            unfilteredTfidfResults.add(new TfidfSearchResult(cachedComponent, documentTfidf));
        }

        ArrayList<TfidfSearchResult> filteredTfidfResults = new ArrayList<>();

        double largestTFIDF = 0, smallestTFIDF = Integer.MAX_VALUE;
        for (TfidfSearchResult resultItem2 : unfilteredTfidfResults) {
            largestTFIDF = resultItem2.getTfidfrequency() > largestTFIDF ? resultItem2.getTfidfrequency() : largestTFIDF;
            smallestTFIDF = resultItem2.getTfidfrequency() < smallestTFIDF ? resultItem2.getTfidfrequency() : smallestTFIDF;
        }

        for (TfidfSearchResult resultItem : unfilteredTfidfResults) {
            double matchPercentage = Math.round((resultItem.getTfidfrequency() - smallestTFIDF) / (largestTFIDF - smallestTFIDF) * 100);
            if (matchPercentage < 5 || Double.isNaN(matchPercentage)) continue;

            filteredTfidfResults.add(resultItem);
        }

        if (!filteredTfidfResults.isEmpty()) {
            toFocus.updateTfidfSearches(filteredTfidfResults);
        } else {
            for (Entry cachedComponent : contentCache.keySet()) {
                String cachedDocument = contentCache
                        .get(cachedComponent)
                        .toLowerCase(Locale.ROOT)
                        .replace("'", "");

                int mostMatches = 0;
                for (String keyword : keywords) {
                    int keywordOccurances = StringUtils.countMatches(cachedDocument, keyword);
                    mostMatches += keywordOccurances;
                }

                if (mostMatches > 0)
                    matchCountSearchResults.add(new MatchCountSearchResult(cachedComponent, mostMatches));
            }

            if (!matchCountSearchResults.isEmpty()) {
                toFocus.updateMatchCountSearches(matchCountSearchResults);
            } else {
                toFocus.setAsBadSearch();
            }
        }

        placeInFocus(toFocus);
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
            final String title = I18n.format(entry.titleKey);
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

            Runnable render = getRendererFor(icon);

            if (render != null)
                indexButton.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
                    render.run();
                });
        }

        return indexButton;
    }

    public static class TfidfSearchResult implements Comparable<TfidfSearchResult> {

        private final Entry resultComponent;
        private final double tfidfrequency;

        public TfidfSearchResult(Entry resultComponent, double tfidfrequency) {
            this.resultComponent = resultComponent;
            this.tfidfrequency = tfidfrequency;
        }

        public Entry getResultComponent() {
            return resultComponent;
        }

        public double getTfidfrequency() {
            return tfidfrequency;
        }

        @Override
        public int compareTo(@NotNull GuiBook.TfidfSearchResult o) {
            return Double.compare(o.getTfidfrequency(), getTfidfrequency());
        }
    }

    public static class MatchCountSearchResult implements Comparable<MatchCountSearchResult> {

        private final Entry resultComponent;
        private final int nbOfMatches;

        public MatchCountSearchResult(Entry resultComponent, int nbOfMatches) {
            this.resultComponent = resultComponent;
            this.nbOfMatches = nbOfMatches;
        }

        public Entry getResultComponent() {
            return resultComponent;
        }

        public int getMatchCount() {
            return nbOfMatches;
        }

        @Override
        public int compareTo(@NotNull GuiBook.MatchCountSearchResult o) {
            return Double.compare(o.getMatchCount(), getMatchCount());
        }
    }
}
