package eladkay.quaeritum.client.gui.book;

import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author WireSegal
 * Created at 9:05 AM on 2/24/18.
 */
@SideOnly(Side.CLIENT)
public class Search {

    private final GuiBook book;

    public Search(GuiBook book) {
        this.book = book;
    }

    public void search(String type) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        ComponentSearchResults toFocus = book.focus instanceof ComponentSearchResults ?
                (ComponentSearchResults) book.focus :
                new ComponentSearchResults(book);


        String query = type.replace("'", "").toLowerCase(Locale.ROOT);
        String[] keywords = query.split(" ");

        ArrayList<FrequencySearchResult> unfilteredTfidfResults = new ArrayList<>();
        ArrayList<MatchCountSearchResult> matchCountSearchResults = new ArrayList<>();

        final int nbOfDocuments = book.contentCache.size();
        for (Entry cachedComponent : book.contentCache.keySet()) if (cachedComponent.isUnlocked(player)) {
            String cachedDocument = book.contentCache
                    .get(cachedComponent)
                    .toLowerCase(Locale.ROOT)
                    .replace("'", "");

            List<String> words = Arrays.asList(cachedDocument.split("\\s+"));
            long mostRepeatedWord =
                    words.stream()
                            .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                            .entrySet()
                            .stream()
                            .map(Map.Entry::getValue)
                            .max(Double::compare)
                            .orElse(-1L);

            if (mostRepeatedWord != -1L) {
                double documentTfidf = 0;
                for (String keyword : keywords) {
                    long keywordOccurance = Pattern.compile("\\b" + keyword).splitAsStream(cachedDocument).count() - 1;
                    double termFrequency = 0.5 + (0.5 * keywordOccurance / mostRepeatedWord);

                    int keywordDocumentOccurance = 0;
                    for (Entry documentComponent : book.contentCache.keySet()) if (documentComponent.isUnlocked(player)) {
                        String documentContent = book.contentCache.get(documentComponent).toLowerCase(Locale.ROOT);
                        if (documentContent.contains(keyword)) {
                            keywordDocumentOccurance++;
                        }
                    }
                    keywordDocumentOccurance = keywordDocumentOccurance == 0 ? keywordDocumentOccurance + 1 : keywordDocumentOccurance;

                    double inverseDocumentFrequency = Math.log(nbOfDocuments / (keywordDocumentOccurance));

                    double keywordTfidf = termFrequency * inverseDocumentFrequency;

                    documentTfidf += keywordTfidf;
                }

                unfilteredTfidfResults.add(new FrequencySearchResult(cachedComponent, documentTfidf));
            }
        }

        ArrayList<FrequencySearchResult> filteredTfidfResults = new ArrayList<>();

        double largestTFIDF = 0, smallestTFIDF = Integer.MAX_VALUE;
        for (FrequencySearchResult resultItem2 : unfilteredTfidfResults) {
            largestTFIDF = resultItem2.getFrequency() > largestTFIDF ? resultItem2.getFrequency() : largestTFIDF;
            smallestTFIDF = resultItem2.getFrequency() < smallestTFIDF ? resultItem2.getFrequency() : smallestTFIDF;
        }

        for (FrequencySearchResult resultItem : unfilteredTfidfResults) {
            double matchPercentage = Math.round((resultItem.getFrequency() - smallestTFIDF) / (largestTFIDF - smallestTFIDF) * 100);
            if (matchPercentage < 5 || Double.isNaN(matchPercentage)) continue;

            filteredTfidfResults.add(resultItem);
        }

        if (!filteredTfidfResults.isEmpty()) {
            toFocus.updateTfidfSearches(filteredTfidfResults);
        } else {
            for (Entry cachedComponent : book.contentCache.keySet()) if (cachedComponent.isUnlocked(player)) {
                String cachedDocument = book.contentCache
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
                toFocus.updateMatchCountSearches(matchCountSearchResults);
            } else {
                toFocus.setAsBadSearch();
            }
        }

        if (book.focus instanceof ComponentSearchResults)
            book.forceInFocus(toFocus);
        else
            book.placeInFocus(toFocus);
    }

    public static class FrequencySearchResult implements Comparable<FrequencySearchResult> {

        private final Entry resultComponent;
        private final double frequency;

        public FrequencySearchResult(Entry resultComponent, double frequency) {
            this.resultComponent = resultComponent;
            this.frequency = frequency;
        }

        public Entry getResultComponent() {
            return resultComponent;
        }

        public double getFrequency() {
            return frequency;
        }

        @Override
        public int compareTo(@NotNull Search.FrequencySearchResult o) {
            return Double.compare(o.getFrequency(), getFrequency());
        }
    }

    public static class MatchCountSearchResult implements Comparable<MatchCountSearchResult> {

        private final Entry resultComponent;
        private final int nbOfMatches;

        public MatchCountSearchResult(Entry resultComponent, int nbOfMatches) {
            this.resultComponent = resultComponent;
            this.nbOfMatches = nbOfMatches;
        }

        public Entry getResultComponent() {
            return resultComponent;
        }

        public int getMatchCount() {
            return nbOfMatches;
        }

        @Override
        public int compareTo(@NotNull MatchCountSearchResult o) {
            return Double.compare(o.getMatchCount(), getMatchCount());
        }
    }
}
