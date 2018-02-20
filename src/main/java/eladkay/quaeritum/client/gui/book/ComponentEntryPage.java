package eladkay.quaeritum.client.gui.book;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import eladkay.quaeritum.api.book.hierarchy.page.Page;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static eladkay.quaeritum.client.gui.book.GuiBook.TITLE_BAR;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentEntryPage extends BookGuiComponent {

    private final List<GuiComponent> pages = Lists.newArrayList();
    private final Entry entry;
    private JsonElement icon;
    private String title;
    private String description;

    private GuiComponent currentActive;

    private ComponentNavBar navBar;

    public ComponentEntryPage(GuiBook book, BookGuiComponent parent, Entry entry, boolean cache) {
        super(16, 16, book.bookComponent.getSize().getXi() - 32, book.bookComponent.getSize().getYi() - 32, book, parent);
        this.entry = entry;

        this.title = I18n.format(entry.titleKey);
        this.description = I18n.format(entry.descKey);
        this.icon = entry.icon;

        ComponentSprite titleBar = new ComponentSprite(TITLE_BAR,
                (int) ((getSize().getX() / 2.0) - (TITLE_BAR.getWidth() / 2.0)),
                -getPos().getXi() - 15);
        titleBar.getColor().setValue(book.mainColor);
        add(titleBar);

        ComponentText titleText = new ComponentText((int) (TITLE_BAR.getWidth() / 2.0), (int) (titleBar.getSize().getY() / 2.0) + 1, ComponentText.TextAlignH.CENTER, ComponentText.TextAlignV.MIDDLE);
        titleText.getText().setValue(title);
        titleBar.add(titleText);

        StringBuilder contentCache = new StringBuilder(title + "\n" + description);

        boolean first = true;
        for (Page page : entry.pages) {

            for (GuiComponent pageComponent : page.createBookComponents(book, getSize())) {

                if (first) {
                    currentActive = pageComponent;
                    first = false;
                } else {
                    pageComponent.setVisible(false);
                }

                contentCache.append("\n");
                List<String> searchables = page.getSearchableStrings();
                if (searchables != null) for (String searchable : searchables)
                    contentCache.append(searchable).append(' ');
                searchables = page.getSearchableKeys();
                if (searchables != null) for (String searchable : searchables)
                    contentCache.append(I18n.format(searchable)).append(' ');

                add(pageComponent);
                pages.add(pageComponent);
            }
        }

        if (cache) book.contentCache.put(this, contentCache.toString());


        navBar = new ComponentNavBar(book, this, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size() - 1);
        add(navBar);

        navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
            update();
        });
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Nullable
    @Override
    public JsonElement getIcon() {
        return icon;
    }

    @Override
    public void update() {
        if (currentActive != null) currentActive.setVisible(false);

        currentActive = pages.get(navBar.getPage());

        if (currentActive != null) currentActive.setVisible(true);
    }

    @Nonnull
    @Override
    public BookGuiComponent clone() {
        return new ComponentEntryPage(getBook(), getLinkingParent(), entry, false);
    }
}
