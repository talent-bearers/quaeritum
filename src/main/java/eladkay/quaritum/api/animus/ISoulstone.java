package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

public interface ISoulstone {
    int getAnimusLevel(ItemStack stack);

    ItemStack addAnimus(ItemStack stack, int amount);

    int getMaxAnimus(ItemStack stack);

    boolean isRechargeable(ItemStack stack);

    void doPassive(ItemStack stack);
}
