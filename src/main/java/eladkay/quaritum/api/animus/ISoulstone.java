package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ISoulstone {
    int getAnimusLevel(@Nonnull ItemStack stack);

    @Nonnull
    ItemStack addAnimus(@Nonnull ItemStack stack, int amount);

    int getMaxAnimus(@Nonnull ItemStack stack);

    boolean isRechargeable(@Nonnull ItemStack stack);

    void doPassive(@Nonnull ItemStack stack);
}
