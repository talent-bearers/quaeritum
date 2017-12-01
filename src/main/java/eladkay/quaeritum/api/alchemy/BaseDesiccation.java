package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 8:40 PM on 8/20/17.
 */
public class BaseDesiccation implements IDesiccation {

    @NotNull
    public final Fluid fluid;
    @NotNull
    public final ItemStack result;

    public BaseDesiccation(@NotNull Fluid fluid, @NotNull ItemStack result) {
        this.fluid = fluid;
        this.result = result;
    }

    @NotNull
    @Override
    public ItemStack getDriedStack(@NotNull FluidStack liquid) {
        return result.copy();
    }

    @Override
    public boolean matches(@NotNull FluidStack liquid) {
        return fluid == liquid.getFluid();
    }

    @NotNull
    @Override
    public FluidStack getExampleLiquidStack() {
        return new FluidStack(fluid, LIQUID_VOLUME);
    }

    @NotNull
    @Override
    public ItemStack getExampleResultStack() {
        return result.copy();
    }
}
