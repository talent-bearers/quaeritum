package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import eladkay.quaeritum.api.structure.StructureCacheRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.HashMap;
import java.util.List;

public class ComponentContent extends GuiComponent {

	private HashMap<Integer, GuiComponent> pages = new HashMap<>();

	private GuiComponent currentActive;

	public ComponentContent(String path, JsonArray contentArray) {
		super(16, 16, GuiBook.COMPONENT_BOOK.getSize().getXi() - 32, GuiBook.COMPONENT_BOOK.getSize().getYi() - 32);

		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		int itemsPerPage = 6;
		int page = 0;
		int count = 0;
		for (int i = 0; i < contentArray.size(); i++) {
			JsonElement element = contentArray.get(i);

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

		ComponentNavBar navBar = new ComponentNavBar((getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size() - 1);
		add(navBar);

		navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
			GuiComponent section = pages.get(navBarChange.getPage());
			section.setVisible(true);
			currentActive.setVisible(false);
			currentActive = section;
		});
	}
}
