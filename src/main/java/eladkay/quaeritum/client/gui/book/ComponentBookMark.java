package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static eladkay.quaeritum.client.gui.book.GuiBook.BOOKMARK;
import static eladkay.quaeritum.client.gui.book.GuiBook.SEARCH_BOX;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentBookMark extends GuiComponent {

	private static Set<ComponentBookMark> bookMarks = new HashSet<>();

	private final GuiBook book;
	private final int id;
	private final boolean largeBox;

	public double bookmarkAnimX;

	public ComponentBookMark(GuiBook book, int id, boolean largeBox) {
		super(book.COMPONENT_BOOK.getSize().getXi() - 10, 20 + 5 * id + (largeBox ? SEARCH_BOX.getHeight() : BOOKMARK.getHeight()) * id, (largeBox ? SEARCH_BOX.getWidth() : BOOKMARK.getWidth()), (largeBox ? SEARCH_BOX.getHeight() : BOOKMARK.getHeight()));
		this.book = book;
		this.id = id;
		this.largeBox = largeBox;

		bookMarks.add(this);

		final Sprite box = (largeBox ? SEARCH_BOX : BOOKMARK);

		clipping.setClipToBounds(true);

		bookmarkAnimX = -box.getWidth() + 20;

		BUS.hook(GuiComponentEvents.PostDrawEvent.class, event -> {

			// RENDER THE TEXT BOX ITSELF
			{
				GlStateManager.pushMatrix();
				GlStateManager.color(1, 1, 1, 1);

				box.bind();
				box.draw((int) event.getPartialTicks(), (float) bookmarkAnimX, 0);

				GlStateManager.popMatrix();
			}
		});
	}

	@Nullable
	private static ComponentBookMark getBookMarkFromID(int id) {
		for (ComponentBookMark bookMark : bookMarks) {
			if (bookMark.getId() == id) return bookMark;
		}

		return null;
	}

	public static int getNextId() {
		int largest = 0;
		for (ComponentBookMark bookMark : bookMarks) {
			if (bookMark.getId() > largest) largest = bookMark.getId();
		}

		return ++largest;
	}

	public void slideOut() {
		bookmarkAnimX = 0;
	}

	public void slideIn() {
		bookmarkAnimX = -BOOKMARK.getWidth() + 20;
	}

	public boolean isLargeBox() {
		return largeBox;
	}

	public int getId() {
		return id;
	}

	public GuiBook getBook() {
		return book;
	}
}
