package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static eladkay.quaeritum.client.gui.book.GuiBook.BOOKMARK;
import static eladkay.quaeritum.client.gui.book.GuiBook.MAGNIFIER;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentSearchBar extends GuiComponent {

	public double bookmarkAnimX;

	private int cursor = 0;
	private int selectionCursor = -1;
	private int cursorRenderFlashingCooldown = 0;
	private int cursorTextOffset = 0;

	private String input = "";
	private String select = "";

	private boolean focused = false;

	private ComponentSprite magnifier;
	private ComponentText text;

	public ComponentSearchBar(GuiBook book, int id) {
		super(book.COMPONENT_BOOK.getSize().getXi() - 10, 20 + 5 * id + BOOKMARK.getHeight() * id, BOOKMARK.getWidth(), BOOKMARK.getHeight());

		clipping.setClipToBounds(true);

		bookmarkAnimX = -BOOKMARK.getWidth() + 20;

		magnifier = new ComponentSprite(MAGNIFIER, 0, 0);
		add(magnifier);

		text = new ComponentText(2, 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
		text.getText().setValue("");
		text.getTransform().setTranslateZ(100);
		add(text);

		BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
			cursorRenderFlashingCooldown = cursorRenderFlashingCooldown > 0 ? --cursorRenderFlashingCooldown : 0;
		});

		BUS.hook(GuiComponentEvents.MouseClickEvent.class, mouseClickEvent -> {
			focused = !focused;
			updateState();
		});

		// HANDLE ALL TEXT MANIPULATION HERE
		BUS.hook(GuiComponentEvents.KeyDownEvent.class, keyDownEvent -> {
			if (!focused) return;

			int cursorCooldown = 50;

			if (GuiScreen.isShiftKeyDown() && (keyDownEvent.getKeyCode() == 205 || keyDownEvent.getKeyCode() == 203)) {

				// RIGHT ARROW 205
				// LEFT ARROW 203
				if (keyDownEvent.getKeyCode() == 205) {
					selectionCursor = MathHelper.clamp(selectionCursor + 1, 0, input.length());

					select = input.substring(Math.min(cursor, selectionCursor), Math.max(cursor, selectionCursor));

					cursorRenderFlashingCooldown = cursorCooldown;
				} else if (keyDownEvent.getKeyCode() == 203) {
					//cursor = MathHelper.clamp(cursor - 1, 0, input.length() - 1);
					selectionCursor = MathHelper.clamp(selectionCursor - 1, 0, input.length());

					select = input.substring(Math.min(cursor, selectionCursor), Math.max(cursor, selectionCursor));

					cursorRenderFlashingCooldown = cursorCooldown;
				}
				Minecraft.getMinecraft().player.sendChatMessage("cursor: " + cursor + " | tempCursor: " + selectionCursor);

			} else if (keyDownEvent.getKeyCode() == 205) {
				if (cursor != selectionCursor) {
					cursor = selectionCursor;
				} else cursor = selectionCursor = MathHelper.clamp(cursor + 1, 0, input.length());

				//if (cursor < input.length()) {
				//	FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
//
				//	int boxWidth = getSize().getXi() - 2;
				//	int backIndent = Math.abs(text.getPos().getXi());
				//	int cursorPosition = input.isEmpty() ? 0 : fontRenderer.getStringWidth(input.substring(0, cursor)) - 1 + text.getPos().getXi();
				//	if (cursorPosition >= boxWidth + backIndent) {
				//		Minecraft.getMinecraft().player.sendChatMessage("cursor: " + cursorPosition + " - whole box with indent: " + (boxWidth + backIndent));
				//		int excessTextWidth = fontRenderer.getStringWidth(input.substring(cursor, cursor + 1));
				//		text.setPos(new Vec2d(text.getPos().getX() - excessTextWidth, text.getPos().getY()));
				//	}
//
				//}
				cursorRenderFlashingCooldown = cursorCooldown;

			} else if (keyDownEvent.getKeyCode() == 203) {
				if (cursor != selectionCursor) {
					cursor = selectionCursor;
				} else cursor = selectionCursor = MathHelper.clamp(cursor - 1, 0, input.length());

				cursorRenderFlashingCooldown = cursorCooldown;

			} else if (GuiScreen.isKeyComboCtrlA(keyDownEvent.getKeyCode())) {
				select = input;

				cursor = input.length();
				selectionCursor = 0;
				cursorRenderFlashingCooldown = cursorCooldown;

			} else if (GuiScreen.isKeyComboCtrlX(keyDownEvent.getKeyCode())) {

				if (!select.isEmpty()) {
					cursor = selectionCursor = MathHelper.clamp(input.indexOf(select), 0, input.length());
					input = input.replace(select, "");
					GuiScreen.setClipboardString(select);

					select = "";

					text.getText().setValue(input);
					cursorRenderFlashingCooldown = cursorCooldown;
				}

			} else if (GuiScreen.isKeyComboCtrlC(keyDownEvent.getKeyCode())) {

				if (!select.isEmpty()) {
					GuiScreen.setClipboardString(select);
					select = "";
					text.getText().setValue(input);
					cursorRenderFlashingCooldown = cursorCooldown;
				}

			} else if (GuiScreen.isKeyComboCtrlV(keyDownEvent.getKeyCode())) {
				if (select.isEmpty()) {
					String clipboard = GuiScreen.getClipboardString();
					input += clipboard;

					cursor = selectionCursor = MathHelper.clamp(cursor + clipboard.length(), 0, input.length());

					text.getText().setValue(input);

					cursorRenderFlashingCooldown = cursorCooldown;
				} else {
					String clipboard = GuiScreen.getClipboardString();
					input = input.replace(select, clipboard);

					cursor = selectionCursor = MathHelper.clamp(input.indexOf(select + select.length()), 0, input.length());

					select = "";

					text.getText().setValue(input);
					cursorRenderFlashingCooldown = cursorCooldown;
				}

			} else {
				switch (keyDownEvent.getKeyCode()) {

					// BACKSPACE
					case 14:
						if (!select.isEmpty()) {
							StringBuilder builder = new StringBuilder(input);
							builder.replace(Math.min(cursor, selectionCursor), Math.max(cursor, selectionCursor), "");
							input = builder.toString();
							select = "";

							cursor = selectionCursor = MathHelper.clamp(selectionCursor + 1, 0, input.length());
						} else if (!input.isEmpty()) {
							StringBuilder builder = new StringBuilder(input);
							builder.deleteCharAt(cursor - 1);
							input = builder.toString();
						}

						cursor = selectionCursor = MathHelper.clamp(cursor - 1, 0, input.length());
						cursorRenderFlashingCooldown = cursorCooldown;

						text.getText().setValue(input);
						break;

					// ENTER
					case 28:
					case 156:
						if (!input.isEmpty()) {
							// TODO: SEARCH HERE
							input = "";

							cursor = selectionCursor = 0;
							cursorRenderFlashingCooldown = cursorCooldown;

							text.getText().setValue(input);
						}
						break;
					default:
						if (!select.isEmpty()) {

							StringBuilder builder = new StringBuilder(input);
							builder.replace(Math.min(cursor, selectionCursor), Math.max(cursor, selectionCursor), Character.toString(keyDownEvent.getKey()));
							input = builder.toString();
							select = "";

							cursor = selectionCursor = MathHelper.clamp(selectionCursor + 1, 0, input.length());

							text.getText().setValue(input);
						} else if (input.length() < 20 && ChatAllowedCharacters.isAllowedCharacter(keyDownEvent.getKey())) {
							select = "";
							this.input += Character.toString(keyDownEvent.getKey());

							cursor = selectionCursor = MathHelper.clamp(cursor + 1, 0, input.length());
							cursorRenderFlashingCooldown = cursorCooldown;

							text.getText().setValue(input);
						}
				}
			}
		});

		BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {

			//BasicAnimation<ComponentSearchBar> mouseInAnim = new BasicAnimation<>(this, "bookmarkAnimX");
			//mouseInAnim.setDuration(20);
			//mouseInAnim.setEasing(Easing.easeOutQuint);
			//mouseInAnim.setTo(0);
			//add(mouseInAnim);

			bookmarkAnimX = cursorTextOffset;
			text.setVisible(true);
			magnifier.setVisible(false);
		});

		BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {

			//BasicAnimation<ComponentSearchBar> mouseOutAnim = new BasicAnimation<>(this, "bookmarkAnimX");
			//mouseOutAnim.setDuration(20);
			//mouseOutAnim.setEasing(Easing.easeOutQuint);
			//mouseOutAnim.setTo(-BOOKMARK.getWidth() + 20);
			//add(mouseOutAnim);

			if (!focused) {
				bookmarkAnimX = -BOOKMARK.getWidth() + 20;
				text.setVisible(false);
				magnifier.setVisible(true);
			}
		});

		BUS.hook(GuiComponentEvents.PostDrawEvent.class, event -> {

			// RENDER THE TEXT BOX ITSELF
			{
				GlStateManager.pushMatrix();
				GlStateManager.color(1, 1, 1, 1);

				BOOKMARK.bind();
				BOOKMARK.draw((int) event.getPartialTicks(), (float) bookmarkAnimX, 0);

				GlStateManager.popMatrix();
			}

			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

			// RENDER CURSOR
			{
				if (focused && (cursorRenderFlashingCooldown != 0 || Math.sin(System.currentTimeMillis() / 200.0) > 0)) {
					GlStateManager.pushMatrix();
					GlStateManager.color(1, 1, 1, 1);
					GlStateManager.enableBlend();
					GlStateManager.disableLighting();
					GlStateManager.disableCull();

					int width = input.isEmpty() ? 0 : fontRenderer.getStringWidth(input.substring(0, cursor)) - 1 + text.getPos().getXi();
					int cursorWidth = 1;
					Color color = Color.WHITE;

					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buffer = tessellator.getBuffer();

					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
					buffer.pos(width + cursorWidth, 1, 110).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
					buffer.pos(width, 1, 110).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
					buffer.pos(width, BOOKMARK.getHeight() - 1, 110).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
					buffer.pos(width + cursorWidth, BOOKMARK.getHeight() - 1, 110).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();

					tessellator.draw();

					GlStateManager.popMatrix();
				}
			}

			// RENDER SELECTION BOX
			{
				if (!select.isEmpty() && selectionCursor != cursor) {
					GlStateManager.pushMatrix();
					GlStateManager.color(1, 1, 1, 1);
					GlStateManager.enableBlend();
					GlStateManager.disableLighting();
					GlStateManager.disableCull();

					int indexStart = fontRenderer.getStringWidth(input.substring(0, Math.min(cursor, selectionCursor))) + text.getPos().getXi();
					int indexEnd = fontRenderer.getStringWidth(input.substring(0, Math.max(cursor, selectionCursor))) + text.getPos().getXi();

					Color color = Color.CYAN;

					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buffer = tessellator.getBuffer();

					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
					buffer.pos(indexEnd, 1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
					buffer.pos(indexStart, 1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
					buffer.pos(indexStart, BOOKMARK.getHeight() - 1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
					buffer.pos(indexEnd, BOOKMARK.getHeight() - 1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();

					tessellator.draw();

					GlStateManager.popMatrix();
				}
			}
		});
	}

	public void updateState() {
		if (focused) {
			bookmarkAnimX = 0;
			text.setVisible(true);
			magnifier.setVisible(false);
		} else {
			bookmarkAnimX = -BOOKMARK.getWidth() + 20;
			text.setVisible(false);
			magnifier.setVisible(true);
		}
	}
}
