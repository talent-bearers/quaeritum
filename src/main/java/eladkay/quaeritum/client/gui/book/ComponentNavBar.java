package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

import static eladkay.quaeritum.client.gui.book.GuiBook.*;

public class ComponentNavBar extends GuiComponent {

	private int page = 0;

	public ComponentNavBar(int posX, int posY, int width, int maxPages) {
		super(posX, posY, width, 20);

		ComponentSprite back = new ComponentSprite(ARROW_BACK, 0, (int) ((getSize().getY() / 2.0) - (ARROW_NEXT.getHeight() / 2.0)));
		ComponentSprite home = new ComponentSprite(ARROW_HOME, (int) ((getSize().getX() / 2.0) - (ARROW_HOME.getWidth() / 2.0)), (int) ((getSize().getY() / 2.0) - (ARROW_NEXT.getHeight() / 2.0)));
		ComponentSprite next = new ComponentSprite(ARROW_NEXT, (int) (getSize().getX() - ARROW_NEXT.getWidth()), (int) ((getSize().getY() / 2.0) - (ARROW_BACK.getHeight() / 2.0)));
		add(back, next, home);

		if (maxPages > 1) {
			ComponentText pageStringComponent = new ComponentText(0, 0, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.MIDDLE);
			pageStringComponent.getUnicode().setValue(false);

			pageStringComponent.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
				String pageString = (page + 1) + "/" + (maxPages + 1);
				pageStringComponent.getText().setValue(pageString);
				pageStringComponent.setPos(new Vec2d((getSize().getX() / 2.0) - (Minecraft.getMinecraft().fontRenderer.getStringWidth(pageString) / 2.0), (int) ((getSize().getY() / 2.0) - (ARROW_NEXT.getHeight() / 2.0)) + 15));
			});
			add(pageStringComponent);
		}

		home.BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {
			home.setSprite(ARROW_HOME_PRESSED);
		});
		home.BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {
			home.setSprite(ARROW_HOME);
		});
		List<String> homeTooltip = new ArrayList<>();
		homeTooltip.add("Index");
		home.render.getTooltip().setValue(homeTooltip);

		home.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
			if (!event.component.getMouseOver()) return;

			if (getParent() != null)
				getParent().setVisible(false);

			GuiBook.MAIN_INDEX.setVisible(true);

			EventNavBarChange eventNavBarChange = new EventNavBarChange(page);
			BUS.fire(eventNavBarChange);
		});

		back.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
			int x = MathHelper.clamp(page - 1, 0, maxPages);
			if (page == x) back.setVisible(false);
			else back.setVisible(true);

			if (!back.isVisible()) return;

			if (event.component.getMouseOver()) {
				back.setSprite(ARROW_BACK_PRESSED);
			} else {
				back.setSprite(ARROW_BACK);
			}
		});
		back.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
			if (!event.component.getMouseOver()) return;

			int x = MathHelper.clamp(page - 1, 0, maxPages);
			if (page == x) return;

			page = x;

			EventNavBarChange eventNavBarChange = new EventNavBarChange(page);
			BUS.fire(eventNavBarChange);
		});
		List<String> backTooltip = new ArrayList<>();
		backTooltip.add("Back");
		back.render.getTooltip().setValue(backTooltip);

		next.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
			int x = MathHelper.clamp(page + 1, 0, maxPages);
			if (page == x) next.setVisible(false);
			else next.setVisible(true);

			if (!next.isVisible()) return;

			if (event.component.getMouseOver()) {
				next.setSprite(ARROW_NEXT_PRESSED);
			} else {
				next.setSprite(ARROW_NEXT);
			}
		});
		next.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
			if (!event.component.getMouseOver()) return;

			int x = MathHelper.clamp(page + 1, 0, maxPages);
			if (page == x) return;

			page = x;

			EventNavBarChange eventNavBarChange = new EventNavBarChange(page);
			BUS.fire(eventNavBarChange);
		});
		List<String> nextTooltip = new ArrayList<>();
		nextTooltip.add("Next");
		next.render.getTooltip().setValue(nextTooltip);
	}


	public int getPage() {
		return page;
	}

	@Override
	public void drawComponent(Vec2d mousePos, float partialTicks) {

	}
}
