package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static eladkay.quaeritum.client.gui.book.GuiBook.*;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentNavBar extends GuiComponent {

    public int maxPages;
    private int page = 0;
    private final GuiBook book;

    public ComponentNavBar(GuiBook book, int posX, int posY, int width, int pageCount) {
        super(posX, posY, width, 20);

        this.book = book;

        maxPages = Math.max(0, pageCount - 1);

        ComponentSprite back = new ComponentSprite(ARROW_BACK, 0, (int) ((getSize().getY() / 2.0) - (ARROW_NEXT.getHeight() / 2.0)));
        ComponentSprite home = new ComponentSprite(ARROW_HOME, (int) ((getSize().getX() / 2.0) - (ARROW_HOME.getWidth() / 2.0)), (int) ((getSize().getY() / 2.0) - (ARROW_NEXT.getHeight() / 2.0)));
        ComponentSprite next = new ComponentSprite(ARROW_NEXT, (int) (getSize().getX() - ARROW_NEXT.getWidth()), (int) ((getSize().getY() / 2.0) - (ARROW_BACK.getHeight() / 2.0)));
        add(back, next, home);

        home.BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {
            home.setSprite(ARROW_HOME_PRESSED);
            home.getColor().setValue(book.mainColor.brighter());
        });
        home.BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {
            home.setSprite(ARROW_HOME);
            home.getColor().setValue(Color.WHITE);
        });
        List<String> homeTooltip = new ArrayList<>();
        homeTooltip.add(I18n.format("librarianlib.book.nav.back"));
        home.render.getTooltip().setValue(homeTooltip);

        home.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            if (GuiBook.isShiftKeyDown()) {
                book.placeInFocus(book.book);
            } else if (!book.history.empty()) {
                book.forceInFocus(book.history.pop());
            }
        });
        home.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
            if (book.history.empty()) home.setVisible(false);
            else home.setVisible(true);
        });

        back.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
            int x = MathHelper.clamp(page - 1, 0, maxPages);
            if (page == x) back.setVisible(false);
            else back.setVisible(true);

            if (!back.isVisible()) return;

            if (event.component.getMouseOver()) {
                back.setSprite(ARROW_BACK_PRESSED);
                back.getColor().setValue(book.mainColor.brighter());
            } else {
                back.setSprite(ARROW_BACK);
                back.getColor().setValue(Color.WHITE);
            }
        });
        back.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            setPage(page - 1);
        });
        List<String> backTooltip = new ArrayList<>();
        backTooltip.add(I18n.format("librarianlib.book.nav.previous"));
        back.render.getTooltip().setValue(backTooltip);

        next.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
            int x = MathHelper.clamp(page + 1, 0, maxPages);
            if (page == x) next.setVisible(false);
            else next.setVisible(true);

            if (!next.isVisible()) return;

            if (event.component.getMouseOver()) {
                next.setSprite(ARROW_NEXT_PRESSED);
                next.getColor().setValue(book.mainColor.brighter());
            } else {
                next.setSprite(ARROW_NEXT);
                next.getColor().setValue(Color.WHITE);
            }
        });
        next.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            setPage(page + 1);
        });
        List<String> nextTooltip = new ArrayList<>();
        nextTooltip.add(I18n.format("librarianlib.book.nav.next"));
        next.render.getTooltip().setValue(nextTooltip);
    }


    public void setPage(int target) {
        int x = MathHelper.clamp(target, 0, maxPages);
        if (page == x) return;

        page = x;

        EventNavBarChange eventNavBarChange = new EventNavBarChange(page);
        BUS.fire(eventNavBarChange);

        book.currentElement = new ElementWithPage(ElementWithPage.actualElement(book), x);
    }

    public void whenMaxPagesSet() {
        if (maxPages > 1) {
            ComponentText pageStringComponent = new ComponentText((int) getSize().getX() / 2, (int) ((getSize().getY() / 2 - ARROW_NEXT.getHeight() / 2.0)) + 15, ComponentText.TextAlignH.CENTER, ComponentText.TextAlignV.MIDDLE);
            pageStringComponent.getUnicode().setValue(false);

            String initialString = (page + 1) + "/" + (maxPages + 1);
            pageStringComponent.getText().setValue(initialString);

            pageStringComponent.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
                String pageString = (page + 1) + "/" + (maxPages + 1);
                pageStringComponent.getText().setValue(pageString);
            });
            add(pageStringComponent);
        }
    }

    public int getPage() {
        return page;
    }

    public static class ElementWithPage implements IBookElement {
        public static IBookElement actualElement(GuiBook book) {
            if (book.currentElement == null)
                return null;
            return book.currentElement.heldElement();
        }

        private final IBookElement element;
        private final int page;

        public ElementWithPage(IBookElement element, int page) {
            this.element = element;
            this.page = page;
        }

        @Override
        public IBookElement heldElement() {
            return element.heldElement();
        }

        @Override
        public @Nullable IBookElement getBookParent() {
            return element.getBookParent();
        }

        @Override
        public GuiComponent createComponent(GuiBook book) {
            GuiComponent component = element.createComponent(book);
            if (component instanceof NavBarHolder)
                ((NavBarHolder) component).navBar.setPage(page);
            return component;
        }
    }
}
