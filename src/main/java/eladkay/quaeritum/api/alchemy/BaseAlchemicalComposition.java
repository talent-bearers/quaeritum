package eladkay.quaeritum.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 8:40 PM on 8/20/17.
 */
public class BaseAlchemicalComposition implements IAlchemicalComposition {

    @NotNull
    public final Fluid fluid;
    @NotNull
    public final ItemStack dust, composite;

    public BaseAlchemicalComposition(@NotNull Fluid fluid, @NotNull ItemStack dust, @NotNull ItemStack composite) {
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
        return dust.copy();
    }

    @NotNull
    @Override
    public ItemStack getCompositeStack(@NotNull FluidStack fluid, @NotNull ItemStack dust) {
        return composite.copy();
    }

    @Override
    public boolean isCompositable(@NotNull FluidStack fluid, @NotNull ItemStack dust) {
        return fluid.getFluid() == this.fluid && OreDictionary.itemMatches(this.dust, dust, false);
    }

    @Override
    public boolean isDecomposable(@NotNull ItemStack composite) {
        return OreDictionary.itemMatches(this.composite, composite, false);
    }

    @NotNull
    @Override
    public FluidStack getExampleLiquidStack() {
        return new FluidStack(fluid, LIQUID_VOLUME);
    }

    @NotNull
    @Override
    public ItemStack getExampleDustStack() {
        return dust;
    }

    @NotNull
    @Override
    public ItemStack getExampleCompositeStack() {
        return composite;
    }
}
