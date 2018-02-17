package eladkay.quaeritum.api.book.provider;

import com.google.gson.JsonElement;
import eladkay.quaeritum.api.book.pageinstance.PageStructure;

public class PageProviderStructure implements PageProvider<PageStructure> {

	@Override
	public PageStructure create(JsonElement jsonElement) {
		return new PageStructure(jsonElement);
	}
}
