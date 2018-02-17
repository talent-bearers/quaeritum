package eladkay.quaeritum.api.book.pageinstance;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PageText extends PageInstance {

	private String text;

	public PageText(JsonElement jsonElement) {
		super(jsonElement);

		if (!jsonElement.isJsonPrimitive()) return;

		text = jsonElement.getAsJsonPrimitive().getAsString();
	}

	@Override
	public String getType() {
		return "text";
	}

	@Override
	public String getCachableString() {
		return text;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiComponent> createBookComponents(GuiBook book, Vec2d size) {
		List<GuiComponent> pages = new ArrayList<>();
		if (text == null) return pages;

		List<String> list = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, 2200);

		for (String section : list) {
			if (section.trim().isEmpty()) continue;

			ComponentText sectionComponent = new ComponentText(0, 0, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
			sectionComponent.getText().setValue(section);
			sectionComponent.getWrap().setValue(size.getXi());
			sectionComponent.getUnicode().setValue(true);

			pages.add(sectionComponent);

			//if (cache) contentCache.append("\n").append(section);
		}
		return pages;
	}
}
