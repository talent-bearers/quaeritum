package eladkay.quaeritum.api.animus;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 4:59 PM on 2/8/17.
 */
public interface IAnimusResource {
    int getAnimus(@NotNull ItemStack stack);

    @NotNull
    EnumAnimusTier getAnimusTier(@NotNull ItemStack stack);
}
