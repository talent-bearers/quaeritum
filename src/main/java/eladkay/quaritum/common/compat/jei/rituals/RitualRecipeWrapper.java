/*
 * Copyright (c) 2016. Eladkay & GitHub contributors. Please do not share the source.
 */

package eladkay.quaritum.common.compat.jei.rituals;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RitualRecipeWrapper implements IRecipeWrapper {

    private final List input;
    private final List output;
    private final String text;

    public RitualRecipeWrapper(RitualRecipeRecipe recipe) {
        input = recipe.inputs;
        output = recipe.outputs;
        text = recipe.optionalText;
    }

    @Override
    public List getInputs() {
        return input;
    }

    public String getOpText() {
        return text;
    }

    @Override
    public List getOutputs() {
        return ImmutableList.of(output);
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return ImmutableList.of();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return ImmutableList.of();
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
        FontRenderer fontRendererObj = FMLClientHandler.instance().getClient().fontRendererObj;
        fontRendererObj.drawString(text, minecraft.displayWidth / 2, minecraft.displayHeight / 2, 0xffffff);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return ImmutableList.of();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

}