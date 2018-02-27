package eladkay.quaeritum.api.book.hierarchy.page;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import eladkay.quaeritum.api.book.structure.CachedStructure;
import eladkay.quaeritum.api.book.structure.StructureCacheRegistry;
import eladkay.quaeritum.client.gui.book.ComponentStructure;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class PageStructure implements Page {

    private final String structureName;
    private final Entry entry;
    private final CachedStructure structure;

    public PageStructure(Entry entry, JsonObject object) {
        this.entry = entry;
        structureName = object.getAsJsonPrimitive("name").getAsString();
        structure = StructureCacheRegistry.getStructureOrAdd(structureName);
    }

    @Override
    public @NotNull Entry getEntry() {
        return entry;
    }

    @Override
    public Collection<String> getSearchableStrings() {
        return Lists.newArrayList(structureName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiComponent> createBookComponents(IBookGui book, Vec2d size) {
        return Lists.newArrayList(
                new ComponentStructure(0, 0, size.getXi(), size.getYi(), structure));
    }
}
