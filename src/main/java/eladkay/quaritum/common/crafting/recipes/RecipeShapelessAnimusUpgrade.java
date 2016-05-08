package eladkay.quaritum.common.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * @author WireSegal
 *         Created at 12:12 PM on 5/8/16.
 */
public class RecipeShapelessAnimusUpgrade extends ShapelessOreRecipe {
    public RecipeShapelessAnimusUpgrade(ItemStack output, Object... input) {
        super(output, input);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return RecipeAnimusUpgrade.output(output, var1);
    }
}
