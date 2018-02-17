package eladkay.quaeritum.api.book.provider;

import com.google.gson.JsonElement;
import eladkay.quaeritum.api.book.pageinstance.PageRecipe;

public class PageProviderRecipe implements PageProvider<PageRecipe> {

	@Override
	public PageRecipe create(JsonElement jsonElement) {
		return new PageRecipe(jsonElement);
	}
}
