package eladkay.quaeritum.client.compat.jei.distillate

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.client.compat.jei.JEICompat
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.plugins.vanilla.furnace.FurnaceRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

object DistillationCraftingCategory : FurnaceRecipeCategory<DistillationCraftingRecipeJEI>(JEICompat.helper.guiHelper) {

    val background = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei_combinations.png"), 0, 54, 82, 54)

    override fun getUid() = "${LibMisc.MOD_ID}:distillation"
    override fun getTitle(): String = I18n.format("jei.${LibMisc.MOD_ID}.recipe.distillation")
    override fun getBackground(): IDrawable = background
    override fun drawExtras(minecraft: Minecraft) {
        animatedFlame.draw(minecraft, 1, 20)
        arrow.draw(minecraft, 24, 18)
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: DistillationCraftingRecipeJEI, ingredients: IIngredients) {
        recipeLayout.itemStacks.init(0, true, 0, 0)
        recipeLayout.itemStacks.init(1, false, 60, 9)
        recipeLayout.fluidStacks.init(0, false, 61, 32)

        recipeLayout.itemStacks.set(ingredients)
        recipeLayout.fluidStacks.set(ingredients)
    }

    override fun getModName() = LibMisc.MOD_ID
}
