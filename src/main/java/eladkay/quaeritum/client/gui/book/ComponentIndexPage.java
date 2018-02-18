package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentIndexPage extends BookGuiComponent {

	private final HashMap<Integer, GuiComponent> pages = new HashMap<>();
	private final Category category;
	private String description;
	private String title;
	private JsonElement icon;

	private GuiComponent currentActive;
	private ComponentNavBar navBar;

	public ComponentIndexPage(GuiBook book, BookGuiComponent parent, Category category) {
		super(16, 16, book.COMPONENT_BOOK.getSize().getXi() - 32, book.COMPONENT_BOOK.getSize().getYi() - 32, book, parent);
		this.category = category;

		this.title = I18n.format(category.titleKey);
		this.description = I18n.format(category.descKey);
		this.icon = category.icon;

		ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
		add(pageComponent);
		currentActive = pageComponent;

		int itemsPerPage = 9;
		int page = 0;
		int count = 0;
		for (Entry entry : category.entries) {

			BookGuiComponent contentComponent = new ComponentEntryPage(book, this, entry, true);

			contentComponent.setLinkingParent(this);
			contentComponent.setVisible(false);
			book.COMPONENT_BOOK.add(contentComponent);

			GuiComponent indexButton = contentComponent.createIndexButton(count, book, null);
			pageComponent.add(indexButton);
			indexButton.setVisible(page == 0);

			count++;
			if (count >= itemsPerPage) {
                pages.put(page++, pageComponent);
                pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
                add(pageComponent);
                pageComponent.setVisible(false);
                count = 0;
            }

		}

		navBar = new ComponentNavBar(book, this, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size());
		add(navBar);

		navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
			update();
		});
	}

	@Nullable
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
		return new ComponentIndexPage(getBook(), getLinkingParent(), category);
	}
}
