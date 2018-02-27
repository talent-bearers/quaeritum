package eladkay.quaeritum.client.gui.book;

import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author WireSegal
 * Created at 9:05 AM on 2/24/18.
 */
@SideOnly(Side.CLIENT)
public class TFIDFSearch implements ISearchAlgorithm {

    private final IBookGui book;

    public TFIDFSearch(IBookGui book) {
        this.book = book;
    }

    @Nullable
    public List<? extends Result> search(String type) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        String query = type.replace("'", "").toLowerCase(Locale.ROOT);
        String[] keywords = query.split(" ");

        ArrayList<FrequencySearchResult> unfilteredTfidfResults = new ArrayList<>();
        ArrayList<MatchCountSearchResult> matchCountSearchResults = new ArrayList<>();

        Map<Entry, String> contentCache = book.getCachedSearchContent();

        final int nbOfDocuments = contentCache.size();
        for (Entry cachedComponent : contentCache.keySet())
            if (cachedComponent.isUnlocked(player)) {
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
                                .map(Map.Entry::getValue)
                                .max(Double::compare)
                                .orElse(-1L);

                if (mostRepeatedWord != -1L) {
                    double documentTfidf = 0;
                    for (String keyword : keywords) {
                        long keywordOccurance = Pattern.compile("\\b" + keyword).splitAsStream(cachedDocument).count() - 1;
                        double termFrequency = 0.5 + (0.5 * keywordOccurance / mostRepeatedWord);

                        int keywordDocumentOccurance = 0;
                        for (Entry documentComponent : contentCache.keySet())
                            if (documentComponent.isUnlocked(player)) {
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

                    unfilteredTfidfResults.add(new FrequencySearchResult(cachedComponent, documentTfidf));
                }
            }

        ArrayList<FrequencySearchResult> filteredTfidfResults = new ArrayList<>();

        double largestTFIDF = 0, smallestTFIDF = Integer.MAX_VALUE;
        for (FrequencySearchResult resultItem2 : unfilteredTfidfResults) {
            largestTFIDF = resultItem2.frequency() > largestTFIDF ? resultItem2.frequency() : largestTFIDF;
            smallestTFIDF = resultItem2.frequency() < smallestTFIDF ? resultItem2.frequency() : smallestTFIDF;
        }

        for (FrequencySearchResult resultItem : unfilteredTfidfResults) {
            double matchPercentage = Math.round((resultItem.frequency() - smallestTFIDF) / (largestTFIDF - smallestTFIDF) * 100);
            if (matchPercentage < 5 || Double.isNaN(matchPercentage)) continue;

            filteredTfidfResults.add(resultItem);
        }

        if (!filteredTfidfResults.isEmpty()) {
            return filteredTfidfResults;
        } else {
            for (Entry cachedComponent : contentCache.keySet())
                if (cachedComponent.isUnlocked(player)) {
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
                return matchCountSearchResults;
            } else {
                return null;
            }
        }
    }

    public static class FrequencySearchResult implements Result {

        private final Entry resultComponent;
        private final double frequency;

        public FrequencySearchResult(Entry resultComponent, double frequency) {
            this.resultComponent = resultComponent;
            this.frequency = frequency;
        }

        @Override
        public boolean specificResults() {
            return true;
        }

        @Override
        public Entry found() {
            return resultComponent;
        }

        @Override
        public double frequency() {
            return frequency;
        }
    }

    public static class MatchCountSearchResult implements Result {

        private final Entry resultComponent;
        private final int nbOfMatches;

        public MatchCountSearchResult(Entry resultComponent, int nbOfMatches) {
            this.resultComponent = resultComponent;
            this.nbOfMatches = nbOfMatches;
        }

        @Override
        public boolean specificResults() {
            return false;
        }

        @Override
        public Entry found() {
            return resultComponent;
        }

        @Override
        public double frequency() {
            return nbOfMatches;
        }
    }
}
