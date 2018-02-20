package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentIndexPage extends NavBarHolder {

	public ComponentIndexPage(GuiBook book, Category category) {
		super(16, 16, book.bookComponent.getSize().getXi() - 32, book.bookComponent.getSize().getYi() - 32, book);

		ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
		add(pageComponent);
		currentActive = pageComponent;

		int itemsPerPage = 9;
		int count = 0;
		int id = 0;
		for (Entry entry : category.entries) {

			GuiComponent indexPlate = book.createIndexButton(id++, entry, null);
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
	}
}
