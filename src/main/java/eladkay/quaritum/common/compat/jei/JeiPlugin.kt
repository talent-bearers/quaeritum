package eladkay.quaritum.common.compat.jei

import amerifrance.guideapi.api.GuideAPI
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.book.ModBook
import mezz.jei.api.*
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

@JEIPlugin
class JeiPlugin : IModPlugin {

    private var jeiHelpers: IJeiHelpers? = null


    override fun register(registry: IModRegistry) {

        jeiHelpers = registry.jeiHelpers
        //jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.picture, 1, OreDictionary.WILDCARD_VALUE));
        //No need because nooping in the getsubitems
        //jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(GuideAPI.guideBook, 1, ItemModBook.meta()));
        registry.addDescription(ts(GuideAPI.guideBook, ModBook.meta()), "quaritum.bookdescjei")
        registry.addRecipeCategories(DiagramRecipeCatagory())
        registry.addRecipeCategoryCraftingItem(ts(ModBlocks.blueprint), "quaritum:diagram")

    }


    private fun ts(i: Item): ItemStack {
        return ItemStack(i)
    }

    private fun ts(i: Item, meta: Int): ItemStack {
        return ItemStack(i, 1, meta)
    }

    private fun ts(i: Block): ItemStack {
        return ItemStack(i)
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {

    }

    private inner class DiagramRecipeCatagory : IRecipeCategory<IRecipeWrapper> {

        override fun getUid(): String {
            return "quaritum:diagram"
        }

        override fun getTitle(): String {
            return "Diagram Crafting"
        }

        override fun getBackground(): IDrawable {
            return object : IDrawable {
                override fun getWidth(): Int {
                    return 32
                }

                override fun getHeight(): Int {
                    return 32
                }

                override fun draw(minecraft: Minecraft) {

                }

                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {

                }
            }
        }

        override fun drawExtras(minecraft: Minecraft) {

        }

        override fun drawAnimations(minecraft: Minecraft) {

        }

        override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper) {

        }
    }

}

