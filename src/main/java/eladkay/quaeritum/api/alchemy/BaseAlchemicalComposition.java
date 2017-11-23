package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 8:40 PM on 8/20/17.
 */
public class BaseAlchemicalComposition implements IAlchemicalComposition {

    @NotNull
    public final Fluid fluid;
    @NotNull
    public final Ingredient dust, composite;

    public BaseAlchemicalComposition(@NotNull Fluid fluid, @NotNull Ingredient dust, @NotNull Ingredient composite) {
        this.fluid = fluid;
        this.dust = dust;
        this.composite = composite;
    }


    @NotNull
    @Override
    public FluidStack getLiquidStack(@NotNull ItemStack composite) {
        return new FluidStack(fluid, LIQUID_VOLUME);
    }

    @NotNull
    @Override
    public ItemStack getDustStack(@NotNull ItemStack composite) {
        return dust.getMatchingStacks()[0].copy();
    }

    @NotNull
    @Override
    public ItemStack getCompositeStack(@NotNull FluidStack fluid, @NotNull ItemStack dust) {
        return composite.getMatchingStacks()[0].copy();
    }

    @Override
    public boolean isComposable(@NotNull FluidStack fluid, @NotNull ItemStack dust) {
        return fluid.getFluid() == this.fluid && this.dust.test(dust);
    }

    @Override
    public boolean isDecomposable(@NotNull ItemStack composite) {
        return this.composite.test(composite);
    }

    @NotNull
    @Override
    public FluidStack getExampleLiquidStack() {
        return new FluidStack(fluid, LIQUID_VOLUME);
    }

    @NotNull
    @Override
    public Ingredient getExampleDustStack() {
        return dust;
    }

    @NotNull
    @Override
    public Ingredient getExampleCompositeStack() {
        return composite;
    }
}
