package eladkay.quaritum.common.crafting;

import eladkay.quaritum.common.crafting.recipes.RecipeAwakenedSoulstone;
import eladkay.quaritum.common.crafting.recipes.RecipeChalk;
import eladkay.quaritum.common.crafting.recipes.RecipePassionate;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

import static eladkay.quaritum.common.item.ModItems.dormant;
import static eladkay.quaritum.common.lib.LibMisc.MOD_ID;

public class ModRecipes {

    public static void init() {
        CraftingManager.getInstance().addRecipe(new RecipeAwakenedSoulstone());
        RecipeSorter.register(MOD_ID + ":awakened", RecipeAwakenedSoulstone.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        CraftingManager.getInstance().addRecipe(new RecipeChalk());
        RecipeSorter.register(MOD_ID + ":chalk", RecipeChalk.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        CraftingManager.getInstance().addRecipe(new RecipePassionate());
        RecipeSorter.register(MOD_ID + ":passionate", RecipePassionate.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        GameRegistry.addShapedRecipe(new ItemStack(dormant), "XYX", "XZX", "XYX", 'X', new ItemStack(Blocks.soul_sand), 'Z', new ItemStack(Items.diamond), 'Y', new ItemStack(Blocks.glass));
        //GameRegistry.addShapedRecipe();
    }
}
