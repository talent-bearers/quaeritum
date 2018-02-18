package eladkay.quaeritum.api.book.hierarchy.book;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import static com.teamwizardry.librarianlib.features.helpers.CommonUtilMethods.getCurrentModId;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Book {

    private static List<Book> allBooks = Lists.newArrayList();
    public static boolean hasEverReloaded = false;

    static {
        ClientRunnable.registerReloadHandler(new ClientRunnable() {
            @Override
            @SideOnly(Side.CLIENT)
            public void runIfClient() {
                hasEverReloaded = true;
                for (Book book : allBooks)
                    book.reload();
            }
        });
    }

    public final ResourceLocation location;

    public List<Category> categories;
    public String headerKey;
    public String subtitleKey;
    public Color bookColor;
    public Color highlightColor;

    public Book(String name) {
        this(new ResourceLocation(getCurrentModId(), name));
    }

    public Book(ResourceLocation location) {
        this.location = location;

        bookColor = Color.WHITE;
        highlightColor = Color.WHITE;
        headerKey = "";
        subtitleKey = "";
        categories = Lists.newArrayList();
        allBooks.add(this);

        if (hasEverReloaded)
            reload();
    }

    public void reload() {
        try {
            JsonElement jsonElement = getJsonFromLink(location);
            if (jsonElement == null || !jsonElement.isJsonObject())
                return;
            JsonObject json = jsonElement.getAsJsonObject();
            bookColor = fromJsonElement(json.get("color"));
            highlightColor = fromJsonElement(json.get("highlight"));
            headerKey = json.getAsJsonPrimitive("title").getAsString();
            subtitleKey = json.getAsJsonPrimitive("subtitle").getAsString();
            JsonArray allCategories = json.getAsJsonArray("categories");
            categories = Lists.newArrayList();
            for (JsonElement categoryJson : allCategories) {
                Category category = Category.fromJson(location.getResourceDomain(), categoryJson.getAsJsonObject());
                if (category != null)
                    categories.add(category);
            }
        } catch (Exception error) {
            error.printStackTrace();
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
