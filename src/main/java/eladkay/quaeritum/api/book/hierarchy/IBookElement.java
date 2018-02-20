package eladkay.quaeritum.api.book.hierarchy;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import eladkay.quaeritum.client.gui.book.GuiBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

/**
 * @author WireSegal
 * Created at 9:35 PM on 2/19/18.
 */
public interface IBookElement {
    @Nullable
    IBookElement getBookParent();

    @SideOnly(Side.CLIENT)
    GuiComponent createComponent(GuiBook book);
}
