package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 7:59 PM on 8/20/17.
 */
public interface IAlchemicalComposition {

    /**
     * All fluid stacks involved in composition should take and produce 500 mB.
     */
    int LIQUID_VOLUME = 500;


    /**
     * Gets the liquid stack that the reagent will decompose to.
     *
     * @param composite The composite stack, in case data needs to be preserved.
     * @return The liquid stack produced by decomposing this composite.
     */
    @NotNull
    FluidStack getLiquidStack(@NotNull ItemStack composite);

    /**
     * Gets the solid dust stack that the reagent will decompose to.
     *
     * @param composite The composite stack, in case data needs to be preserved.
     * @return The residue stack produced by decomposing this composite.
     */
    @NotNull
    ItemStack getDustStack(@NotNull ItemStack composite);

    /**
     * Gets the resulting composite stack that the fluid and dust will compose to.
     *
     * @param fluid The fluid stack, in case data needs to be preserved.
     * @param dust  The dust stack, in case data needs to be preserved.
     * @return The composite of the fluid and the dust.
     */
    @NotNull
    ItemStack getCompositeStack(@NotNull FluidStack fluid, @NotNull ItemStack dust);

    /**
     * Whether a fluid and a dust can be composited with this recipe.
     *
     * @param fluid The fluid to be checked.
     * @param dust  The dust to be checked.
     * @return Whether the composite is valid.
     */
    boolean isComposable(@NotNull FluidStack fluid, @NotNull ItemStack dust);

    /**
     * Whether a composite can be decomposed into a liquid and a dust.
     *
     * @param composite The composite to be checked.
     * @return Whether the decomposition is valid.
     */
    boolean isDecomposable(@NotNull ItemStack composite);

    /**
     * @return An example liquid, for JEI and similar displays.
     */
    @NotNull
    FluidStack getExampleLiquidStack();

    /**
     * @return An example dust, for JEI and similar displays.
     */
    @NotNull
    Ingredient getExampleDustStack();

    /**
     * @return An example composite, for JEI and similar displays.
     */
    @NotNull
    Ingredient getExampleCompositeStack();
}
