package eladkay.quaeritum.api.book.provider;

import javax.annotation.Nullable;
import java.util.HashMap;

public class PageProviderRegistry {

	public static PageProviderRegistry INSTANCE = new PageProviderRegistry();

	public HashMap<String, PageProvider> pageProviderMap = new HashMap<>();

	private PageProviderRegistry() {
		pageProviderMap.put("text", new PageProviderText());
		pageProviderMap.put("recipe", new PageProviderRecipe());
		pageProviderMap.put("structure", new PageProviderStructure());
	}

	@Nullable
	public PageProvider getPageProvider(String type) {
		return pageProviderMap.getOrDefault(type, null);
	}
}
