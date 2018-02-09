package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

import static eladkay.quaeritum.client.gui.book.GuiBook.BOOKMARK_LONG;

public class ComponentSearchBar extends GuiComponent {

	public double bookmarkAnimX;

	private String input = "";

	private String select = "";

	private boolean focused = false;

	public ComponentSearchBar(GuiBook book, int id) {
		super(book.COMPONENT_BOOK.getSize().getXi(), 20 + 5 * id + BOOKMARK_LONG.getHeight() * id, BOOKMARK_LONG.getWidth(), BOOKMARK_LONG.getHeight());

		clipping.setClipToBounds(true);

		bookmarkAnimX = -BOOKMARK_LONG.getWidth() + 20;

		ComponentText text = new ComponentText(0, 0, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
		text.getText().setValue("");
		text.getTransform().setTranslateZ(100);
		add(text);

		BUS.hook(GuiComponentEvents.MouseClickEvent.class, mouseClickEvent -> {
			focused = mouseClickEvent.component.getMouseOver();
		});

		BUS.hook(GuiComponentEvents.KeyDownEvent.class, keyDownEvent -> {
			if (!focused) return;

			if (GuiScreen.isKeyComboCtrlA(keyDownEvent.getKeyCode())) {
				select = input;

			} else if (GuiScreen.isKeyComboCtrlX(keyDownEvent.getKeyCode())) {

				if (!select.isEmpty()) {
					input = input.replace(select, "");
					GuiScreen.setClipboardString(select);
					select = "";

					text.getText().setValue(input);
				}

			} else if (GuiScreen.isKeyComboCtrlC(keyDownEvent.getKeyCode())) {

				if (!select.isEmpty()) {
					GuiScreen.setClipboardString(select);
					select = "";

					text.getText().setValue(input);
				}

			} else if (GuiScreen.isKeyComboCtrlV(keyDownEvent.getKeyCode())) {
				if (select.isEmpty()) {
					input += GuiScreen.getClipboardString();

					text.getText().setValue(input);

				} else {
					input = input.replace(select, GuiScreen.getClipboardString());

					text.getText().setValue(input);
				}
			} else {
				select = "";

				switch (keyDownEvent.getKeyCode()) {

					case 14:
						if (!input.isEmpty()) {
							input = input.substring(0, input.length() - 1);
						}

						break;
					case 28:
					case 156:
						if (!input.isEmpty()) {
							// TODO: SEARCH HERE
							input = "";
						}
						break;
					default:
						if (input.length() < 100 && ChatAllowedCharacters.isAllowedCharacter(keyDownEvent.getKey())) {
							this.input += Character.toString(keyDownEvent.getKey());
						}
				}

				text.getText().setValue(input);
			}
		});

		BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {

			//BasicAnimation<ComponentSearchBar> mouseInAnim = new BasicAnimation<>(this, "bookmarkAnimX");
			//mouseInAnim.setDuration(20);
			//mouseInAnim.setEasing(Easing.easeOutQuint);
			//mouseInAnim.setTo(0);
			//add(mouseInAnim);

			bookmarkAnimX = 0;
		});

		BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {

			//BasicAnimation<ComponentSearchBar> mouseOutAnim = new BasicAnimation<>(this, "bookmarkAnimX");
			//mouseOutAnim.setDuration(20);
			//mouseOutAnim.setEasing(Easing.easeOutQuint);
			//mouseOutAnim.setTo(-BOOKMARK_LONG.getWidth() + 20);
			//add(mouseOutAnim);

			if (input.isEmpty())
				bookmarkAnimX = -BOOKMARK_LONG.getWidth() + 20;
		});

		BUS.hook(GuiComponentEvents.PostDrawEvent.class, event -> {

			GlStateManager.pushMatrix();

			GlStateManager.color(1, 1, 1, 1);

			BOOKMARK_LONG.bind();
			BOOKMARK_LONG.draw((int) event.getPartialTicks(), (float) bookmarkAnimX, 0);

			GlStateManager.popMatrix();

		});
	}
}
