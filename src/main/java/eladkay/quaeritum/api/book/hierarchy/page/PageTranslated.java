package eladkay.quaeritum.api.book.hierarchy.page;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PageTranslated implements Page {

    private final String defaultValue;
    private final Map<String, String> translations = Maps.newHashMap();
    private final Entry entry;

    public PageTranslated(Entry entry, JsonObject jsonElement) {
        this.entry = entry;
        this.defaultValue = jsonElement.getAsJsonPrimitive("en_us").getAsString();
        for (Map.Entry<String, JsonElement> kv : jsonElement.entrySet())
            if (!kv.getKey().equals("type"))
                translations.put(kv.getKey(), kv.getValue().getAsString());
    }

    @Override
    public @NotNull Entry getEntry() {
        return entry;
    }

    @Override
    public Collection<String> getSearchableStrings() {
        return translations.values();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiComponent> createBookComponents(GuiBook book, Vec2d size) {
        List<GuiComponent> pages = new ArrayList<>();

        String lang = Minecraft.getMinecraft().gameSettings.language;

        String text = translations.getOrDefault(lang, defaultValue);

        List<String> list = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, 118 * 16);

        for (String section : list) {
            if (section.trim().isEmpty()) continue;

            ComponentText sectionComponent = new ComponentText(0, 0, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
            sectionComponent.getText().setValue(section);
            sectionComponent.getWrap().setValue(size.getXi());
            sectionComponent.getUnicode().setValue(true);

            pages.add(sectionComponent);
        }
        return pages;
    }
}
