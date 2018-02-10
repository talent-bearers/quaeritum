package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

public class ComponentSearchResults extends GuiComponent {

	private final HashMap<Integer, GuiComponent> pages = new HashMap<>();

	private GuiComponent currentActive;
	private ComponentNavBar navBar;

	public ComponentSearchResults(GuiBook book, @Nonnull GuiComponent parent, Set<GuiBook.SearchResultItem> results) {
		super(16, 16, book.COMPONENT_BOOK.getSize().getXi() - 32, book.COMPONENT_BOOK.getSize().getYi() - 32);

		ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
		add(pageComponent);

		int itemsPerPage = 6;
		int page = 0;
		int count = 0;
		for (GuiBook.SearchResultItem resultItem : results) {
			GuiComponent resultComponent = resultItem.getResultComponent();
			if (resultComponent instanceof BookGuiComponent) {
				GuiComponent indexButton = ((BookGuiComponent) resultComponent).getOrMakeIndexButton(count, book, this);
				pageComponent.add(indexButton);

				if (page != 0) {
					indexButton.setVisible(false);
				}

				count++;
				if (count >= itemsPerPage) {
					pages.put(page++, pageComponent);
					pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
					add(pageComponent);
					pageComponent.setVisible(false);
					count = 0;
				}
			}
		}

		navBar = new ComponentNavBar(book, this, parent, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size() - 1);
		add(navBar);

		navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
			if (currentActive != null) currentActive.setVisible(false);

			currentActive = pages.get(navBar.getPage());
			currentActive.setVisible(true);
		});

	}
}
