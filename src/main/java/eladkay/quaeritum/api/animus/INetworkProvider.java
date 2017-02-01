package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.common.util.ItemNBTHelper;
import eladkay.quaeritum.api.lib.LibNBT;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public interface INetworkProvider {
    @Nullable
    default UUID getPlayer(@NotNull ItemStack stack) {
        String id = ItemNBTHelper.getString(stack, LibNBT.TAG_UUID, null);
        return id == null ? null : UUID.fromString(id);
    }

    @NotNull
    default ItemStack setPlayer(@NotNull ItemStack stack, @Nullable UUID player) {
        if (player == null) ItemNBTHelper.removeEntry(stack, LibNBT.TAG_UUID);
        else ItemNBTHelper.setString(stack, LibNBT.TAG_UUID, player.toString());
        return stack;
    }

    boolean isProvider(@NotNull ItemStack stack);

    boolean isReceiver(@NotNull ItemStack stack);
}
