package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import eladkay.quaeritum.api.book.IBookGui;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ComponentMainIndex extends NavBarHolder {

    public ComponentMainIndex(@Nonnull IBookGui book) {
        super(0, 0, book.getMainComponent().getSize().getXi(), book.getMainComponent().getSize().getYi() - 16, book);

        // --------- BANNER --------- //
        {
            ComponentSprite componentBanner = new ComponentSprite(book.bannerSprite(), -8, 12);
            componentBanner.getColor().setValue(book.getBook().bookColor);
            add(componentBanner);

            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            ComponentText componentBannerText = new ComponentText(20, 5, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
            componentBannerText.getText().setValue(I18n.format(book.getBook().headerKey));
            componentBannerText.getColor().setValue(book.getBook().highlightColor);

            String subText = I18n.format(book.getBook().subtitleKey);
            ComponentText componentBannerSubText = new ComponentText(componentBanner.getSize().getXi() - 10, 2 + fontRenderer.FONT_HEIGHT, ComponentText.TextAlignH.RIGHT, ComponentText.TextAlignV.TOP);
            componentBannerSubText.getText().setValue(subText);
            componentBannerSubText.getUnicode().setValue(true);
            componentBannerSubText.getColor().setValue(book.getBook().highlightColor);

            componentBanner.add(componentBannerText, componentBannerSubText);
        }
        // --------- BANNER --------- //

        // --------- MAIN INDEX --------- //
        {

            ArrayList<GuiComponent> categories = new ArrayList<>();
            EntityPlayer player = Minecraft.getMinecraft().player;
            for (Category category : book.getBook().categories) {
                if (category.anyUnlocked(player)) {
                    ComponentCategoryButton component = new ComponentCategoryButton(0, 0, 24, 24, book, category);
                    add(component);
                    categories.add(component);
                }
            }

            int row = 0;
            int column = 0;
            int buffer = 8;
            int marginX = 28;
            int marginY = 45;
            int itemsPerRow = 3;
            for (GuiComponent button : categories) {

                button.setPos(new Vec2d(
                        marginX + (column * button.getSize().getXi()) + (column * buffer),
                        marginY + (row * button.getSize().getY()) + (row * buffer)));

                column++;

                if (column >= itemsPerRow) {
                    row++;
                    column = 0;
                }
            }
        }
        // --------- MAIN INDEX --------- //
    }
}
