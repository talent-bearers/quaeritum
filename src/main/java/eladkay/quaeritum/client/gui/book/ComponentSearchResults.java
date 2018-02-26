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

public class ComponentSearchResults extends NavBarHolder implements ISearchAlgorithm.Acceptor {

    private final HashMap<Integer, GuiComponent> pages = new HashMap<>();
    private final int margin = 16;
    private GuiComponent currentActive;
    private ComponentNavBar navBar = null;
    private ComponentText pageHeader;
    private ComponentVoid resultSection;
    private GuiBook book;

    private List<? extends ISearchAlgorithm.Result> results = Lists.newArrayList();

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

    @Override
    public void acceptResults(@Nullable List<? extends ISearchAlgorithm.Result> results) {
        this.results = results;
        reset();

        navBar = new ComponentNavBar(book, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, 1);
        add(navBar);

        navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
            update();
        });

        if (results == null || results.isEmpty()) {
            pageHeader.getText().setValue(I18n.format("librarianlib.book.results.notfound"));
        } else {
            ISearchAlgorithm.Result forTyping = results.get(0);
            if (forTyping.specificResults())
                pageHeader.getText().setValue(results.size() == 1 ?
                        I18n.format("librarianlib.book.results.oneresult") :
                        I18n.format("librarianlib.book.results.nresults", results.size()));
            else
                pageHeader.getText().setValue(I18n.format("librarianlib.book.results.toobroad", results.size()));

            Collections.sort(results);

            ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
            resultSection.add(pageComponent);

            double largestFrequency = 0, smallestFrequency = Integer.MAX_VALUE;
            for (ISearchAlgorithm.Result resultItem : results) {
                if (resultItem.frequency() > largestFrequency)
                    largestFrequency = resultItem.frequency();
                if (resultItem.frequency() < smallestFrequency)
                    smallestFrequency = resultItem.frequency();
            }

            int itemsPerPage = 8;
            int count = 0;
            for (ISearchAlgorithm.Result resultItem : results) {

                double matchPercentage = results.size() == 1 ? 100 : Math.round((resultItem.frequency() - smallestFrequency) / (largestFrequency - smallestFrequency) * 100);
                if (matchPercentage <= 0) continue;

                Entry resultComponent = resultItem.found();

                ComponentText textComponent = new ComponentText(25, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);

                GuiComponent indexButton = book.createIndexButton(count, resultComponent, plate -> plate.add(textComponent));
                pageComponent.add(indexButton);

                // --------- HANDLE EXTRA TEXT COMPONENT --------- //
                {
                    final TextFormatting color;
                    final String exactResult;
                    final String simplifiedResult;
                    if (forTyping.specificResults()) {

                        if (matchPercentage <= 25)
                            color = TextFormatting.DARK_RED;
                        else if (matchPercentage <= 50)
                            color = TextFormatting.YELLOW;
                        else if (matchPercentage <= 75)
                            color = TextFormatting.GREEN;
                        else
                            color = TextFormatting.DARK_GREEN;

                        exactResult = I18n.format("librarianlib.book.results.match", matchPercentage);
                        simplifiedResult = I18n.format("librarianlib.book.results.match", Math.round(matchPercentage));
                    } else {
                        color = TextFormatting.RESET;
                        exactResult = simplifiedResult = resultItem.frequency() == 1 ?
                                I18n.format("librarianlib.book.results.kwd") :
                                I18n.format("librarianlib.book.results.kwds", (int) resultItem.frequency());
                    }

                    textComponent.getUnicode().setValue(true);
                    textComponent.getText().setValue("| " + color + simplifiedResult);

                    indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
                        textComponent.getText().setValue("  | " + color + TextFormatting.ITALIC + exactResult);
                    });

                    indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
                        textComponent.getText().setValue("| " + color + simplifiedResult);
                    });
                }
                // --------- HANDLE EXTRA TEXT COMPONENT --------- //

                count++;
                if (count >= itemsPerPage) {
                    pages.put(navBar.maxPages++, pageComponent);
                    pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
                    resultSection.add(pageComponent);
                    pageComponent.setVisible(false);
                    count = 0;
                }
            }

            navBar.whenMaxPagesSet();
        }
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
        results.acceptResults(this.results);
        return results;
    }

    @Override
    public @Nullable IBookElement getBookParent() {
        return book.book;
    }
}
