/*
 * Copyright (c) 2016. Eladkay & GitHub contributors. Please do not share the source.
 */

package eladkay.quaritum.common.compat.jei.rituals;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class RitualRecipeHandler implements IRecipeHandler {
    @Nonnull
    @Override
    public Class getRecipeClass() {
        return RitualRecipeRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return RitualRecipeCatagory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull Object o) {
        return new RitualRecipeWrapper((RitualRecipeRecipe) o);
    }

    @Override
    public boolean isRecipeValid(@Nonnull Object o) {
        return true;
    }
}
