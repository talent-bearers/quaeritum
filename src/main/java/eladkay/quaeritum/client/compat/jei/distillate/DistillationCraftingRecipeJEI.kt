package eladkay.quaeritum.client.compat.jei.distillate

import eladkay.quaeritum.api.alchemy.IAlchemicalComposition
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack

class DistillationCraftingRecipeJEI(val baseRecipe: IAlchemicalComposition) : IRecipeWrapper {

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists<ItemStack>(ItemStack::class.java, listOf(baseRecipe.exampleCompositeStack.matchingStacks.toList()))
        ingredients.setOutputLists(ItemStack::class.java, listOf(baseRecipe.exampleDustStack.matchingStacks.toList()))
        ingredients.setOutput<FluidStack>(FluidStack::class.java, baseRecipe.exampleLiquidStack)
    }

}
