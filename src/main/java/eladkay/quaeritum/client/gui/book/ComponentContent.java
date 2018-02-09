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
import java.util.HashMap;
import java.util.List;

public class ComponentContent extends BookGuiComponent {

	private HashMap<Integer, GuiComponent> pages = new HashMap<>();

	private GuiComponent currentActive;

	private ComponentNavBar navBar;

	public ComponentContent(GuiBook book, @Nonnull GuiComponent parent, JsonArray contentArray) {
		super(16, 16, book.COMPONENT_BOOK.getSize().getXi() - 32, book.COMPONENT_BOOK.getSize().getYi() - 32);

		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		StringBuilder textCache = new StringBuilder();

		int itemsPerPage = 6;
		int page = 0;
		int count = 0;
		for (int i = 0; i < contentArray.size(); i++) {
			JsonElement element = contentArray.get(i);

			if (element.isJsonPrimitive()) {
				String text = element.getAsJsonPrimitive().getAsString();

				textCache.append(text).append("\n");

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
				}
			} else if (element.isJsonObject()) {
				JsonObject object = element.getAsJsonObject();

				if (object.has("type") && object.get("type").isJsonPrimitive()) {

					String type = object.getAsJsonPrimitive("type").getAsString();

					if (type.equalsIgnoreCase("structure")) {
						if (object.has("name") && object.get("name").isJsonPrimitive()) {

							String name = object.getAsJsonPrimitive("name").getAsString();

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

		if (!book.contentCache.containsKey(this) && !book.contentCache.containsValue(textCache.toString()))
			book.contentCache.put(this, textCache.toString());

		navBar = new ComponentNavBar(book, this, parent, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size() - 1);
		add(navBar);

		navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
			makeVisible();
		});
	}

	@Override
	public void makeVisible() {
		super.makeVisible();
		currentActive.setVisible(false);

		GuiComponent section = pages.get(navBar.getPage());
		if (section != null) {
			if (section instanceof BookGuiComponent)
				((BookGuiComponent) section).makeVisible();
			else section.setVisible(true);
		}
		currentActive = section;
	}
}
