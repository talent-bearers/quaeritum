package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

public interface IFlower {
    int getRarity();

    int getAnimusFromStack(ItemStack stack);
}
