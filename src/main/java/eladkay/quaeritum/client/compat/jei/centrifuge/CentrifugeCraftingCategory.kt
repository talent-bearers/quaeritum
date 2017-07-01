package eladkay.quaeritum.client.compat.jei.centrifuge

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.client.compat.jei.JEICompat
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

object CentrifugeCraftingCategory : IRecipeCategory<CentrifugeCraftingRecipeJEI> {

    private val background = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MOD_ID, "textures/gui/centrifuge.png"), 0, 0, 72, 46)

    override fun getUid() = "${LibMisc.MOD_ID}:centrifuge"
    override fun getTitle(): String = I18n.format("jei.${LibMisc.MOD_ID}.recipe.centrifuge")
    override fun getBackground(): IDrawable = background
    override fun drawExtras(minecraft: Minecraft?) = Unit // NO-OP

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: CentrifugeCraftingRecipeJEI, ingredients: IIngredients) {
        recipeLayout.itemStacks.init(INPUT_ONE, true, 1, 1)
        recipeLayout.itemStacks.init(INPUT_TWO, true, 19, 1)
        recipeLayout.itemStacks.init(OUTPUT, false, 49, 24)

        recipeLayout.itemStacks.set(ingredients)
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String> {
        return emptyList()
    }

    override fun getModName() = LibMisc.MOD_ID

    override fun getIcon() = null

    private val INPUT_ONE = 0
    private val INPUT_TWO = 1
    private val OUTPUT = 2
}
