package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ISoulstone {
    @Nullable
    int getAnimusLevel(@Nonnull ItemStack stack);

    @Nonnull
    ItemStack addAnimus(@Nonnull ItemStack stack, @Nonnull int amount);

    @Nullable
    int getMaxAnimus(@Nonnull ItemStack stack);

    @Nullable
    boolean isRechargeable(@Nonnull ItemStack stack);

    @Nullable
    void doPassive(@Nonnull ItemStack stack);
}
