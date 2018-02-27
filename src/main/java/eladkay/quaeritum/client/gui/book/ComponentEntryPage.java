package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import eladkay.quaeritum.api.book.hierarchy.page.Page;
import net.minecraft.client.resources.I18n;


/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentEntryPage extends NavBarHolder {

    public ComponentEntryPage(IBookGui book, Entry entry) {
        super(16, 16, book.getMainComponent().getSize().getXi() - 32, book.getMainComponent().getSize().getYi() - 32, book);

        String title = I18n.format(entry.titleKey);

        ComponentSprite titleBar = new ComponentSprite(book.titleBarSprite(),
                (int) ((getSize().getX() / 2.0) - (book.titleBarSprite().getWidth() / 2.0)),
                -getPos().getXi() - 15);
        titleBar.getColor().setValue(book.getBook().bookColor);
        add(titleBar);

        ComponentText titleText = new ComponentText((int) (titleBar.getSize().getX() / 2.0), (int) (titleBar.getSize().getY() / 2.0) + 1, ComponentText.TextAlignH.CENTER, ComponentText.TextAlignV.MIDDLE);
        titleText.getText().setValue(title);
        titleBar.add(titleText);

        for (Page page : entry.pages) {
            for (GuiComponent pageComponent : page.createBookComponents(book, getSize())) {
                addPage(pageComponent);
            }
            navBar.whenMaxPagesSet();
        }
    }
}
