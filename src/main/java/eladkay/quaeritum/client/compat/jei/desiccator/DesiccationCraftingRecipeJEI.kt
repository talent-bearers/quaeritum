package eladkay.quaeritum.client.compat.jei.desiccator

import eladkay.quaeritum.api.alchemy.IDesiccation
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack

class DesiccationCraftingRecipeJEI(val baseRecipe: IDesiccation) : IRecipeWrapper {

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInput<FluidStack>(FluidStack::class.java, baseRecipe.exampleLiquidStack)
        ingredients.setOutput(ItemStack::class.java, baseRecipe.exampleResultStack)
    }

}
