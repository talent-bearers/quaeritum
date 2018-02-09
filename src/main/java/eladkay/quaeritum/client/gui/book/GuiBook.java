package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.animator.Animator;
import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;

public class GuiBook extends GuiBase {

	static Texture GUIDE_BOOK_SHEET = new Texture(new ResourceLocation(MOD_ID, "textures/gui/book/guide_book.png"));
	static Sprite BOOK = GUIDE_BOOK_SHEET.getSprite("book", 146, 180);
	static Sprite ARROW_NEXT = GUIDE_BOOK_SHEET.getSprite("arrow_next", 18, 10);
	static Sprite ARROW_NEXT_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_next_pressed", 18, 10);
	static Sprite ARROW_BACK = GUIDE_BOOK_SHEET.getSprite("arrow_back", 18, 10);
	static Sprite ARROW_BACK_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_back_pressed", 18, 10);
	static Sprite ARROW_HOME = GUIDE_BOOK_SHEET.getSprite("arrow_home", 18, 9);
	static Sprite ARROW_HOME_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_home_pressed", 18, 9);
	static Sprite BANNER = GUIDE_BOOK_SHEET.getSprite("banner", 140, 31);
	static Sprite BOOKMARK_SHORT = GUIDE_BOOK_SHEET.getSprite("bookmark_short", 177, 22);
	static Sprite BOOKMARK_LONG = GUIDE_BOOK_SHEET.getSprite("bookmark_long", 226, 22);
	static Sprite ERROR = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/error.png"));
	static Sprite FOF = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/fof.png"));

	private static Animator animator = new Animator();

	static ComponentSprite COMPONENT_BOOK;
	static ComponentVoid MAIN_INDEX;

	static String langname;

