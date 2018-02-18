package eladkay.quaeritum.api.book.hierarchy.book;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Book {
    public final String modId;

    public final List<Category> categories;
    public final String headerKey;
    public final String subtitleKey;
    public final Color bookColor;
    public final Color highlightColor;

    public Book(String modId, List<Category> categories, String headerKey, String subtitleKey, Color bookColor, Color highlightColor) {
        this.modId = modId;
        this.categories = categories;
        this.headerKey = headerKey;
        this.subtitleKey = subtitleKey;
        this.bookColor = bookColor;
        this.highlightColor = highlightColor;
    }

    @Nullable
    public static Book fromJson(String modId, JsonObject json) {
        try {
            Color color = fromJsonElement(json.get("color"));
            Color highlight = fromJsonElement(json.get("highlight"));
            String header = json.getAsJsonPrimitive("title").getAsString();
            String subtitle = json.getAsJsonPrimitive("subtitle").getAsString();
            JsonArray allCategories = json.getAsJsonArray("categories");
            List<Category> categories = Lists.newArrayList();
            for (JsonElement categoryJson : allCategories) {
                Category category = Category.fromJson(modId, categoryJson.getAsJsonObject());
                if (category != null)
                    categories.add(category);
            }
            return new Book(modId, categories, header, subtitle, color, highlight);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static Color fromJsonElement(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber())
                return new Color(primitive.getAsInt());
            else
                return new Color(Integer.decode(element.getAsString()));
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            return new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
        }
        return Color.WHITE;
    }

    public static JsonElement getJsonFromLink(String location) {
        return getJsonFromLink(new ResourceLocation(location));
    }

    public static JsonElement getJsonFromLink(ResourceLocation location) {
        InputStream stream = LibrarianLib.PROXY.getResource(location.getResourceDomain(), "book/" + location.getResourcePath() + ".json");
        if (stream == null) return null;

        InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        return new JsonParser().parse(reader);
    }
}
