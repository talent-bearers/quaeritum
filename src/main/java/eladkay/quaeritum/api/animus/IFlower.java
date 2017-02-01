package eladkay.quaeritum.api.animus;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IFlower {

    int getRarity(@NotNull ItemStack stack);

    int getAnimusFromStack(@NotNull ItemStack stack);
}