	public GuiBook() {
		super(146, 180);

		COMPONENT_BOOK = new ComponentSprite(BOOK, 0, 0);
		getMainComponents().add(COMPONENT_BOOK);

		MAIN_INDEX = new ComponentVoid(0, 0, COMPONENT_BOOK.getSize().getXi(), COMPONENT_BOOK.getSize().getYi());
		COMPONENT_BOOK.add(MAIN_INDEX);

		// --------- BANNER --------- //
		{
			ComponentSprite componentBanner = new ComponentSprite(BANNER, -8, 12);
			MAIN_INDEX.add(componentBanner);

			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
			ComponentText componentBannerText = new ComponentText(20, 5, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
			componentBannerText.getText().setValue("Lexica Demoniaqa");
			componentBannerText.getColor().setValue(Color.CYAN);

			String subText = "- By Demoniaque";
			ComponentText componentBannerSubText = new ComponentText(componentBanner.getSize().getXi() - 10, 2 + fontRenderer.FONT_HEIGHT, ComponentText.TextAlignH.RIGHT, ComponentText.TextAlignV.TOP);
			componentBannerSubText.getText().setValue(subText);
			componentBannerSubText.getUnicode().setValue(true);
			componentBannerSubText.getColor().setValue(Color.CYAN);

			componentBanner.add(componentBannerText, componentBannerSubText);
		}
		// --------- BANNER --------- //

		// --------- MAIN INDEX --------- //
		{
			langname = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().toLowerCase();

			ArrayList<GuiComponent> indexComponents = new ArrayList<>();

			JsonElement json = getJsonFromLink("documentation/%LANG%/index.json");
			if (json != null) {
				if (json.isJsonObject() && json.getAsJsonObject().has("index")) {

					JsonArray array = json.getAsJsonObject().getAsJsonArray("index");
					for (JsonElement element : array) {
						if (!element.isJsonObject()) continue;

						JsonObject chunk = element.getAsJsonObject();
						if (chunk.has("text") && chunk.has("link")
								&& chunk.get("text").isJsonPrimitive() && chunk.get("link").isJsonPrimitive()
								&& chunk.get("icon").isJsonPrimitive() && chunk.get("icon").isJsonPrimitive()) {

							Sprite icon = new Sprite(new ResourceLocation(chunk.getAsJsonPrimitive("icon").getAsString()));

							String link = chunk.getAsJsonPrimitive("link").getAsString();
							String text = chunk.getAsJsonPrimitive("text").getAsString();

							ComponentVoid button = new ComponentVoid(0, 0, 24, 24);

							button.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (GuiComponentEvents.PostDrawEvent event) -> {
								GlStateManager.pushMatrix();
								GlStateManager.enableAlpha();
								GlStateManager.enableBlend();
								GlStateManager.disableCull();

								GlStateManager.color(0, 0, 0);
								icon.getTex().bind();
								icon.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, button.getSize().getXi(), button.getSize().getYi());

								GlStateManager.enableCull();
								GlStateManager.popMatrix();
							});

							button.BUS.hook(GuiComponentEvents.MouseClickEvent.class, (event) -> {
								JsonElement jsonElement = getJsonFromLink(link);
								if (jsonElement == null || !jsonElement.isJsonObject()) return;

								JsonObject object = jsonElement.getAsJsonObject();
								if (object.has("type") && object.get("type").isJsonPrimitive()
										&& object.has("title") && object.get("title").isJsonPrimitive()
										&& object.has("content") && object.get("content").isJsonArray()) {

									String type = object.getAsJsonPrimitive("type").getAsString();
									if (type.equals("index")) {

										ComponentSubIndex index = new ComponentSubIndex(link, object.getAsJsonArray("content"));

										COMPONENT_BOOK.add(index);
										MAIN_INDEX.setVisible(false);

									} else if (type.equals("content")) {

									}
								}
							});

							button.render.getTooltip().func((Function<GuiComponent, java.util.List<String>>) guiComponent -> {
								List<String> list = new ArrayList<>();
								list.add(text);
								return list;
							});

							ComponentAnimatableVoid circleWipe = new ComponentAnimatableVoid(0, 0, 24, 24);
							button.add(circleWipe);
							circleWipe.getTransform().setTranslateZ(100);

							circleWipe.clipping.setClipToBounds(true);
							circleWipe.clipping.setCustomClipping(() -> {

								GlStateManager.disableTexture2D();
								GlStateManager.disableCull();
								Tessellator tessellator = Tessellator.getInstance();
								BufferBuilder buffer = tessellator.getBuffer();
								buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
								for (int i = 0; i <= 10; i++) {
									float angle = (float) (i * Math.PI * 2 / 10);
									float x1 = (float) (12 + MathHelper.cos(angle) * circleWipe.x);
									float y1 = (float) (12 + MathHelper.sin(angle) * circleWipe.x);
									buffer.pos(x1, y1, 100).color(0f, 1f, 1f, 1f).endVertex();
								}
								tessellator.draw();

								return Unit.INSTANCE;
							});

							final double radius = 16;

							circleWipe.BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {

								BasicAnimation mouseInAnim = new BasicAnimation<>(circleWipe, "x");
								mouseInAnim.setDuration(10);
								mouseInAnim.setEasing(Easing.easeOutQuint);
								mouseInAnim.setTo(radius);
								animator.add(mouseInAnim);
							});

							circleWipe.BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {

								BasicAnimation mouseOutAnim = new BasicAnimation<>(circleWipe, "x");
								mouseOutAnim.setDuration(10);
								mouseOutAnim.setEasing(Easing.easeOutQuint);
								mouseOutAnim.setTo(0);
								animator.add(mouseOutAnim);
							});

							circleWipe.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
								if (circleWipe.x != 0 && !event.component.getMouseOver()) {

								}
							});

							circleWipe.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (GuiComponentEvents.PostDrawEvent event) -> {
								GlStateManager.pushMatrix();
								GlStateManager.color(1, 1, 1, 1);
								GlStateManager.enableAlpha();
								GlStateManager.enableBlend();
								GlStateManager.disableCull();

								GL11.glEnable(GL_POLYGON_SMOOTH);

								GlStateManager.color(0, 1, 1);
								icon.getTex().bind();
								icon.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, circleWipe.getSize().getXi(), circleWipe.getSize().getYi());

								GL11.glDisable(GL_POLYGON_SMOOTH);

								GlStateManager.enableCull();
								GlStateManager.popMatrix();
							});
							indexComponents.add(button);
						}
					}
				}
			}

			int row = 0;
			int column = 0;
			int buffer = 8;
			int marginX = 28;
			int marginY = 45;
			int itemsPerRow = 3;
			for (GuiComponent button : indexComponents) {

				button.setPos(new Vec2d(
						marginX + (column * button.getSize().getXi()) + (column * buffer),
						marginY + (row * button.getSize().getY()) + (row * buffer)));

				MAIN_INDEX.add(button);

				column++;

				if (column >= itemsPerRow) {
					row++;
					column = 0;
				}
			}
		}
		// --------- MAIN INDEX --------- //
	}

	@Nullable
	static JsonElement getJsonFromLink(String link) {
		String updatedString = link;
		if (link.contains("%LANG%")) updatedString = link.replace("%LANG%", langname);

		InputStream stream = LibrarianLib.PROXY.getResource(MOD_ID, updatedString);

		if (stream == null && !updatedString.equals(link)) stream = LibrarianLib.PROXY.getResource(MOD_ID, link);
		if (stream == null) return null;

		InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
		return new JsonParser().parse(reader);
	}

	public static class IndexItem {

		public final Sprite icon;
		public final String text;
		public final String link;

		public IndexItem(String text, String link, @Nullable Sprite icon) {
			this.text = text;
			this.icon = icon;
			this.link = link;
		}
	}
}
