package eladkay.quaeritum.api.book.hierarchy.page;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import eladkay.quaeritum.client.gui.book.ComponentRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PageRecipe implements Page {

    private final ResourceLocation recipe;
    private final Entry entry;

    public PageRecipe(Entry entry, JsonObject jsonElement) {
        this.entry = entry;
        recipe = new ResourceLocation(jsonElement.getAsJsonPrimitive("recipe").getAsString());
    }

    @Override
    public @NotNull Entry getEntry() {
        return entry;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiComponent> createBookComponents(IBookGui book, Vec2d size) {
        return Lists.newArrayList(new ComponentRecipe(0, 0, size.getXi(), size.getYi(), book.getBook().bookColor, recipe, book.nextSprite(true)));
    }
}
