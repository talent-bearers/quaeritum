package eladkay.quaritum.api.animus;

import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.api.lib.LibNBT;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ISoulstone {
    default int getAnimusLevel(@Nonnull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }

    @Nonnull
    default ItemStack setAnimus(@Nonnull ItemStack stack, int amount) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS,
                Math.min(getMaxAnimus(stack), Math.max(0, amount)));
        return stack;
    }

    default int getRarityLevel(@Nonnull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_RARITY, 0);
    }

    @Nonnull
    default ItemStack setRarity(@Nonnull ItemStack stack, int rarity) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_RARITY,
                Math.min(getMaxAnimus(stack), Math.max(0, rarity)));
        return stack;
    }

    int getMaxAnimus(@Nonnull ItemStack stack);

    int getMaxRarity(@Nonnull ItemStack stack);

    boolean isRechargeable(@Nonnull ItemStack stack);

    default void doPassive(@Nonnull ItemStack stack) {
        //NO-OP
    }
}
