package eladkay.quaeritum.api.book.hierarchy.category;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import eladkay.quaeritum.client.gui.book.ComponentCategoryPage;
import eladkay.quaeritum.client.gui.book.ComponentEntryPage;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Category implements IBookElement {
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

    @Override
    public @Nullable IBookElement getBookParent() {
        return book;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiComponent createComponent(GuiBook book) {
        if (isSingleEntry())
            return new ComponentEntryPage(book, entries.get(0));
        return new ComponentCategoryPage(book, this);
    }
}
