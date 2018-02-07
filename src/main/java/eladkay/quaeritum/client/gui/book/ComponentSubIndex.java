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

import java.util.HashMap;

public class ComponentSubIndex extends GuiComponent {

	private HashMap<Integer, GuiComponent> pages = new HashMap<>();

	private GuiComponent currentActive;

	public ComponentSubIndex(String path, JsonArray contentArray) {
		super(16, 16, GuiBook.COMPONENT_BOOK.getSize().getXi() - 32, GuiBook.COMPONENT_BOOK.getSize().getYi() - 32);

		ComponentVoid pageComponent = new ComponentVoid(0, 0, getSize().getXi(), getSize().getYi());
		add(pageComponent);
		currentActive = pageComponent;

		int itemsPerPage = 3;
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
				String link = path + subIndex.getAsJsonPrimitive("link").getAsString();
				String desc = subIndex.getAsJsonPrimitive("desc").getAsString();

				Sprite icon = null;
				ItemStack stack = ItemStack.EMPTY;
				if (subIndex.has("icon") && subIndex.get("icon").isJsonPrimitive()) {

					icon = new Sprite(new ResourceLocation(subIndex.getAsJsonPrimitive("icon").getAsString()));

				} else if (subIndex.has("item") && subIndex.get("item").isJsonPrimitive()) {

					Item itemIcon = ForgeRegistries.ITEMS.getValue(new ResourceLocation(subIndex.getAsJsonPrimitive("item").getAsString()));
					if (itemIcon != null) stack = new ItemStack(itemIcon);
				}

				ComponentVoid subIndexComponent = new ComponentVoid(0, 16 * count, getSize().getXi(), 16);

				ComponentText textComponent = new ComponentText(20, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.TOP);
				textComponent.getUnicode().setValue(true);
				textComponent.getText().setValue(title);
				subIndexComponent.add(textComponent);

				subIndexComponent.BUS.hook(GuiComponentEvents.MouseInEvent.class, (event) -> {
					textComponent.getText().setValue(" " + TextFormatting.ITALIC.toString() + title);
				});

				subIndexComponent.BUS.hook(GuiComponentEvents.MouseOutEvent.class, (event) -> {
					textComponent.getText().setValue(title);
				});

				Sprite finalIcon = icon;
				ItemStack finalStack = stack;
				subIndexComponent.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (event) -> {
					GlStateManager.pushMatrix();
					GlStateManager.color(1, 1, 1, 1);
					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();

					if (finalIcon != null) {
						finalIcon.getTex().bind();
						finalIcon.draw((int) ClientTickHandler.getPartialTicks(), 0, 0, 16, 16);
					} else if (!finalStack.isEmpty()) {
						RenderHelper.enableGUIStandardItemLighting();
						GlStateManager.enableRescaleNormal();

						RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
						itemRender.zLevel = 200.0f;
						itemRender.renderItemAndEffectIntoGUI(finalStack, 0, 0);
						itemRender.zLevel = 0.0f;

						GlStateManager.disableRescaleNormal();
						RenderHelper.disableStandardItemLighting();
					}

					GlStateManager.popMatrix();
				});

				pageComponent.add(subIndexComponent);


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

		ComponentNavBar navBar = new ComponentNavBar((getSize().getXi() / 2) - 35, getSize().getYi() + 16, 70, pages.size() - 1);
		add(navBar);

		navBar.BUS.hook(EventNavBarChange.class, (navBarChange) -> {
			GuiComponent subIndexPage = pages.get(navBarChange.getPage());
			subIndexPage.setVisible(true);
			currentActive.setVisible(false);
			currentActive = subIndexPage;
		});
	}
}
