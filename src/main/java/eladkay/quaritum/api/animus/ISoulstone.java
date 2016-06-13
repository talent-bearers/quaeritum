package eladkay.quaritum.api.animus;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ISoulstone {
    int getAnimusLevel(@Nonnull ItemStack stack);

    @Nonnull
    ItemStack addAnimus(@Nonnull ItemStack stack, int amount);

    int getMaxAnimus(@Nonnull ItemStack stack);

    int getRarityLevel(@Nonnull ItemStack stack); //getRarity is a method in Item

    @Nonnull
    ItemStack addRarity(@Nonnull ItemStack stack, int amount);

    int getMaxRarity(@Nonnull ItemStack stack);

    boolean isRechargeable(@Nonnull ItemStack stack);

    void doPassive(@Nonnull ItemStack stack);
}
