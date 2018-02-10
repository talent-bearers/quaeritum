package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import eladkay.quaeritum.api.structure.StructureCacheRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentEntryPage extends BookGuiComponent {

	private final GuiBook book;
	private final HashMap<Integer, GuiComponent> pages = new HashMap<>();
	@Nullable
	private String icon;
	private String title;
	private String description;

	private GuiComponent currentActive;

	private ComponentNavBar navBar;

	public ComponentEntryPage(GuiBook book, @Nonnull GuiComponent parent, JsonObject entryObject) {
		super(16, 16, book.COMPONENT_BOOK.getSize().getXi() - 32, book.COMPONENT_BOOK.getSize().getYi() - 32);
		this.book = book;

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

			book.contentCache.put(this, title);
			book.contentCache.put(this, description);

			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

			int page = 0;
			for (int i = 0; i < entryContentArray.size(); i++) {
				JsonElement element = entryContentArray.get(i);

				if (element.isJsonPrimitive()) {
					String text = element.getAsJsonPrimitive().getAsString();

					List<String> list = fontRenderer.listFormattedStringToWidth(text, 2200);

					for (String section : list) {
						ComponentText sectionComponent = new ComponentText(0, 0, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
						sectionComponent.getText().setValue(section);
						sectionComponent.getWrap().setValue(getSize().getXi());
						sectionComponent.getUnicode().setValue(true);

						if (page == 0) {
							currentActive = sectionComponent;
						} else {
							sectionComponent.setVisible(false);
						}

						add(sectionComponent);
						pages.put(page++, sectionComponent);

						book.contentCache.put(this, section);
					}
				} else if (element.isJsonObject()) {
					JsonObject object = element.getAsJsonObject();

					if (object.has("type") && object.get("type").isJsonPrimitive()) {

						String sectionType = object.getAsJsonPrimitive("type").getAsString();

						if (sectionType.equalsIgnoreCase("structure")) {
							if (object.has("name") && object.get("name").isJsonPrimitive()) {

								String name = object.getAsJsonPrimitive("name").getAsString();

								book.contentCache.put(this, name.replace("_", " "));

								ComponentStructure structure = new ComponentStructure(0, 0, getSize().getXi(), getSize().getYi(), StructureCacheRegistry.INSTANCE.getStructureOrAdd(name));

								if (page == 0) {
									currentActive = structure;
								} else {
									structure.setVisible(false);
								}

								add(structure);
								pages.put(page++, structure);
							}
						}
					}
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

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Nonnull
	@Override
	public GuiBook getBook() {
		return book;
	}

	@Nullable
	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public void update() {
		navBar.BUS.fire(new EventNavBarChange(navBar.getPage()));
	}
}
