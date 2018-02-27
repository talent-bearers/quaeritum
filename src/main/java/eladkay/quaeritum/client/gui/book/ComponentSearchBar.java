package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.client.gui.book.ComponentTextField.TextEditEvent;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ComponentSearchBar extends ComponentBookMark {

    private ComponentTextField text;

    public ComponentSearchBar(IBookGui book, int id, @Nullable Consumer<String> onType) {
        super(book, book.searchIconSprite(), id);

        text = new ComponentTextField(Minecraft.getMinecraft().fontRenderer,
                2, 1, getSize().getXi() - 44 - 2 * book.searchIconSprite().getWidth(), Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2);
        if (onType != null)
            text.BUS.hook(TextEditEvent.class, (TextEditEvent edit) -> onType.accept(edit.getWhole()));
        text.setEnabledColor(0xFFFFFF);
        text.setAutoFocus(true);
        add(text);

        clipping.setClipToBounds(true);

        BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {
            if (isForcedOpen()) {
                slideOutShort();
                text.setVisible(true);
            }
        });

        BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {
            if (isForcedOpen()) {
                slideIn();
                text.setVisible(false);
            }
        });
    }

    public boolean isForcedOpen() {
        return !text.isFocused() && text.getText().isEmpty();
    }
}
