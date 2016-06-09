package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFlower {

    int getRarity(@Nonnull ItemStack stack);

    int getAnimusFromStack(@Nonnull ItemStack stack);
}
