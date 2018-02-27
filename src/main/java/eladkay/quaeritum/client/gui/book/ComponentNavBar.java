package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentNavBar extends GuiComponent {

    private final IBookGui book;
    private final Sprite nextSprite;
    public int maxPages;
    private int page = 0;

    public ComponentNavBar(IBookGui book, int posX, int posY, int width, int pageCount) {
        super(posX, posY, width, 20);

        this.book = book;

        maxPages = Math.max(0, pageCount - 1);

        Sprite backSprite = book.backSprite(false);
        Sprite homeSprite = book.homeSprite(false);
        nextSprite = book.nextSprite(false);

        ComponentSprite back = new ComponentSprite(backSprite, 0, (int) ((getSize().getY() / 2.0) - (backSprite.getHeight() / 2.0)));
        ComponentSprite home = new ComponentSprite(homeSprite, (int) ((getSize().getX() / 2.0) - (homeSprite.getWidth() / 2.0)), (int) ((getSize().getY() / 2.0) - (backSprite.getHeight() / 2.0)));
        ComponentSprite next = new ComponentSprite(nextSprite, (int) (getSize().getX() - nextSprite.getWidth()), (int) ((getSize().getY() / 2.0) - (nextSprite.getHeight() / 2.0)));
        add(back, next, home);

        home.BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {
            home.setSprite(book.homeSprite(true));
            home.getColor().setValue(book.getBook().bookColor.brighter());
        });
        home.BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {
            home.setSprite(book.homeSprite(false));
            home.getColor().setValue(Color.WHITE);
        });
        List<String> homeTooltip = new ArrayList<>();
        homeTooltip.add(I18n.format("librarianlib.book.nav.back"));
        home.render.getTooltip().setValue(homeTooltip);

        home.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            if (GuiScreen.isShiftKeyDown()) {
                book.placeInFocus(book.getBook());
            } else if (!book.getHistory().empty()) {
                book.forceInFocus(book.getHistory().pop());
            }
        });
        home.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
            if (book.getHistory().empty()) home.setVisible(false);
            else home.setVisible(true);
        });

        back.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
            int x = MathHelper.clamp(page - 1, 0, maxPages);
            if (page == x) back.setVisible(false);
            else back.setVisible(true);

            if (!back.isVisible()) return;

            if (event.component.getMouseOver()) {
                back.setSprite(book.backSprite(true));
                back.getColor().setValue(book.getBook().bookColor.brighter());
            } else {
                back.setSprite(book.backSprite(false));
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

            next.setSprite(book.nextSprite(event.component.getMouseOver()));
            if (event.component.getMouseOver())
                next.getColor().setValue(book.getBook().bookColor.brighter());
            else
                next.getColor().setValue(Color.WHITE);
        });
        next.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
            setPage(page + 1);
        });
        List<String> nextTooltip = new ArrayList<>();
        nextTooltip.add(I18n.format("librarianlib.book.nav.next"));
        next.render.getTooltip().setValue(nextTooltip);
    }

    public void whenMaxPagesSet() {
        if (maxPages > 1) {
            ComponentText pageStringComponent = new ComponentText((int) getSize().getX() / 2, (int) ((getSize().getY() / 2 - nextSprite.getHeight() / 2.0)) + 15, ComponentText.TextAlignH.CENTER, ComponentText.TextAlignV.MIDDLE);
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

    public void setPage(int target) {
        int x = MathHelper.clamp(target, 0, maxPages);
        if (page == x) return;

        page = x;

        EventNavBarChange eventNavBarChange = new EventNavBarChange(page);
        BUS.fire(eventNavBarChange);

        book.setCurrentElement(new ElementWithPage(book.actualElement(), x));
    }

    public static class ElementWithPage implements IBookElement {

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
        public GuiComponent createComponent(IBookGui book) {
            GuiComponent component = element.createComponent(book);
            if (component instanceof NavBarHolder)
                ((NavBarHolder) component).navBar.setPage(page);
            return component;
        }
    }
}
