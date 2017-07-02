package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
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

    @NotNull
    default EnumAnimusTier getAnimusTier(@NotNull ItemStack stack) {
        return EnumAnimusTier.fromMeta(ItemNBTHelper.getInt(stack, LibNBT.TAG_RARITY, 0));
    }

    @NotNull
    default ItemStack setAnimusTier(@NotNull ItemStack stack, @NotNull EnumAnimusTier tier) {
        EnumAnimusTier maxTier = getMaxAnimusTier(stack);
        EnumAnimusTier newTier = maxTier.compareTo(maxTier) < 0 ? tier : maxTier;
        ItemNBTHelper.setInt(stack, LibNBT.TAG_RARITY, newTier.ordinal());
        return stack;
    }

    @NotNull
    default EnumAnimusTier getMaxAnimusTier(@NotNull ItemStack stack) {
        return EnumAnimusTier.QUAERITUS;
    }

    int getMaxAnimus(@NotNull ItemStack stack);
}
