package eladkay.quaeritum.client.compat.jei

import eladkay.quaeritum.api.alchemy.AlchemicalCompositions
import eladkay.quaeritum.api.alchemy.Desiccations
import eladkay.quaeritum.api.machines.BaseCentrifugeRecipe
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.client.compat.jei.centrifuge.CentrifugeCraftingCategory
import eladkay.quaeritum.client.compat.jei.centrifuge.CentrifugeCraftingRecipeJEI
import eladkay.quaeritum.client.compat.jei.compound.CompoundingCraftingCategory
import eladkay.quaeritum.client.compat.jei.compound.CompoundingCraftingRecipeJEI
import eladkay.quaeritum.client.compat.jei.desiccator.DesiccationCraftingCategory
import eladkay.quaeritum.client.compat.jei.desiccator.DesiccationCraftingRecipeJEI
import eladkay.quaeritum.client.compat.jei.distillate.DistillationCraftingCategory
import eladkay.quaeritum.client.compat.jei.distillate.DistillationCraftingRecipeJEI
import eladkay.quaeritum.common.block.ModBlocks
import mezz.jei.api.IJeiHelpers
import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import net.minecraft.item.ItemStack

@JEIPlugin
class JEICompat : IModPlugin {

    companion object {
        lateinit var helper: IJeiHelpers
            private set
    }

    override fun register(registry: IModRegistry) {
        helper = registry.jeiHelpers

        registry.addRecipes(CentrifugeRecipes.getRecipes().mapNotNull {
            if (it !is BaseCentrifugeRecipe) null else CentrifugeCraftingRecipeJEI(it)
        }, CentrifugeCraftingCategory.uid)
        registry.addRecipes(Desiccations.getRecipes().mapNotNull {
            if (it.exampleResultStack.isEmpty) null else DesiccationCraftingRecipeJEI(it)
        }, DesiccationCraftingCategory.uid)
        registry.addRecipes(AlchemicalCompositions.getRecipes().map {
            DistillationCraftingRecipeJEI(it)
        }, DistillationCraftingCategory.uid)
        registry.addRecipes(AlchemicalCompositions.getRecipes().map {
            CompoundingCraftingRecipeJEI(it)
        }, CompoundingCraftingCategory.uid)
        registry.addRecipeCatalyst(ItemStack(ModBlocks.centrifuge), CentrifugeCraftingCategory.uid)
        registry.addRecipeCatalyst(ItemStack(ModBlocks.desiccator), DesiccationCraftingCategory.uid)
        registry.addRecipeCatalyst(ItemStack(ModBlocks.spiralDistillate), DistillationCraftingCategory.uid)
        registry.addRecipeCatalyst(ItemStack(ModBlocks.compoundCrucible), CompoundingCraftingCategory.uid)
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        helper = registry.jeiHelpers
        registry.addRecipeCategories(CentrifugeCraftingCategory)
        registry.addRecipeCategories(DesiccationCraftingCategory)
        registry.addRecipeCategories(CompoundingCraftingCategory)
        registry.addRecipeCategories(DistillationCraftingCategory)
    }
}

