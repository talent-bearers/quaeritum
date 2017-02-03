package eladkay.quaeritum.client.compat.jei.centrifuge

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.machines.BaseCentrifugeRecipe
import eladkay.quaeritum.client.compat.jei.JEICompat
import mezz.jei.api.gui.IDrawableAnimated
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class CentrifugeCraftingRecipeJEI(val baseRecipe: BaseCentrifugeRecipe) : BlankRecipeWrapper() {

    companion object {
        private val flame = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MOD_ID, "textures/gui/centrifuge.png"), 72, 0, 15, 14)
        private val animFlame = JEICompat.helper.guiHelper.createAnimatedDrawable(flame, 150, IDrawableAnimated.StartDirection.TOP, true)
    }

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists<ItemStack>(ItemStack::class.java, listOf(baseRecipe.inputOne, baseRecipe.inputTwo).filterNotNull())
        ingredients.setOutput(ItemStack::class.java, baseRecipe.output)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        if (baseRecipe.requiresHeat())
            animFlame.draw(minecraft, 2, 28)
    }

}
