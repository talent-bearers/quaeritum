package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.gui.components.ComponentStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.GhostRecipe;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static eladkay.quaeritum.client.gui.book.GuiBook.ARROW_HOME_PRESSED;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;

public class ComponentRecipe extends GuiComponent {

	private final GuiBook book;

	public ComponentRecipe(int posX, int posY, int width, int height, GuiBook book, String item) {
		super(posX, posY, width, height);
		this.book = book;

		IRecipe recipe = ForgeRegistries.RECIPES.getValue(new ResourceLocation(item));

		if (recipe == null) return;
		GhostRecipe ghostRecipe = createGhostRecipe(recipe, Minecraft.getMinecraft().player.openContainer.inventorySlots);

		ComponentStack output = new ComponentStack(
				(int) ((book.COMPONENT_BOOK.getSize().getX()) - 60), (int) ((book.COMPONENT_BOOK.getSize().getY() / 2.0) - 32));
		output.getStack().setValue(recipe.getRecipeOutput());
		output.getTransform().setScale(1.25);
		add(output);

		int row = 0, column = 0;
		for (int i = 1; i < recipe.getIngredients().size() + 1; i++) {
			GhostRecipe.GhostIngredient ingredient = ghostRecipe.get(i);

			int x = (int) ((book.COMPONENT_BOOK.getSize().getX()) - 60 - (16 * 3) - 32);
			int y = (int) ((book.COMPONENT_BOOK.getSize().getY() / 2.0) - 32 - 16);
			ComponentStack stack = new ComponentStack(x + row * 16, y + column * 16);
			stack.getStack().setValue(ingredient.getItem());
			add(stack);
			stack.BUS.hook(GuiComponentEvents.ComponentTickEvent.class, event -> {
				if (!stack.getStack().getValue(stack).isItemEqual(ingredient.getItem())) {
					stack.getStack().setValue(ingredient.getItem());
				}
			});

			if (++row >= 3) {
				column++;
				row = 0;
			}

		}

		BUS.hook(GuiComponentEvents.PostDrawEvent.class, event -> {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.translate((int) ((book.COMPONENT_BOOK.getSize().getX()) - 60 - 8), (int) ((book.COMPONENT_BOOK.getSize().getY() / 2.0) - 16), 0);
			GlStateManager.rotate(180, 0, 0, 1);
			Color highlight = book.highlightColor.brighter();
			GlStateManager.color(1f, 0.5f, 1f, 1f);
			ARROW_HOME_PRESSED.bind();
			ARROW_HOME_PRESSED.draw((int) event.getPartialTicks(), 0, 0);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.disableCull();
			GlStateManager.color(1f, 1f, 1f, 1f);
			//GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL_SMOOTH);

			int x = (int) ((book.COMPONENT_BOOK.getSize().getX()) - 60 - (16 * 3) - 32);
			int y = (int) ((book.COMPONENT_BOOK.getSize().getY() / 2.0) - 32 - 16);
			int bandWidth = 1;
			int excess = 6;

			GlStateManager.translate(x - (bandWidth / 2.0), y, 500);

			Color color = book.highlightColor;
			Color fadeOff = new Color(color.getRed(), color.getGreen(), color.getBlue(), 20);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(16 + bandWidth, 0 - excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(16, 0 - excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(16, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(16 + bandWidth, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(16 + bandWidth, 48 + excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(16, 48 + excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(16, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(16 + bandWidth, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(32 + bandWidth, 0 - excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(32, 0 - excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(32, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(32 + bandWidth, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(32 + bandWidth, 48 + excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(32, 48 + excess, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(32, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(32 + bandWidth, 24, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(0 - excess, 16 + bandWidth, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(0 - excess, 16, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(24, 16, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(24, 16 + bandWidth, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(48 + excess, 16 + bandWidth, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(48 + excess, 16, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(24, 16, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(24, 16 + bandWidth, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(0 - excess, 32 + bandWidth, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(0 - excess, 32, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(24, 32, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(24, 32 + bandWidth, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(48 + excess, 32 + bandWidth, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(48 + excess, 32, 200).color(fadeOff.getRed(), fadeOff.getGreen(), fadeOff.getBlue(), fadeOff.getAlpha()).endVertex();
			buffer.pos(24, 32, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			buffer.pos(24, 32 + bandWidth, 200).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();

			GlStateManager.popMatrix();

		});
	}

	private GhostRecipe createGhostRecipe(IRecipe recipe, List<Slot> slots) {
		GhostRecipe ghostRecipe = new GhostRecipe();
		ItemStack itemstack = recipe.getRecipeOutput();
		ghostRecipe.setRecipe(recipe);
		ghostRecipe.addIngredient(Ingredient.fromStacks(itemstack),
				(int) ((book.COMPONENT_BOOK.getSize().getX()) - 60), (int) ((book.COMPONENT_BOOK.getSize().getY() / 2.0) - 32));
		int i = 3;
		int j = 3;
		int k = recipe instanceof IShapedRecipe ? ((IShapedRecipe) recipe).getRecipeWidth() : i;
		int l = 1;
		Iterator<Ingredient> iterator = recipe.getIngredients().iterator();

		for (int i1 = 0; i1 < j; ++i1) {
			for (int j1 = 0; j1 < k; ++j1) {
				if (!iterator.hasNext()) {
					return ghostRecipe;
				}

				Ingredient ingredient = iterator.next();

				if (ingredient != Ingredient.EMPTY) {
					Slot slot = slots.get(l);
					ghostRecipe.addIngredient(ingredient, slot.xPos, slot.yPos);
				}

				++l;
			}

			if (k < i) {
				l += i - k;
			}
		}

		return ghostRecipe;
	}
}
