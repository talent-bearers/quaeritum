package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

public interface ISoulstone {
    int getAnimusLevel(ItemStack stack);

    ItemStack deductAnimus(ItemStack stack, int amount);

    ItemStack addAnimus(ItemStack stack, int amount);

    int getMaxAnimus();

    boolean isRechargeable();

    void doPassive(ItemStack stack);
}