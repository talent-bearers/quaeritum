package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashMap;

import static eladkay.quaeritum.client.gui.book.GuiBook.getJsonFromLink;

/**
 * Property of Demoniaque.
 * All rights reserved.
 */
public class ComponentIndex extends BookGuiComponent {

	private HashMap<Integer, GuiComponent> pages = new HashMap<>();

	private GuiComponent currentActive;
	private ComponentNavBar navBar;

	public ComponentIndex(GuiBook book, @Nonnull GuiComponent parent, JsonArray contentArray) {
		super(16, 16, book.COMPONENT_BOOK.getSize().getXi() - 32, book.COMPONENT_BOOK.getSize().getYi() - 32);

		ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
		add(pageComponent);
		currentActive = pageComponent;

		int itemsPerPage = 6;
		int page = 0;
		int count = 0;
		for (int i = 0; i < contentArray.size(); i++) {
			JsonElement element = contentArray.get(i);
			if (!element.isJsonObject()) continue;

			JsonObject subIndex = element.getAsJsonObject();

			if (subIndex.has("title") && subIndex.get("title").isJsonPrimitive()
					&& subIndex.has("link") && subIndex.get("link").isJsonPrimitive()
					&& subIndex.has("desc") && subIndex.get("desc").isJsonPrimitive()) {

				String title = subIndex.getAsJsonPrimitive("title").getAsString();
				String link = subIndex.getAsJsonPrimitive("link").getAsString();
				String desc = subIndex.getAsJsonPrimitive("desc").getAsString();

				Sprite icon = null;
				ItemStack stack = ItemStack.EMPTY;
				if (subIndex.has("icon") && subIndex.get("icon").isJsonPrimitive()) {

					icon = new Sprite(new ResourceLocation(subIndex.getAsJsonPrimitive("icon").getAsString()));

				} else if (subIndex.has("item") && subIndex.get("item").isJsonPrimitive()) {

					Item itemIcon = ForgeRegistries.ITEMS.getValue(new ResourceLocation(subIndex.getAsJsonPrimitive("item").getAsString()));
					if (itemIcon != null) stack = new ItemStack(itemIcon);
				}

				ComponentVoid indexButton = new ComponentVoid(0, 16 * count, getSize().getXi(), 16);

				// CONTENT CACHING
				{
					JsonElement jsonElement = getJsonFromLink(link);
					if (jsonElement == null || !jsonElement.isJsonObject()) return;

					JsonObject object = jsonElement.getAsJsonObject();
					if (object.has("type") && object.get("type").isJsonPrimitive()
							&& object.has("title") && object.get("title").isJsonPrimitive()
							&& object.has("content") && object.get("content").isJsonArray()) {

						String type = object.getAsJsonPrimitive("type").getAsString();
						if (type.equals("index")) {

							ComponentIndex index = new ComponentIndex(book, this, object.getAsJsonArray("content"));
							index.setVisible(false);
							book.COMPONENT_BOOK.add(index);

							book.pageLinks.put(indexButton, index);
						} else if (type.equals("content")) {
							ComponentContent content = new ComponentContent(book, this, object.getAsJsonArray("content"));
							content.setVisible(false);
							book.COMPONENT_BOOK.add(content);

							book.pageLinks.put(indexButton, content);
						}
					}
					indexButton.BUS.hook(GuiComponentEvents.MouseClickEvent.class, event -> {
						if (book.pageLinks.containsKey(event.component)) {
							GuiComponent component = book.pageLinks.get(event.component);
							if (component == null) return;
							component.setVisible(true);
							setVisible(false);
						}
					});
				}

				// SUB INDEX PLATE RENDERING
				{
					ComponentText textComponent = new ComponentText(20, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
					textComponent.getUnicode().setValue(true);
					textComponent.getText().setValue(title);
					indexButton.add(textComponent);

					indexButton.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
						textComponent.getText().setValue(" " + TextFormatting.ITALIC.toString() + title);
					});

					indexButton.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
						textComponent.getText().setValue(title);
					});

					Sprite finalIcon = icon;
					ItemStack finalStack = stack;
					indexButton.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
						if (finalIcon != null) {

							GlStateManager.pushMatrix();
							GlStateManager.color(1, 1, 1, 1);
							GlStateManager.enableAlpha();
							GlStateManager.enableBlend();
							GlStateManager.disableRescaleNormal();
							RenderHelper.disableStandardItemLighting();
							GlStateManager.disableLighting();

							finalIcon.getTex().bind();
							finalIcon.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, 16, 16);

							GlStateManager.enableLighting();
							GlStateManager.enableDepth();
							RenderHelper.enableStandardItemLighting();
							GlStateManager.enableRescaleNormal();
							GlStateManager.popMatrix();

						} else if (!finalStack.isEmpty()) {

							GlStateManager.pushMatrix();
							GlStateManager.enableBlend();
							GlStateManager.enableRescaleNormal();
							RenderHelper.enableGUIStandardItemLighting();

							RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
							itemRender.renderItemAndEffectIntoGUI(finalStack, 0, 0);
							itemRender.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, finalStack, 0, 0);

							RenderHelper.disableStandardItemLighting();
							GlStateManager.popMatrix();
						}
					});
				}

				pageComponent.add(indexButton);


				count++;
				if (count >= itemsPerPage) {
					pages.put(page++, pageComponent);
					pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
					add(pageComponent);
					pageComponent.setVisible(false);
					count = 0;
				}
			}
		}

		navBar = new ComponentNavBar(book, this, parent, (getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size() - 1);
		add(navBar);

		navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
			makeVisible();
		});
	}

	@Override
	public void makeVisible() {
		super.makeVisible();
		currentActive.setVisible(false);

		GuiComponent subIndexPage = pages.get(navBar.getPage());
		if (subIndexPage != null) {
			if (subIndexPage instanceof BookGuiComponent)
				((BookGuiComponent) subIndexPage).makeVisible();
			else subIndexPage.setVisible(true);
		}
		currentActive = subIndexPage;
	}
}
