package eladkay.quaeritum.client.compat.jei.compound

import eladkay.quaeritum.api.alchemy.IAlchemicalComposition
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack

class CompoundingCraftingRecipeJEI(val baseRecipe: IAlchemicalComposition) : IRecipeWrapper {

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists<ItemStack>(ItemStack::class.java, listOf(baseRecipe.exampleDustStack.matchingStacks.toList()))
        ingredients.setInput<FluidStack>(FluidStack::class.java, baseRecipe.exampleLiquidStack)
        ingredients.setOutput(ItemStack::class.java, baseRecipe.exampleCompositeStack.matchingStacks.firstOrNull()
                ?: ItemStack.EMPTY)
    }

}
