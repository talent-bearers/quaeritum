package eladkay.quaeritum.api.book.pageinstance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eladkay.quaeritum.api.book.provider.PageProvider;
import eladkay.quaeritum.api.book.provider.PageProviderRegistry;

import java.util.HashSet;
import java.util.Set;

public class PageInstanceFactory {

	public static PageInstanceFactory INSTANCE = new PageInstanceFactory();

	public Set<PageInstance> pageInstanceSet = new HashSet<>();

	private PageInstanceFactory() {
	}

	public PageInstance getPage(JsonElement element) {
		JsonElement providedElement = element;
		PageProvider provider = null;
		if (element.isJsonPrimitive())
			provider = PageProviderRegistry.INSTANCE.getPageProvider("text");
		else if (element.isJsonObject()) {
			JsonObject object = element.getAsJsonObject();
			if (object.has("type") && object.get("type").isJsonPrimitive()) {
				provider = PageProviderRegistry.INSTANCE.getPageProvider(object.getAsJsonPrimitive("type").getAsString());

				if (object.has("data") && object.get("data").isJsonObject()) {
					providedElement = object.get("data");
				}
			}
		}

		if (provider == null) return null;

		return provider.create(providedElement);
	}
}
