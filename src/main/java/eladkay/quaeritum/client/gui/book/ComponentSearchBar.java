package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static eladkay.quaeritum.client.gui.book.GuiBook.MAGNIFIER;

public class ComponentSearchBar extends ComponentBookMark {

	private ComponentTextField text;

	public ComponentSearchBar(GuiBook book, int id, @Nullable Consumer<String> onType, @Nullable Consumer<String> onEnter) {
		super(book, MAGNIFIER, id);

		text = new ComponentTextField(1, 1, getSize().getXi() - 44 - 2 * MAGNIFIER.getWidth(), Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2);
		text.setEditedCallback(onType);
		text.setEnterCallback(onEnter);
		text.setEnabledColor(0xFFFFFF);
		add(text);

		clipping.setClipToBounds(true);

		BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {
			if (isForcedOpen()) {
				slideOutShort();
				text.setVisible(true);
			}
		});

		BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {
			if (isForcedOpen()) {
				slideIn();
				text.setVisible(false);
			}
		});
	}

	public boolean isForcedOpen() {
	    return !text.isFocused() && text.getText().isEmpty();
    }
}
