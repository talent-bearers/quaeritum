package eladkay.quaeritum.common.crafting.recipes

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.ShapelessOreRecipe

/**
 * @author WireSegal
 * *         Created at 12:12 PM on 5/8/16.
 */
class RecipeShapelessAnimusUpgrade(output: ItemStack, vararg input: Any) : ShapelessOreRecipe(output, *input) {

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
        return RecipeAnimusUpgrade.output(output, var1)
    }
}
