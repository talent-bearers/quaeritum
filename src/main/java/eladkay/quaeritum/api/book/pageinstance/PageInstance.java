package eladkay.quaeritum.api.book.pageinstance;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class PageInstance {

	public PageInstance(JsonElement jsonElement) {
	}

	public abstract String getType();

	public abstract String getCachableString();

	@SideOnly(Side.CLIENT)
	public abstract List<GuiComponent> createBookComponents(GuiBook book, Vec2d size);
}
