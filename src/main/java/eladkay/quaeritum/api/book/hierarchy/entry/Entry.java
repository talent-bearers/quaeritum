package eladkay.quaeritum.api.book.hierarchy.entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import eladkay.quaeritum.api.book.hierarchy.page.Page;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Entry implements IBookElement {
	public final Category category;

	public final List<Page> pages;
	public final String titleKey;
	public final String descKey;
	public final JsonElement icon;

	public boolean isValid = false;

	public Entry(Category category, JsonObject json) {
		List<Page> pages = Lists.newArrayList();
		String titleKey = "";
		String descKey = "";
		JsonElement icon = new JsonObject();

		this.category = category;
		try {
			titleKey = json.getAsJsonPrimitive("title").getAsString();
			descKey = json.getAsJsonPrimitive("description").getAsString();
			icon = json.get("icon");
			JsonArray allPages = json.getAsJsonArray("content");
			for (JsonElement pageJson : allPages) {
				Page page = Page.fromJson(this, pageJson);
				if (page != null)
					pages.add(page);
			}
			isValid = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		this.pages = pages;
		this.titleKey = titleKey;
		this.descKey = descKey;
		this.icon = icon;
	}

	@Override
	public @Nullable IBookElement getParent() {
		return category;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiComponent createComponent(GuiBook book, Vec2d size) {
		return null; // todo saad
	}
}
