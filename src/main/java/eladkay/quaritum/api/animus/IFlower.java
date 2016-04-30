package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

public interface IFlower {
    int getRarity(ItemStack stack);

    int getAnimusFromStack(ItemStack stack);
}
