package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import eladkay.quaeritum.api.lib.LibNBT;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface INetworkProvider {
    @Nullable
    default UUID getPlayer(@NotNull ItemStack stack) {
        return ItemNBTHelper.getUUID(stack, LibNBT.TAG_UUID);
    }

    @NotNull
    default ItemStack setPlayer(@NotNull ItemStack stack, @Nullable UUID player) {
        if (player == null) ItemNBTHelper.removeEntry(stack, LibNBT.TAG_UUID);
        else ItemNBTHelper.setUUID(stack, LibNBT.TAG_UUID, player);
        return stack;
    }

    boolean isProvider(@NotNull ItemStack stack);

    boolean isReceiver(@NotNull ItemStack stack);
}
