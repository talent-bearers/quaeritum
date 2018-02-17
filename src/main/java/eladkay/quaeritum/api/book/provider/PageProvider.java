package eladkay.quaeritum.api.book.provider;

import com.google.gson.JsonElement;
import eladkay.quaeritum.api.book.pageinstance.PageInstance;

public interface PageProvider<P extends PageInstance> {

	P create(JsonElement jsonElement);
}
