package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 9:56 AM on 2/1/17.
 */
public final class AlchemicalCompositions {
    @NotNull
    private static final List<IAlchemicalComposition> recipes = new ArrayList<>();

    @NotNull
    public static List<IAlchemicalComposition> getRecipes() {
        return recipes;
    }

    @Nullable
    public static IAlchemicalComposition getRecipe(@NotNull FluidStack stack, @NotNull ItemStack dust) {
        for (IAlchemicalComposition recipe : recipes)
            if (recipe.isComposable(stack, dust))
                return recipe;
        return null;
    }

    @Nullable
    public static IAlchemicalComposition getRecipe(@NotNull ItemStack composite) {
        for (IAlchemicalComposition recipe : recipes)
            if (recipe.isDecomposable(composite))
                return recipe;
        return null;
    }

    @NotNull
    public static IAlchemicalComposition registerRecipe(@NotNull IAlchemicalComposition recipe) {
        recipes.add(recipe);
        return recipe;
    }

    @NotNull
    public static BaseAlchemicalComposition registerRecipe(@NotNull Fluid liquid, @NotNull ItemStack dust, @NotNull ItemStack composite) {
        return (BaseAlchemicalComposition) registerRecipe(new BaseAlchemicalComposition(liquid, Ingredient.fromStacks(dust), Ingredient.fromStacks(composite)));
    }

    @NotNull
    public static BaseAlchemicalComposition registerRecipe(@NotNull Fluid liquid, @NotNull Ingredient dust, @NotNull Ingredient composite) {
        return (BaseAlchemicalComposition) registerRecipe(new BaseAlchemicalComposition(liquid, dust, composite));
    }
}
