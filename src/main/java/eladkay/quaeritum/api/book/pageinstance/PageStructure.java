package eladkay.quaeritum.api.book.pageinstance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.structure.StructureCacheRegistry;
import eladkay.quaeritum.client.gui.book.ComponentStructure;
import eladkay.quaeritum.client.gui.book.GuiBook;

import java.util.ArrayList;
import java.util.List;

public class PageStructure extends PageInstance {

	private String structureName = null;

	public PageStructure(JsonElement jsonElement) {
		super(jsonElement);

		if (!jsonElement.isJsonObject()) return;

		JsonObject object = jsonElement.getAsJsonObject();

		if (object.has("name") && object.get("name").isJsonPrimitive()) {
			structureName = object.getAsJsonPrimitive("name").getAsString();
		}
	}

	@Override
	public String getType() {
		return "structure";
	}

	@Override
	public String getCachableString() {
		return structureName;
	}

	@Override
	public List<GuiComponent> createBookComponents(GuiBook book, Vec2d size) {
		List<GuiComponent> pages = new ArrayList<>();

		if (structureName == null) return pages;

		ComponentStructure structure = new ComponentStructure(0, 0, size.getXi(), size.getYi(), StructureCacheRegistry.INSTANCE.getStructureOrAdd(structureName));
		pages.add(structure);

		return pages;
	}
}
