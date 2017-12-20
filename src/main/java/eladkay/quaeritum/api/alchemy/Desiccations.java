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
 * Created at 9:56 AM on 2/1/17.
 */
public final class Desiccations {
    @NotNull
    private static final List<IDesiccation> recipes = new ArrayList<>();

    @NotNull
    public static List<IDesiccation> getRecipes() {
        return recipes;
    }

    @Nullable
    public static IDesiccation getRecipe(@NotNull FluidStack stack) {
        for (IDesiccation recipe : recipes)
            if (recipe.matches(stack))
                return recipe;
        return null;
    }

    @NotNull
    public static IDesiccation registerRecipe(@NotNull IDesiccation recipe) {
        recipes.add(recipe);
        return recipe;
    }

    @NotNull
    public static BaseDesiccation registerRecipe(@NotNull Fluid liquid, @NotNull ItemStack result) {
        return (BaseDesiccation) registerRecipe(new BaseDesiccation(liquid, result));
    }
}
