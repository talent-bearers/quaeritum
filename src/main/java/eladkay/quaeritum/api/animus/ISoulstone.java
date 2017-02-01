package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.common.util.ItemNBTHelper;
import eladkay.quaeritum.api.lib.LibNBT;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface ISoulstone {
    default int getAnimusLevel(@NotNull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }

    @NotNull
    default ItemStack setAnimus(@NotNull ItemStack stack, int amount) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS,
                Math.min(getMaxAnimus(stack), Math.max(0, amount)));
        return stack;
    }

    default int getRarityLevel(@NotNull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_RARITY, 0);
    }

    @NotNull
    default ItemStack setRarity(@NotNull ItemStack stack, int rarity) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_RARITY,
                Math.min(getMaxAnimus(stack), Math.max(0, rarity)));
        return stack;
    }

    int getMaxAnimus(@NotNull ItemStack stack);

    int getMaxRarity(@NotNull ItemStack stack);

    boolean isRechargeable(@NotNull ItemStack stack);

    default void doPassive(@NotNull ItemStack stack) {
        //NO-OP
    }
}
