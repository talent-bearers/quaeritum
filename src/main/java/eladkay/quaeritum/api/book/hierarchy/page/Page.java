package eladkay.quaeritum.api.book.hierarchy.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.book.provider.PageTypes;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface Page {

	@NotNull
	String getType();

	@Nullable
	default List<String> getSearchableStrings() {
		return null;
	}

	@Nullable
	default List<String> getSearchableKeys() {
		return null;
	}

	@SideOnly(Side.CLIENT)
	List<GuiComponent> createBookComponents(GuiBook book, Vec2d size);

	static Page fromJson(JsonElement element) {
		try {
			JsonObject obj = null;
			Function<JsonObject, Page> provider = null;
			if (element.isJsonPrimitive()) {
				provider = PageTypes.getPageProvider("text");
				obj = new JsonObject();
				obj.addProperty("type", "text");
				obj.addProperty("value", element.getAsString());
			} else if (element.isJsonObject()) {
				obj = element.getAsJsonObject();
				provider = PageTypes.getPageProvider(obj.getAsJsonPrimitive("type").getAsString());
			}

			if (obj == null || provider == null)
				return null;

			return provider.apply(obj);
		} catch (Exception error) {
			error.printStackTrace();
			return null;
		}
	}
}
