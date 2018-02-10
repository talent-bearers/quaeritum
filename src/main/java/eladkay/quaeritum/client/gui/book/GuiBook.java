package eladkay.quaeritum.client.gui.book;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
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
	static Sprite BOOKMARK = GUIDE_BOOK_SHEET.getSprite("bookmark", 133, 13);
	static Sprite MAGNIFIER = GUIDE_BOOK_SHEET.getSprite("magnifier", 12, 12);
	static Sprite SEARCH_BOX = GUIDE_BOOK_SHEET.getSprite("search_box", 133, 114);
	static Sprite ERROR = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/error.png"));
	static Sprite FOF = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/fof.png"));

	public ComponentSprite COMPONENT_BOOK;
	public ComponentVoid MAIN_INDEX;
	public GuiComponent FOCUSED_COMPONENT;

	static String langname;

	public HashMultimap<GuiComponent, String> contentCache = HashMultimap.create();
	public HashBiMap<GuiComponent, GuiComponent> pageLinks = HashBiMap.create();

	public GuiBook() {
		super(146, 180);

		COMPONENT_BOOK = new ComponentSprite(BOOK, 0, 0);
		getMainComponents().add(COMPONENT_BOOK);

		FOCUSED_COMPONENT = MAIN_INDEX = new ComponentVoid(0, 0, COMPONENT_BOOK.getSize().getXi(), COMPONENT_BOOK.getSize().getYi());
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

		// --------- SEARCH BAR --------- //
		{
			ComponentSearchBar bar = new ComponentSearchBar(this, 0, search -> {

				String query = search.replace("'", "").toLowerCase();
				String[] keywords = query.split(" ");

				Set<SearchResultItem> results = new HashSet<>();

				for (GuiComponent cachedComponent : contentCache.keySet()) {
					if (cachedComponent instanceof BookGuiComponent) {
						String title = ((BookGuiComponent) cachedComponent).getTitle().toLowerCase();
						String description = ((BookGuiComponent) cachedComponent).getDescription().toLowerCase();

						keywordLoop:
						for (String keyword : keywords) {

							// ----- SEARCH TITLES ----- //
							{
								if (keyword.contains(title) || title.contains(keyword)) {
									for (SearchResultItem resultItem : results) {
										if (resultItem.getResultComponent() == cachedComponent) {
											resultItem.addKeywordMatched(keyword);
											break keywordLoop;
										}
									}

									SearchResultItem resultItem = new SearchResultItem(cachedComponent);
									resultItem.addKeywordMatched(keyword);
									results.add(resultItem);
								}
							}
							// ----- SEARCH TITLES ----- //

							// ----- SEARCH DESCRIPTIONS ----- //
							{
								if (keyword.contains(description) || description.contains(keyword)) {
									for (SearchResultItem resultItem : results) {
										if (resultItem.getResultComponent() == cachedComponent) {
											resultItem.addKeywordMatched(keyword);
											break keywordLoop;
										}
									}

									SearchResultItem resultItem = new SearchResultItem(cachedComponent);
									resultItem.addKeywordMatched(keyword);
									results.add(resultItem);
								}
							}
							// ----- SEARCH DESCRIPTIONS ----- //
						}
					}

					// ----- SEARCH CONTENT TEXT ----- //
					Set<String> cachedStrings = contentCache.get(cachedComponent);
					for (String cachedString : cachedStrings) {
						if (cachedString == null) continue;

						keywordLoop:
						for (String keyword : keywords) {
							if (cachedString.contains(keyword) || keyword.contains(cachedString)) {

								for (SearchResultItem resultItem : results) {
									if (resultItem.getResultComponent() == cachedComponent) {
										resultItem.addKeywordMatched(keyword);
										break keywordLoop;
									}
								}

								SearchResultItem resultItem = new SearchResultItem(cachedComponent);
								resultItem.addKeywordMatched(keyword);
							}
						}
					}
					// ----- SEARCH CONTENT TEXT ----- //
				}

				if (!results.isEmpty()) {
					ComponentSearchResults resultsComponent = new ComponentSearchResults(this, FOCUSED_COMPONENT, results);

					COMPONENT_BOOK.add(resultsComponent);
					FOCUSED_COMPONENT.setVisible(false);
					FOCUSED_COMPONENT = resultsComponent;
				}
			});
			COMPONENT_BOOK.add(bar);
		}
		// --------- SEARCH BAR --------- //

		// --------- MAIN INDEX --------- //
		{
			langname = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().toLowerCase();

			ArrayList<GuiComponent> categories = new ArrayList<>();

			JsonElement json = getJsonFromLink("documentation/%LANG%/categories.json");
			if (json != null && json.isJsonArray()) {

				for (JsonElement element : json.getAsJsonArray()) {
					if (!element.isJsonPrimitive()) continue;

					JsonElement indexElement = getJsonFromLink(element.getAsJsonPrimitive().getAsString());
					if (indexElement == null || !indexElement.isJsonObject()) continue;

					JsonObject cateogryObject = indexElement.getAsJsonObject();

					ComponentCategory categoryComponent = new ComponentCategory(0, 0, 24, 24, this, cateogryObject);
					MAIN_INDEX.add(categoryComponent);
					categories.add(categoryComponent);
				}
			}

			int row = 0;
			int column = 0;
			int buffer = 8;
			int marginX = 28;
			int marginY = 45;
			int itemsPerRow = 3;
			for (GuiComponent button : categories) {

				button.setPos(new Vec2d(
						marginX + (column * button.getSize().getXi()) + (column * buffer),
						marginY + (row * button.getSize().getY()) + (row * buffer)));

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

	public static class SearchResultItem {

		private final GuiComponent resultComponent;
		private final Set<String> keywordsMatched = new HashSet<>();

		public SearchResultItem(GuiComponent resultComponent) {
			this.resultComponent = resultComponent;
		}

		public void addKeywordMatched(String keyword) {
			keywordsMatched.add(keyword);
		}

		public GuiComponent getResultComponent() {
			return resultComponent;
		}

		public Set<String> getKeywordsMatched() {
			return keywordsMatched;
		}

		public int getAmountOfMatches() {
			return keywordsMatched.size();
		}
	}
}
