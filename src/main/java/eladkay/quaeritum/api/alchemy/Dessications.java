package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
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
public final class Dessications {
    @NotNull
    private static final List<IDessication> recipes = new ArrayList<>();

    @NotNull
    public static List<IDessication> getRecipes() {
        return recipes;
    }

    @Nullable
    public static IDessication getRecipe(@NotNull FluidStack stack) {
        for (IDessication recipe : recipes)
            if (recipe.matches(stack))
                return recipe;
        return null;
    }

    @NotNull
    public static IDessication registerRecipe(@NotNull IDessication recipe) {
        recipes.add(recipe);
        return recipe;
    }

    @NotNull
    public static BaseDessication registerRecipe(@NotNull Fluid liquid, @NotNull ItemStack result) {
        return (BaseDessication) registerRecipe(new BaseDessication(liquid, result));
    }
}
