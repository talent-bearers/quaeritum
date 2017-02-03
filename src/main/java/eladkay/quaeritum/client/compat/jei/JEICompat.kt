package eladkay.quaeritum.client.compat.jei

import eladkay.quaeritum.api.machines.BaseCentrifugeRecipe
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.client.compat.jei.centrifuge.CentrifugeCraftingCategory
import eladkay.quaeritum.client.compat.jei.centrifuge.CentrifugeCraftingRecipeHandler
import eladkay.quaeritum.client.compat.jei.centrifuge.CentrifugeCraftingRecipeJEI
import eladkay.quaeritum.common.block.ModBlocks
import mezz.jei.api.*
import mezz.jei.api.ingredients.IModIngredientRegistration
import net.minecraft.item.ItemStack

@JEIPlugin
class JEICompat : IModPlugin {

    companion object {
        lateinit var helper: IJeiHelpers
            private set
    }

    override fun register(registry: IModRegistry) {
        helper = registry.jeiHelpers

        registry.addRecipeHandlers(CentrifugeCraftingRecipeHandler)
        registry.addRecipeCategories(CentrifugeCraftingCategory)
        registry.addRecipes(CentrifugeRecipes.getRecipes().mapNotNull {
            if (it !is BaseCentrifugeRecipe) null else CentrifugeCraftingRecipeJEI(it)
        })
        registry.addRecipeCategoryCraftingItem(ItemStack(ModBlocks.centrifuge), CentrifugeCraftingCategory.uid)

    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) = Unit //NO-OP
    override fun registerItemSubtypes(subtypeRegistry: ISubtypeRegistry) = Unit // NO-OP
    override fun registerIngredients(registry: IModIngredientRegistration) = Unit // NO-OP
}

