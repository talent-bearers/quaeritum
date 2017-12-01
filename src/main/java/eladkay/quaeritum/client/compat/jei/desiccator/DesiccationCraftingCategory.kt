package eladkay.quaeritum.client.compat.jei.desiccator

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.client.compat.jei.JEICompat
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.config.Constants
import mezz.jei.plugins.vanilla.furnace.FurnaceRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n

object DesiccationCraftingCategory : FurnaceRecipeCategory<DesiccationCraftingRecipeJEI>(JEICompat.helper.guiHelper) {

    val background = JEICompat.helper.guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 0, 114, 82, 54)

    override fun getUid() = "${LibMisc.MOD_ID}:desiccator"
    override fun getTitle(): String = I18n.format("jei.${LibMisc.MOD_ID}.recipe.desiccator")
    override fun getBackground(): IDrawable = background
    override fun drawExtras(minecraft: Minecraft) {
        animatedFlame.draw(minecraft, 1, 20)
        arrow.draw(minecraft, 24, 18)
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: DesiccationCraftingRecipeJEI, ingredients: IIngredients) {
        recipeLayout.fluidStacks.init(INPUT_ONE, true, 1, 1)
        recipeLayout.itemStacks.init(OUTPUT, false, 60, 18)

        recipeLayout.itemStacks.set(ingredients)
        recipeLayout.fluidStacks.set(ingredients)
    }

    override fun getModName() = LibMisc.MOD_ID

    private val INPUT_ONE = 0
    private val OUTPUT = 0
}
