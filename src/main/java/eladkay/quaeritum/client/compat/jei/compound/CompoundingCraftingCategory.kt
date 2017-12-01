package eladkay.quaeritum.client.compat.jei.compound

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.client.compat.jei.JEICompat
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.plugins.vanilla.furnace.FurnaceRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

object CompoundingCraftingCategory : FurnaceRecipeCategory<CompoundingCraftingRecipeJEI>(JEICompat.helper.guiHelper) {

    val background = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei_combinations.png"), 0, 0, 94, 54)

    override fun getUid() = "${LibMisc.MOD_ID}:compound"
    override fun getTitle(): String = I18n.format("jei.${LibMisc.MOD_ID}.recipe.compound")
    override fun getBackground(): IDrawable = background
    override fun drawExtras(minecraft: Minecraft) {
        animatedFlame.draw(minecraft, 10, 20)
        arrow.draw(minecraft, 36, 18)
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: CompoundingCraftingRecipeJEI, ingredients: IIngredients) {
        recipeLayout.fluidStacks.init(0, true, 19, 1)
        recipeLayout.itemStacks.init(0, true, 0, 0)
        recipeLayout.itemStacks.init(1, false, 72, 18)

        recipeLayout.fluidStacks.set(ingredients)
        recipeLayout.itemStacks.set(ingredients)
    }

    override fun getModName() = LibMisc.MOD_ID
}
