package eladkay.quaritum.api.animus;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.item.ItemStack;

public interface ISoulstone {
    @Nullable
    int getAnimusLevel(@NotNull ItemStack stack);

    @NotNull
    ItemStack addAnimus(@NotNull ItemStack stack, @NotNull int amount);

    @Nullable
    int getMaxAnimus(@NotNull ItemStack stack);

    @Nullable
    boolean isRechargeable(@NotNull ItemStack stack);

    @Nullable
    void doPassive(@NotNull ItemStack stack);
}
