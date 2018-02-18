package eladkay.quaeritum.api.book.hierarchy.category;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Category {
    public final String modId;

    public final List<Entry> entries;
    public final String titleKey;
    public final String descKey;
    public final JsonElement icon;

    public Category(String modId, List<Entry> entries, String titleKey, String descKey, JsonElement icon) {
        this.modId = modId;
        this.entries = entries;
        this.titleKey = titleKey;
        this.descKey = descKey;
        this.icon = icon;
    }

    @Nullable
    public static Category fromJson(String modId, JsonObject json) {
        try {
            String title = json.getAsJsonPrimitive("title").getAsString();
            String desc = json.getAsJsonPrimitive("description").getAsString();
            JsonElement icon = json.get("icon");
            JsonArray allEntries = json.getAsJsonArray("entries");
            List<Entry> entries = Lists.newArrayList();
            for (JsonElement entryJson : allEntries) {
                JsonElement parsable = entryJson.isJsonPrimitive() ?
                        Book.getJsonFromLink(entryJson.getAsString()) : entryJson;
                Entry entry = Entry.fromJson(modId, parsable.getAsJsonObject());
                if (entry != null)
                    entries.add(entry);
            }
            return new Category(modId, entries, title, desc, icon);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
