package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentText;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
@SideOnly(Side.CLIENT)
public abstract class BookGuiComponent extends GuiComponent {

	@Nonnull
	private final GuiBook book;
	@Nullable
	private BookGuiComponent parent;

	public BookGuiComponent(int posX, int posY, int width, int height, @Nonnull GuiBook book, @Nullable BookGuiComponent parent) {
		super(posX, posY, width, height);
		this.book = book;
		this.parent = parent;
	}

	public abstract String getTitle();

	public abstract String getDescription();

	@Nonnull
	public GuiBook getBook() {
		return book;
	}

	@Nullable
	public abstract JsonElement getIcon();

	@Nullable
	public BookGuiComponent getLinkingParent() {
		return parent == null ? book.centralIndex : parent;
	}

	public void setLinkingParent(@Nonnull BookGuiComponent component) {
		this.parent = component;
	}

	public abstract void update();

	@Nonnull
	public abstract BookGuiComponent clone();

	public GuiComponent createIndexButton(int indexID, GuiBook book, @Nullable Consumer<GuiComponent> extra) {
		ComponentVoid indexButton = new ComponentVoid(0, 16 * indexID, getSize().getXi(), 16);

		if (extra != null) extra.accept(indexButton);

		indexButton.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
			book.focus.setVisible(false);
			book.focus = this;
			book.focus.setVisible(true);
			update();
		});

		// SUB INDEX PLATE RENDERING
		{
			ComponentText textComponent = new ComponentText(20, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
			textComponent.getUnicode().setValue(true);
			textComponent.getText().setValue(getTitle());
			indexButton.add(textComponent);

			indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
				textComponent.getText().setValue(" " + TextFormatting.ITALIC.toString() + getTitle());
			});

			indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
				textComponent.getText().setValue(TextFormatting.RESET.toString() + getTitle());
			});

			Runnable render = getRendererFor(getIcon());

			if (render != null)
                indexButton.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
                    render.run();
                });
		}

		return indexButton;
	}

	public static Runnable getRendererFor(JsonElement icon) {
	    return getRendererFor(icon, false);
	}

    public static Runnable getRendererFor(JsonElement icon, boolean mask) {
        if (icon == null) return null;

        if (icon.isJsonPrimitive()) {
            ResourceLocation iconLocation = new ResourceLocation(icon.getAsString());
            Sprite sprite = new Sprite(new ResourceLocation(iconLocation.getResourceDomain(),
                    "textures/" + iconLocation.getResourcePath() + ".png"));
            return () -> renderSprite(sprite, mask);
        } else if (icon.isJsonObject()) {
            ItemStack stack = CraftingHelper.getItemStack(icon.getAsJsonObject(), new JsonContext("minecraft"));
            if (!stack.isEmpty())
                return () -> renderStack(stack);
        }
        return null;
    }

	private static void renderSprite(Sprite sprite, boolean mask) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		if (!mask)
            GlStateManager.color(1, 1, 1, 1);

		sprite.getTex().bind();
		sprite.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, 16, 16);

		GlStateManager.popMatrix();
	}

	private static void renderStack(ItemStack stack) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();

		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
		itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
		itemRender.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, 0, 0);

		GlStateManager.enableAlpha();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}
