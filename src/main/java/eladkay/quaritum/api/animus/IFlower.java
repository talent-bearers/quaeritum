package eladkay.quaritum.api.animus;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.item.ItemStack;

public interface IFlower {

    @Nullable
    int getRarity(@NotNull ItemStack stack);

    @Nullable
    int getAnimusFromStack(@NotNull ItemStack stack);
}
