package eladkay.quaeritum.client.compat.jei.centrifuge

import mezz.jei.api.recipe.IRecipeHandler

object CentrifugeCraftingRecipeHandler : IRecipeHandler<CentrifugeCraftingRecipeJEI> {
    override fun getRecipeClass() = CentrifugeCraftingRecipeJEI::class.java
    override fun getRecipeWrapper(recipe: CentrifugeCraftingRecipeJEI) = recipe
    override fun isRecipeValid(recipe: CentrifugeCraftingRecipeJEI) = true
    override fun getRecipeCategoryUid(recipe: CentrifugeCraftingRecipeJEI) = CentrifugeCraftingCategory.uid
}
