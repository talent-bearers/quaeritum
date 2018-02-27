package eladkay.quaeritum.api.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import eladkay.quaeritum.api.book.hierarchy.IBookElement;
import eladkay.quaeritum.api.book.hierarchy.book.Book;
import eladkay.quaeritum.api.book.hierarchy.entry.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @author WireSegal
 * Created at 9:08 AM on 2/27/18.
 */
@SideOnly(Side.CLIENT)
public interface IBookGui {

    static Runnable getRendererFor(JsonElement icon, Vec2d size) {
        return getRendererFor(icon, size, false);
    }

    static Runnable getRendererFor(JsonElement icon, Vec2d size, boolean mask) {
        if (icon == null) return null;

        if (icon.isJsonPrimitive()) {
            ResourceLocation iconLocation = new ResourceLocation(icon.getAsString());
            Sprite sprite = new Sprite(new ResourceLocation(iconLocation.getResourceDomain(),
                    "textures/" + iconLocation.getResourcePath() + ".png"));
            return () -> renderSprite(sprite, size, mask);
        } else if (icon.isJsonObject()) {
            ItemStack stack = CraftingHelper.getItemStack(icon.getAsJsonObject(), new JsonContext("minecraft"));
            if (!stack.isEmpty())
                return () -> renderStack(stack, size);
        }
        return null;
    }

    static void renderSprite(Sprite sprite, Vec2d size, boolean mask) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        if (!mask)
            GlStateManager.color(1, 1, 1, 1);

        sprite.getTex().bind();
        sprite.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, size.getXi(), size.getYi());

        GlStateManager.popMatrix();
    }

    static void renderStack(ItemStack stack, Vec2d size) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.scale(size.getX() / 16.0, size.getY() / 16.0, 0);

        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        itemRender.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, 0, 0);

        GlStateManager.enableAlpha();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    @NotNull
    GuiComponent getMainComponent();

    @Nullable
    GuiComponent getFocus();

    void setFocus(@Nullable GuiComponent component);

    @NotNull
    Stack<IBookElement> getHistory();

    @Nullable
    IBookElement getCurrentElement();

    void setCurrentElement(@Nullable IBookElement element);

    @NotNull
    Book getBook();

    @NotNull
    GuiComponent makeNavigationButton(int offsetIndex, Entry entry, @Nullable Consumer<ComponentVoid> extra);

    @NotNull
    Map<Entry, String> getCachedSearchContent();

    @NotNull
    Sprite bindingSprite();
    @NotNull
    Sprite pageSprite();
    @NotNull
    Sprite nextSprite(boolean pressed);
    @NotNull
    Sprite backSprite(boolean pressed);
    @NotNull
    Sprite homeSprite(boolean pressed);
    @NotNull
    Sprite bannerSprite();
    @NotNull
    Sprite bookmarkSprite();
    @NotNull
    Sprite searchIconSprite();
    @NotNull
    Sprite titleBarSprite();

    default IBookElement actualElement() {
        IBookElement current = getCurrentElement();
        if (current == null)
            return null;
        return current.heldElement();
    }

    default void placeInFocus(IBookElement element) {
        if (element == actualElement())
            return;

        IBookElement currentElement = getCurrentElement();
        if (currentElement != null)
            getHistory().push(currentElement);
        forceInFocus(element);
    }

    default void forceInFocus(IBookElement element) {
        if (element == actualElement())
            return;

        GuiComponent focus = getFocus();
        if (focus != null)
            focus.invalidate();
        focus = element.createComponent(this);
        setFocus(focus);
        getMainComponent().add(focus);
        setCurrentElement(element);
    }
}
