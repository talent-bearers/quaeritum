package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentCategoryPage extends NavBarHolder {

    public ComponentCategoryPage(IBookGui book, Category category) {
        super(16, 16, book.getMainComponent().getSize().getXi() - 32, book.getMainComponent().getSize().getYi() - 32, book);

        ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
        add(pageComponent);
        currentActive = pageComponent;

        int itemsPerPage = 9;
        int count = 0;
        int id = 0;
        EntityPlayer player = Minecraft.getMinecraft().player;
        for (Entry entry : category.entries) {
            if (entry.isUnlocked(player)) {
                GuiComponent indexPlate = book.makeNavigationButton(id++, entry, null);
                pageComponent.add(indexPlate);

                count++;
                if (count >= itemsPerPage) {
                    addPage(pageComponent);
                    pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
                    add(pageComponent);
                    pageComponent.setVisible(false);
                    count = 0;
                    id = 0;
                }
            }
            navBar.whenMaxPagesSet();
        }
    }
}
