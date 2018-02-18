package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static eladkay.quaeritum.api.lib.LibMisc.MOD_ID;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class GuiBook extends GuiBase {

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
	public static Sprite ERROR = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/error.png"));
	public static Sprite FOF = new Sprite(new ResourceLocation(MOD_ID, "textures/gui/book/error/fof.png"));

	public final Book book;
	public final Color mainColor;
	public final Color highlightColor;

	public ComponentSprite bookComponent;
	public BookGuiComponent centralIndex;
	public BookGuiComponent focus;
	public HashMap<BookGuiComponent, String> contentCache = new HashMap<>();

	public GuiBook(Book book) {
		super(146, 180);
		this.book = book;
		this.mainColor = book.bookColor;
		this.highlightColor = book.highlightColor;

		bookComponent = new ComponentSprite(BOOK, 0, 0);
		bookComponent.getColor().setValue(mainColor.darker());

		ComponentSprite bookFilling = new ComponentSprite(BOOK_FILLING, 0, 0);
		bookComponent.add(bookFilling);

		//bookComponent.BUS.hook(GuiComponentEvents.PostDrawEvent.class, event -> {
		//	GlStateManager.pushMatrix();
		//	GlStateManager.color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue());
//
		//	BOOK.bind();
		//	BOOK.draw(0, 0, 0);
//
		//	GlStateManager.popMatrix();
		//});

		getMainComponents().add(bookComponent);

		focus = centralIndex = new ComponentMainIndex(0, 0, bookComponent.getSize().getXi(), bookComponent.getSize().getYi(), this, null);
		bookComponent.add(centralIndex);
	}

	public Consumer<String> searchImplementation(ComponentSearchResults searchResultsComponent) {
		return type -> {

			if (focus != searchResultsComponent)
				searchResultsComponent.setLinkingParent(focus);

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
