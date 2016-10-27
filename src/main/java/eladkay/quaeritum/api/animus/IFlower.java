package eladkay.quaeritum.api.animus;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IFlower {

    int getRarity(@Nonnull ItemStack stack);

    int getAnimusFromStack(@Nonnull ItemStack stack);
}
