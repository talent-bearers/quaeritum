package eladkay.quaritum.common.crafting;

import eladkay.quaritum.common.crafting.recipes.RecipeAwakenedSoulstone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static eladkay.quaritum.common.item.ModItems.dormant;

public class ModRecipes {

    public static void init() {
        CraftingManager.getInstance().addRecipe(new RecipeAwakenedSoulstone());
        GameRegistry.addShapedRecipe(new ItemStack(dormant), "XYX", "XZX", "XYX", 'X', new ItemStack(Blocks.soul_sand), 'Z', new ItemStack(Items.diamond), 'Y', new ItemStack(Blocks.glass));
    }
}
