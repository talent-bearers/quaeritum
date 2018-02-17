package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import eladkay.quaeritum.api.book.pageinstance.PageInstance;
import eladkay.quaeritum.api.book.pageinstance.PageInstanceFactory;
import eladkay.quaeritum.api.book.pageinstance.PageText;
import eladkay.quaeritum.api.structure.StructureCacheRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

import static eladkay.quaeritum.client.gui.book.GuiBook.TITLE_BAR;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentEntryPage extends BookGuiComponent {

	private final HashMap<Integer, GuiComponent> pages = new HashMap<>();
	private final JsonObject entryObject;
	@Nullable
	private String icon;
	private String title;
	private String description;

	private GuiComponent currentActive;

	private ComponentNavBar navBar;

	public ComponentEntryPage(GuiBook book, BookGuiComponent parent, JsonObject entryObject, boolean cache) {
		super(16, 16, book.COMPONENT_BOOK.getSize().getXi() - 32, book.COMPONENT_BOOK.getSize().getYi() - 32, book, parent);
		this.entryObject = entryObject;

		if (entryObject.has("type") && entryObject.get("type").isJsonPrimitive()
				&& entryObject.has("title") && entryObject.get("title").isJsonPrimitive()
				&& entryObject.has("content") && entryObject.get("content").isJsonArray()
				&& entryObject.has("icon") && entryObject.get("icon").isJsonPrimitive()
				&& entryObject.has("description") && entryObject.get("description").isJsonPrimitive()) {

			String type = entryObject.getAsJsonPrimitive("type").getAsString();
			if (!type.equals("entry")) return;

			String icon = entryObject.getAsJsonPrimitive("icon").getAsString();
			String title = entryObject.getAsJsonPrimitive("title").getAsString();
			String description = entryObject.getAsJsonPrimitive("description").getAsString();
			JsonArray entryContentArray = entryObject.getAsJsonArray("content");

			this.title = title;
			this.description = description;
			this.icon = icon;
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

			ComponentSprite titleBar = new ComponentSprite(TITLE_BAR,
					(int) ((getSize().getX() / 2.0) - (TITLE_BAR.getWidth() / 2.0)),
					-getPos().getXi() - 15);
			titleBar.getColor().setValue(book.mainColor);
			add(titleBar);

			ComponentText titleText = new ComponentText((int) (TITLE_BAR.getWidth() / 2.0), (int) (titleBar.getSize().getY() / 2.0) + 1, ComponentText.TextAlignH.CENTER, ComponentText.TextAlignV.MIDDLE);
			titleText.getText().setValue(title);
			titleBar.add(titleText);

			StringBuilder contentCache = new StringBuilder(title + "\n" + description);

			int page = 0;
			for (int i = 0; i < entryContentArray.size(); i++) {
				JsonElement element = entryContentArray.get(i);

				if (element.isJsonPrimitive()) {

					PageText pageText = new PageText(element);
					List<GuiComponent> pageComponents = pageText.createBookComponents(getSize());
					for (GuiComponent pageComponent : pageComponents)
						pages.put(page++, pageComponent);

				} else if (element.isJsonObject()) {
					JsonObject object = element.getAsJsonObject();

					if (object.has("type") && object.get("type").isJsonPrimitive()) {
						String sectionType = object.getAsJsonPrimitive("type").getAsString();

						for (PageInstance pageInstance : PageInstanceFactory.INSTANCE.pageInstanceSet) {
							if (pageInstance.getType().equals(sectionType)) {

							}
						}

						if (sectionType.equalsIgnoreCase("structure")) {
							if (object.has("name") && object.get("name").isJsonPrimitive()) {

								String name = object.getAsJsonPrimitive("name").getAsString();

								if (cache) contentCache.append("\n").append(name.replace("_", " "));

								ComponentStructure structure = new ComponentStructure(0, 0, getSize().getXi(), getSize().getYi(), StructureCacheRegistry.INSTANCE.getStructureOrAdd(name));

								if (page == 0) {
									currentActive = structure;
								} else {
									structure.setVisible(false);
								}

								add(structure);
								pages.put(page++, structure);
							}
						} else if (sectionType.equalsIgnoreCase("recipe")) {
							if (object.has("item") && object.get("item").isJsonPrimitive()) {
								if (cache)
									contentCache.append("\n").append(object.getAsJsonPrimitive("item").getAsString().replace("_", " "));

								ComponentRecipe recipe = new ComponentRecipe(0, 0, getSize().getXi(), getSize().getYi(), getBook(), object.getAsJsonPrimitive("item").getAsString(), null);

								if (page == 0) {
									currentActive = recipe;
								} else {
									recipe.setVisible(false);
								}

								add(recipe);
								pages.put(page++, recipe);
							}
						}
					}
				}
			}

			if (cache) book.contentCache.put(this, contentCache.toString());
		}

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
	public String getIcon() {
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
		return new ComponentEntryPage(getBook(), getLinkingParent(), entryObject, false);
	}
}
