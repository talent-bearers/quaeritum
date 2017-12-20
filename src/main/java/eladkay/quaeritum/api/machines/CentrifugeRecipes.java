package eladkay.quaeritum.api.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WireSegal
 * Created at 9:56 AM on 2/1/17.
 */
public final class CentrifugeRecipes {
    @NotNull
    private static final List<ICentrifugeRecipe> recipes = new ArrayList<>();

    @NotNull
    public static List<ICentrifugeRecipe> getRecipes() {
        return recipes;
    }

    @Nullable
    public static ICentrifugeRecipe getRecipe(@NotNull IItemHandler handler, boolean heated) {
        for (ICentrifugeRecipe recipe : recipes)
            if (recipe.matches(handler, heated))
                return recipe;
        return null;
    }

    @NotNull
    public static ICentrifugeRecipe registerRecipe(@NotNull ICentrifugeRecipe recipe) {
        recipes.add(recipe);
        return recipe;
    }

    @NotNull
    public static BaseCentrifugeRecipe registerRecipe(@NotNull Object inputOne, @Nullable Object inputTwo, @NotNull ItemStack output) {
        return (BaseCentrifugeRecipe) registerRecipe(new BaseCentrifugeRecipe(inputOne, inputTwo, output));
    }
}
