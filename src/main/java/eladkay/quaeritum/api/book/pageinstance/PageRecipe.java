package eladkay.quaeritum.api.book.pageinstance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.client.gui.book.ComponentRecipe;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PageRecipe extends PageInstance {

	private String recipeItem = null;

	public PageRecipe(JsonElement jsonElement) {
		super(jsonElement);

		if (!jsonElement.isJsonObject()) return;

		JsonObject object = jsonElement.getAsJsonObject();

		if (object.has("item") && object.get("item").isJsonPrimitive()) {
			//if (cache)
			//	contentCache.append("\n").append(object.getAsJsonPrimitive("item").getAsString().replace("_", " "));
			recipeItem = object.getAsJsonPrimitive("item").getAsString();
		}
	}

	@Override
	public String getType() {
		return "structure";
	}

	@Override
	public String getCachableString() {
		return recipeItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiComponent> createBookComponents(GuiBook book, Vec2d size) {
		List<GuiComponent> pages = new ArrayList<>();

		if (recipeItem == null) return pages;

		ComponentRecipe recipe = new ComponentRecipe(0, 0, size.getXi(), size.getYi(), book.mainColor, recipeItem, null);
		pages.add(recipe);

		return pages;
	}
}
