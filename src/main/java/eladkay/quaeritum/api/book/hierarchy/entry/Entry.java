package eladkay.quaeritum.api.book.hierarchy.entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eladkay.quaeritum.api.book.hierarchy.page.Page;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:19 PM on 2/17/18.
 */
public class Entry {
    public final String modId;

    public final List<Page> pages;
    public final String titleKey;
    public final String descKey;
    public final ItemStack icon;

    public Entry(String modId, List<Page> pages, String titleKey, String descKey, ItemStack icon) {
        this.modId = modId;
        this.pages = pages;
        this.titleKey = titleKey;
        this.descKey = descKey;
        this.icon = icon;
    }

    @Nullable
    public static Entry fromJson(String modId, JsonObject json) {
        try {
            String title = json.getAsJsonPrimitive("title").getAsString();
            String desc = json.getAsJsonPrimitive("description").getAsString();
            ItemStack icon = CraftingHelper.getItemStack(json.getAsJsonObject("icon"), new JsonContext(modId));
            JsonArray allPages = json.getAsJsonArray("content");
            List<Page> pages = Lists.newArrayList();
            for (JsonElement pageJson : allPages) {
                Page page = Page.fromJson(pageJson);
                if (page != null)
                    pages.add(page);
            }
            return new Entry(modId, pages, title, desc, icon);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
