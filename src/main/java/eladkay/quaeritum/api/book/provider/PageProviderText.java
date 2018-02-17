package eladkay.quaeritum.api.book.provider;

import com.google.gson.JsonElement;
import eladkay.quaeritum.api.book.pageinstance.PageText;

public class PageProviderText implements PageProvider<PageText> {

	@Override
	public PageText create(JsonElement jsonElement) {
		return new PageText(jsonElement);
	}
}
