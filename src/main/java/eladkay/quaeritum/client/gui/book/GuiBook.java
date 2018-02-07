package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;

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

	static ComponentSprite COMPONENT_BOOK;
	static ComponentVoid MAIN_INDEX;

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
			String langname = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().toLowerCase();
			String path = "documentation/" + langname;

			InputStream stream1 = LibrarianLib.PROXY.getResource(MOD_ID, path + "/index.json");
			if (stream1 == null) {
				stream1 = LibrarianLib.PROXY.getResource(MOD_ID, "documentation/en_us/index.json");
				path = "documentation/en_us";
			}

			ArrayList<ComponentVoid> indexComponents = new ArrayList<>();
			if (stream1 != null) {
				InputStreamReader reader = new InputStreamReader(stream1, Charset.forName("UTF-8"));
				JsonElement json = new JsonParser().parse(reader);
				if (json.isJsonObject() && json.getAsJsonObject().has("index")) {

					JsonArray array = json.getAsJsonObject().getAsJsonArray("index");
					for (JsonElement element : array) {
						if (!element.isJsonObject()) continue;

						JsonObject chunk = element.getAsJsonObject();
						if (chunk.has("text") && chunk.has("link")
								&& chunk.get("text").isJsonPrimitive() && chunk.get("link").isJsonPrimitive()
								&& chunk.get("icon").isJsonPrimitive() && chunk.get("icon").isJsonPrimitive()) {

							Sprite icon = new Sprite(new ResourceLocation(chunk.getAsJsonPrimitive("icon").getAsString()));

							String link = path + chunk.getAsJsonPrimitive("link").getAsString();
							String text = chunk.getAsJsonPrimitive("text").getAsString();

							ComponentVoid button = new ComponentVoid(0, 0, 24, 24);

							String finalPath = path;
							button.BUS.hook(GuiComponentEvents.MouseClickEvent.class, (event) -> {
								InputStream inputStream = LibrarianLib.PROXY.getResource(MOD_ID, link);
								if (inputStream == null)
									inputStream = LibrarianLib.PROXY.getResource(MOD_ID, link.replace(langname, "en_us"));
								if (inputStream == null) return;

								InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
								JsonElement jsonElement = new JsonParser().parse(inputStreamReader);
								if (!jsonElement.isJsonObject()) return;

								JsonObject object = jsonElement.getAsJsonObject();
								if (object.has("type") && object.get("type").isJsonPrimitive()
										&& object.has("title") && object.get("title").isJsonPrimitive()
										&& object.has("content") && object.get("content").isJsonArray()) {

									String type = object.getAsJsonPrimitive("type").getAsString();
									if (type.equals("index")) {

										ComponentSubIndex index = new ComponentSubIndex(finalPath, object.getAsJsonArray("content"));

										COMPONENT_BOOK.add(index);
										MAIN_INDEX.setVisible(false);

									} else if (type.equals("content")) {

									}
								}
							});

							button.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
								GlStateManager.pushMatrix();
								GlStateManager.color(1, 1, 1, 1);
								GlStateManager.enableAlpha();
								GlStateManager.enableBlend();

								if (!event.component.getMouseOver()) GlStateManager.color(0, 0, 0);
								else GlStateManager.color(1, 0f, 0.5f);

								icon.getTex().bind();
								icon.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, button.getSize().getXi(), button.getSize().getYi());

								GlStateManager.popMatrix();
							});

							button.render.getTooltip().func((Function<GuiComponent, List<String>>) guiComponent -> {
								List<String> list = new ArrayList<>();
								list.add(text);
								return list;
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
			for (ComponentVoid button : indexComponents) {

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
