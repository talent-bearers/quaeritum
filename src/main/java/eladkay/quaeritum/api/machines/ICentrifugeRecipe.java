package eladkay.quaeritum.api.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 9:57 AM on 2/1/17.
 */
public interface ICentrifugeRecipe {
    boolean matches(@NotNull IItemHandler handler, boolean heated);

    int steamRequired(@NotNull IItemHandler handler, boolean heated);

    void consumeInputs(@NotNull IItemHandler handler, boolean heated);

    @NotNull
    ItemStack getOutput(@NotNull IItemHandler handler, boolean heated);
}
