package eladkay.quaeritum.api.book.hierarchy;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 9:35 PM on 2/19/18.
 */
public interface IBookElement {
    @Nullable
    IBookElement getParent();

    @SideOnly(Side.CLIENT)
    List<GuiComponent> createBookComponents(GuiBook book, Vec2d size);
}
