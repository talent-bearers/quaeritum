package eladkay.quaeritum.api.book.hierarchy.category;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Category {
    public final Book book;

    public final List<Entry> entries;
    public final String titleKey;
    public final String descKey;
    public final JsonElement icon;

    public boolean isValid = false;

    public Category(Book book, JsonObject json) {
        this.book = book;
        String title = "";
        String desc = "";
        JsonElement icon = new JsonObject();
        List<Entry> entries = Lists.newArrayList();

        try {
            title = json.getAsJsonPrimitive("title").getAsString();
            desc = json.getAsJsonPrimitive("description").getAsString();
            icon = json.get("icon");
            JsonArray allEntries = json.getAsJsonArray("entries");
            for (JsonElement entryJson : allEntries) {
                JsonElement parsable = entryJson.isJsonPrimitive() ?
                        Book.getJsonFromLink(entryJson.getAsString()) : entryJson;
                Entry entry = new Entry(this, parsable.getAsJsonObject());
                if (entry.isValid)
                    entries.add(entry);
            }
            if (!entries.isEmpty())
                isValid = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        this.titleKey = title;
        this.descKey = desc;
        this.icon = icon;
        this.entries = entries;
    }

    public boolean isSingleEntry() {
        return entries.size() == 1;
    }

}
