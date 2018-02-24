package eladkay.quaeritum.client.gui.book;

import com.google.common.collect.Lists;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ComponentSearchResults extends NavBarHolder implements IBookElement {

    private final HashMap<Integer, GuiComponent> pages = new HashMap<>();
    private final int margin = 16;
    private GuiComponent currentActive;
    private ComponentNavBar navBar = null;
    private ComponentText pageHeader;
    private ComponentVoid resultSection;
    private GuiBook book;

    private boolean lastWasFrequency = false;
    private List<Search.FrequencySearchResult> freq = Lists.newArrayList();
    private List<Search.MatchCountSearchResult> match = Lists.newArrayList();

    public ComponentSearchResults(GuiBook book) {
        super(16, 16, book.bookComponent.getSize().getXi() - 32, book.bookComponent.getSize().getYi() - 32, book);
        this.book = book;

        pageHeader = new ComponentText(0, 0, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
        pageHeader.getText().setValue(I18n.format("librarianlib.book.results.notfound"));
        pageHeader.getUnicode().setValue(true);
        pageHeader.getWrap().setValue(getSize().getXi());
        add(pageHeader);

        resultSection = new ComponentVoid(0, margin, getSize().getXi(), getSize().getYi() - margin);
        add(resultSection);
    }

    public void updateTfidfSearches(List<Search.FrequencySearchResult> results) {
        reset();

        lastWasFrequency = true;
        freq = results;

        pageHeader.getText().setValue(results.size() == 1 ?
                I18n.format("librarianlib.book.results.oneresult") :
                I18n.format("librarianlib.book.results.nresults", results.size()));

        Collections.sort(results);

        ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
        resultSection.add(pageComponent);

        double largestTFIDF = 0, smallestTFIDF = Integer.MAX_VALUE;
        for (Search.FrequencySearchResult resultItem2 : results) {
            largestTFIDF = resultItem2.getFrequency() > largestTFIDF ? resultItem2.getFrequency() : largestTFIDF;
            smallestTFIDF = resultItem2.getFrequency() < smallestTFIDF ? resultItem2.getFrequency() : smallestTFIDF;
        }

        int itemsPerPage = 8;
        int page = 0;
        int count = 0;
        for (Search.FrequencySearchResult resultItem : results) {

            double matchPercentage = results.size() == 1 ? 100 : Math.round((resultItem.getFrequency() - smallestTFIDF) / (largestTFIDF - smallestTFIDF) * 100);
            if (matchPercentage <= 0) continue;

            Entry resultComponent = resultItem.getResultComponent();

            ComponentText textComponent = new ComponentText(25, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);

            GuiComponent indexButton = book.createIndexButton(count, resultComponent, plate -> plate.add(textComponent));
            pageComponent.add(indexButton);

            // --------- HANDLE EXTRA TEXT COMPONENT --------- //
            {

                final TextFormatting color;
                if (matchPercentage > 25) {
                    if (matchPercentage <= 50) {
                        color = TextFormatting.YELLOW;
                    } else if (matchPercentage <= 75) {
                        color = TextFormatting.GREEN;
                    } else if (matchPercentage <= 100) {
                        color = TextFormatting.DARK_GREEN;
                    } else color = TextFormatting.DARK_RED;
                } else color = TextFormatting.DARK_RED;


                double finalMatchPercentage = Math.round(matchPercentage);
                String mpercent = I18n.format("librarianlib.book.results.match", matchPercentage);
                String fmpercent = I18n.format("librarianlib.book.results.match", finalMatchPercentage);

                textComponent.getUnicode().setValue(true);
                textComponent.getText().setValue("| " + color + mpercent);

                indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
                    textComponent.getText().setValue("  | " + color + TextFormatting.ITALIC + fmpercent);
                });

                indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
                    textComponent.getText().setValue("| " + color + fmpercent);
                });
            }
            // --------- HANDLE EXTRA TEXT COMPONENT --------- //

            count++;
            if (count >= itemsPerPage) {
                pages.put(page++, pageComponent);
                pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
                resultSection.add(pageComponent);
                pageComponent.setVisible(false);
                count = 0;
            }
        }

        navBar = new ComponentNavBar(book, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size());
        add(navBar);

        navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
            update();
        });
    }

    public void updateMatchCountSearches(List<Search.MatchCountSearchResult> results) {
        reset();

        lastWasFrequency = false;
        match = results;

        pageHeader.getText().setValue(I18n.format("librarianlib.book.results.toobroad", results.size()));

        Collections.sort(results);

        ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
        resultSection.add(pageComponent);

        int itemsPerPage = 8;
        int page = 0;
        int count = 0;
        for (Search.MatchCountSearchResult resultItem : results) {

            Entry resultComponent = resultItem.getResultComponent();

            ComponentText textComponent = new ComponentText(25, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);

            GuiComponent indexButton = book.createIndexButton(count, resultComponent, plate -> plate.add(textComponent));
            pageComponent.add(indexButton);

            // --------- HANDLE EXTRA TEXT COMPONENT --------- //
            {

                String kwds = resultItem.getMatchCount() == 1 ?
                        I18n.format("librarianlib.book.results.kwd") :
                        I18n.format("librarianlib.book.results.kwds", resultItem.getMatchCount());

                textComponent.getUnicode().setValue(true);
                textComponent.getText().setValue("| " + kwds);

                indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
                    textComponent.getText().setValue("  | " + TextFormatting.ITALIC.toString() + kwds);
                });

                indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
                    textComponent.getText().setValue("| " + TextFormatting.RESET.toString() + kwds);
                });
            }
            // --------- HANDLE EXTRA TEXT COMPONENT --------- //

            count++;
            if (count >= itemsPerPage) {
                pages.put(page++, pageComponent);
                pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
                resultSection.add(pageComponent);
                pageComponent.setVisible(false);
                count = 0;
            }

        }

        navBar = new ComponentNavBar(book, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size());
        add(navBar);

        navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
            update();
        });
    }

    public void setAsBadSearch() {
        reset();
        pageHeader.getText().setValue(I18n.format("librarianlib.book.results.notfound"));

        navBar = new ComponentNavBar(book, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size());
        add(navBar);

        navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
            update();
        });
    }

    private void reset() {
        resultSection.invalidate();
        resultSection = new ComponentVoid(0, margin, getSize().getXi(), getSize().getYi() - margin);
        add(resultSection);
        pages.clear();
        if (navBar != null) navBar.invalidate();
    }

    @Override
    public void update() {
        if (currentActive != null) currentActive.setVisible(false);

        currentActive = pages.get(navBar.getPage());

        if (currentActive != null) currentActive.setVisible(true);
    }

    @Override
    public GuiComponent createComponent(GuiBook book) {
        ComponentSearchResults results = new ComponentSearchResults(book);
        if (lastWasFrequency)
            results.updateTfidfSearches(freq);
        else
            results.updateMatchCountSearches(match);
        return results;
    }

    @Override
    public @Nullable IBookElement getBookParent() {
        return book.book;
    }
}
