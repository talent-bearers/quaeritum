/*
 * Copyright (c) 2016. Eladkay & GitHub contributors. Please do not share the source.
 */

package eladkay.quaritum.common.compat.jei.rituals;

import eladkay.quaritum.common.block.ModBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nonnull;
import java.util.Collection;

public class RitualRecipeCatagory implements IRecipeCategory {
    public static final String UID = "quaritum.ritual";
    private final IDrawable background;
    private final IDrawable overlay;

    public RitualRecipeCatagory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(168, 64);
        overlay = guiHelper.createDrawable(new ResourceLocation("quaritum", "textures/gui/JEIOverlayRitualRecipe.png"),
                48, 48, 48, 48);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "quaritum.ritual";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Ritual Crafting";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        overlay.draw(minecraft, 32, 32);
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout iRecipeLayout, @Nonnull IRecipeWrapper iRecipeWrapper) {
        if (!(iRecipeWrapper instanceof RitualRecipeWrapper))
            return;

        RitualRecipeWrapper wrapper = ((RitualRecipeWrapper) iRecipeWrapper);
        int index = 0;
        FontRenderer fontRendererObj = FMLClientHandler.instance().getClient().fontRendererObj;
        fontRendererObj.drawString(wrapper.getOpText(), Minecraft.getMinecraft().displayWidth / 2, Minecraft.getMinecraft().displayHeight / 2, 0);
        iRecipeLayout.getItemStacks().init(index, true, 40, 0);
        if (wrapper.getInputs().get(0) instanceof Collection) {
            iRecipeLayout.getItemStacks().set(index, ((Collection<ItemStack>) wrapper.getInputs().get(0)));
        } else {
            iRecipeLayout.getItemStacks().set(index, ((ItemStack) wrapper.getInputs().get(0)));
        }

        index++;

        if (wrapper.getInputs().size() > 1) {
            iRecipeLayout.getItemStacks().init(index, true, 20, 0);
            iRecipeLayout.getItemStacks().set(index, ((ItemStack) wrapper.getInputs().get(1)));
            index++;
        }

        iRecipeLayout.getItemStacks().init(index, true, 70, 0);
        iRecipeLayout.getItemStacks().set(index, new ItemStack(ModBlocks.blueprint, 1, 0));
        index++;

        iRecipeLayout.getItemStacks().init(index, false, 99, 0);
        if (wrapper.getOutputs().get(0) instanceof Collection) {
            iRecipeLayout.getItemStacks().set(index, ((Collection<ItemStack>) wrapper.getOutputs().get(0)));
        } else {
            iRecipeLayout.getItemStacks().set(index, ((ItemStack) wrapper.getOutputs().get(0)));
        }
    }
}
