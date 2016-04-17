package eladkay.quaritum.common.animus;

import net.minecraft.item.ItemStack;

public interface ISoulStone {
    int getAnimusLevel(ItemStack stack);

    ItemStack deductAnimus(ItemStack stack, int amount);

    ItemStack addAnimus(ItemStack stack, int amount);

    int getMaxAnimus();

    boolean isRechargeable();

    void doPassive(ItemStack stack);
}
