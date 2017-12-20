package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 7:59 PM on 8/20/17.
 */
public interface IDesiccation {

    /**
     * All fluid stacks involved in composition should take and produce 500 mB.
     */
    int LIQUID_VOLUME = 500;

    /**
     * Gets the solid dust stack that the liquid dries to.
     *
     * @param liquid The fluid stack, in case data needs to be preserved.
     * @return The residue stack produced by desiccation.
     */
    @NotNull
    ItemStack getDriedStack(@NotNull FluidStack liquid);

    /**
     * Whether a liquid can be desiccated.
     *
     * @param liquid The liquid to be checked.
     * @return Whether the desiccation is valid.
     */
    boolean matches(@NotNull FluidStack liquid);

    /**
     * @return An example liquid, for JEI and similar displays.
     */
    @NotNull
    FluidStack getExampleLiquidStack();

    /**
     * @return An example composite, for JEI and similar displays.
     */
    @NotNull
    ItemStack getExampleResultStack();
}
