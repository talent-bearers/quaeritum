package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;
import static eladkay.quaeritum.client.gui.book.BookGuiComponent.getRendererFor;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class GuiBook extends GuiBase {

	public static int bookmarkID = 0;
	public static Sprite ERROR = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/error.png"));
	public static Sprite FOF = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/fof.png"));
	private static Texture GUIDE_BOOK_SHEET = new Texture(new ResourceLocation(MOD_ID, "textures/gui/book/guide_book.png"));
	public static Sprite BOOK = GUIDE_BOOK_SHEET.getSprite("book", 146, 180);
	public static Sprite BOOK_FILLING = GUIDE_BOOK_SHEET.getSprite("book_filling", 146, 180);
	public static Sprite ARROW_NEXT = GUIDE_BOOK_SHEET.getSprite("arrow_next", 18, 10);
	public static Sprite ARROW_NEXT_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_next_pressed", 18, 10);
	public static Sprite ARROW_BACK = GUIDE_BOOK_SHEET.getSprite("arrow_back", 18, 10);
	public static Sprite ARROW_BACK_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_back_pressed", 18, 10);
	public static Sprite ARROW_HOME = GUIDE_BOOK_SHEET.getSprite("arrow_home", 18, 9);
	public static Sprite ARROW_HOME_PRESSED = GUIDE_BOOK_SHEET.getSprite("arrow_home_pressed", 18, 9);
	public static Sprite BANNER = GUIDE_BOOK_SHEET.getSprite("banner", 140, 31);
	public static Sprite BOOKMARK = GUIDE_BOOK_SHEET.getSprite("bookmark", 133, 13);
	public static Sprite MAGNIFIER = GUIDE_BOOK_SHEET.getSprite("magnifier", 12, 12);
	public static Sprite TITLE_BAR = GUIDE_BOOK_SHEET.getSprite("title_bar", 86, 11);
	public final Book book;
	public final Color mainColor;
	public final Color highlightColor;

	public ComponentSprite bookComponent;
	public GuiComponent centralIndex;
	public GuiComponent focus;
	public HashMap<BookGuiComponent, String> contentCache = new HashMap<>();

	public List<IBookElement> history = new ArrayList<>();

	public GuiBook(Book book) {
		super(146, 180);
		this.book = book;
		this.mainColor = book.bookColor;
		this.highlightColor = book.highlightColor;

		bookComponent = new ComponentSprite(BOOK, 0, 0);
		bookComponent.getColor().setValue(mainColor.darker());

		ComponentSprite bookFilling = new ComponentSprite(BOOK_FILLING, 0, 0);
		bookComponent.add(bookFilling);

		getMainComponents().add(bookComponent);

		// --------- SEARCH BAR --------- //
		{
			ComponentSearchResults searchResultsComponent = new ComponentSearchResults(this);
			searchResultsComponent.setVisible(false);
			bookComponent.add(searchResultsComponent);

			ComponentSearchBar bar = new ComponentSearchBar(this, bookmarkID++, searchImplementation(searchResultsComponent), null);
			bookComponent.add(bar);
		}
		// --------- SEARCH BAR --------- //

		focus = centralIndex = new ComponentMainIndex(0, 0, bookComponent.getSize().getXi(), bookComponent.getSize().getYi(), this);
		bookComponent.add(centralIndex);
	}

	public Consumer<String> searchImplementation(ComponentSearchResults searchResultsComponent) {
		return type -> {

			String query = type.replace("'", "").toLowerCase(Locale.ROOT);
			String[] keywords = query.split(" ");

			ArrayList<TfidfSearchResult> unfilteredTfidfResults = new ArrayList<>();
			ArrayList<MatchCountSearchResult> matchCountSearchResults = new ArrayList<>();

			final int nbOfDocuments = contentCache.size();
			for (BookGuiComponent cachedComponent : contentCache.keySet()) {
				String cachedDocument = contentCache
						.get(cachedComponent)
						.toLowerCase(Locale.ROOT)
						.replace("'", "");

				List<String> words = Arrays.asList(cachedDocument.split("\\s+"));
				long mostRepeatedWord =
						words.stream()
								.collect(Collectors.groupingBy(w -> w, Collectors.counting()))
								.entrySet()
								.stream()
								.max(Comparator.comparing(Map.Entry::getValue))
								.get().getValue();

				double documentTfidf = 0;
				for (String keyword : keywords) {
					long keywordOccurance = Pattern.compile("\\b" + keyword).splitAsStream(cachedDocument).count() - 1;
					double termFrequency = 0.5 + (0.5 * keywordOccurance / mostRepeatedWord);

					int keywordDocumentOccurance = 0;
					for (BookGuiComponent documentComponent : contentCache.keySet()) {
						String documentContent = contentCache.get(documentComponent).toLowerCase(Locale.ROOT);
						if (documentContent.contains(keyword)) {
							keywordDocumentOccurance++;
						}
					}
					keywordDocumentOccurance = keywordDocumentOccurance == 0 ? keywordDocumentOccurance + 1 : keywordDocumentOccurance;

					double inverseDocumentFrequency = Math.log(nbOfDocuments / (keywordDocumentOccurance));

					double keywordTfidf = termFrequency * inverseDocumentFrequency;

					documentTfidf += keywordTfidf;
				}

				unfilteredTfidfResults.add(new TfidfSearchResult(cachedComponent, documentTfidf));
			}

			ArrayList<TfidfSearchResult> filteredTfidfResults = new ArrayList<>();

			double largestTFIDF = 0, smallestTFIDF = Integer.MAX_VALUE;
			for (TfidfSearchResult resultItem2 : unfilteredTfidfResults) {
				largestTFIDF = resultItem2.getTfidfrequency() > largestTFIDF ? resultItem2.getTfidfrequency() : largestTFIDF;
				smallestTFIDF = resultItem2.getTfidfrequency() < smallestTFIDF ? resultItem2.getTfidfrequency() : smallestTFIDF;
			}

			for (TfidfSearchResult resultItem : unfilteredTfidfResults) {
				double matchPercentage = Math.round((resultItem.getTfidfrequency() - smallestTFIDF) / (largestTFIDF - smallestTFIDF) * 100);
				if (matchPercentage < 5 || Double.isNaN(matchPercentage)) continue;

				filteredTfidfResults.add(resultItem);
			}

			if (!filteredTfidfResults.isEmpty()) {
				searchResultsComponent.updateTfidfSearches(filteredTfidfResults);
			} else {
				for (BookGuiComponent cachedComponent : contentCache.keySet()) {
					String cachedDocument = contentCache
							.get(cachedComponent)
							.toLowerCase(Locale.ROOT)
							.replace("'", "");

					int mostMatches = 0;
					for (String keyword : keywords) {
						int keywordOccurances = StringUtils.countMatches(cachedDocument, keyword);
						mostMatches += keywordOccurances;
					}

					if (mostMatches > 0)
						matchCountSearchResults.add(new MatchCountSearchResult(cachedComponent, mostMatches));
				}

				if (!matchCountSearchResults.isEmpty()) {
					searchResultsComponent.updateMatchCountSearches(matchCountSearchResults);
				} else {
					searchResultsComponent.setAsBadSearch();
				}
			}

			focus.setVisible(false);
			focus = searchResultsComponent;
			focus.setVisible(true);
		};
	}

	public GuiComponent createIndexButton(int indexID, Entry entry, @Nullable Consumer<ComponentVoid> extra) {
		ComponentVoid indexButton = new ComponentVoid(0, 16 * indexID, bookComponent.getSize().getXi() - 32, 16);

		if (extra != null) extra.accept(indexButton);
		indexButton.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {

			history.add(entry);
			focus.invalidate();

			GuiComponent nextPageTo = focus = entry.createComponent(this, new Vec2d(bookComponent.getSize().getXi() - 32, bookComponent.getSize().getYi() - 32));
			bookComponent.add(nextPageTo);
		});

		// SUB INDEX PLATE RENDERING
		{
			final String title = I18n.format(entry.titleKey);
			final JsonElement icon = entry.icon;

			ComponentText textComponent = new ComponentText(20, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
			textComponent.getUnicode().setValue(true);
			textComponent.getText().setValue(title);
			indexButton.add(textComponent);

			indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
				textComponent.getText().setValue(" " + TextFormatting.ITALIC.toString() + title);
			});

			indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
				textComponent.getText().setValue(TextFormatting.RESET.toString() + title);
			});

			Runnable render = getRendererFor(icon);

			if (render != null)
				indexButton.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
					render.run();
				});
		}

		return indexButton;
	}

	public static class TfidfSearchResult implements Comparable<TfidfSearchResult> {

		private final BookGuiComponent resultComponent;
		private final double tfidfrequency;

		public TfidfSearchResult(BookGuiComponent resultComponent, double tfidfrequency) {
			this.resultComponent = resultComponent;
			this.tfidfrequency = tfidfrequency;
		}

		public BookGuiComponent getResultComponent() {
			return resultComponent;
		}

		public double getTfidfrequency() {
			return tfidfrequency;
		}

		@Override
		public int compareTo(@NotNull GuiBook.TfidfSearchResult o) {
			return Double.compare(o.getTfidfrequency(), getTfidfrequency());
		}
	}

	public static class MatchCountSearchResult implements Comparable<MatchCountSearchResult> {

		private final BookGuiComponent resultComponent;
		private final int nbOfMatches;

		public MatchCountSearchResult(BookGuiComponent resultComponent, int nbOfMatches) {
			this.resultComponent = resultComponent;
			this.nbOfMatches = nbOfMatches;
		}

		public BookGuiComponent getResultComponent() {
			return resultComponent;
		}

		public int getMatchCount() {
			return nbOfMatches;
		}

		@Override
		public int compareTo(@NotNull GuiBook.MatchCountSearchResult o) {
			return Double.compare(o.getMatchCount(), getMatchCount());
		}
	}
}
