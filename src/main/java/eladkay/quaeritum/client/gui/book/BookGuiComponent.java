package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public abstract class BookGuiComponent extends GuiComponent {

	public BookGuiComponent(int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
	}

	public BookGuiComponent(int posX, int posY, int width) {
		super(posX, posY, width);
	}

	public BookGuiComponent(int posX, int posY) {
		super(posX, posY);
	}

	public void makeInvisible() {
		setVisible(false);
	}

	public void makeVisible() {
		setVisible(true);
	}
}
